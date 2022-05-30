package pl.altkom.services;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.altkom.domain.CarDTO;
import pl.altkom.utils.Commons;
import pl.altkom.utils.PreparedRequest;

import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class RestServiceDelegate implements JavaDelegate {

    protected Expression URL;
    protected Expression checkID;
    protected Expression methodHTTP;
    protected Expression detailURL;
    protected Expression valueMapping;
    protected Expression resp;


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info(valueMapping.toString() + valueMapping.getExpressionText());
        String completeURL = createCompleteURL(execution);
        RestTemplate restTemplate = new RestTemplate();
        decideAndPerformHTTPMethod(execution, completeURL, restTemplate);
    }

    private void decideAndPerformHTTPMethod(DelegateExecution execution, String completeURL, RestTemplate restTemplate) {

        switch (methodHTTP.getExpressionText()) {
            case "GET":
                performGetHTTPMethod(execution, completeURL, restTemplate);
                break;
            case "POST":
                performPostHTTPMethod(execution, completeURL, restTemplate);
                break;
            case "PATCH":
                performPatchHTTPMethod(execution, completeURL, restTemplate);
                break;

        }

    }

    private void performPostHTTPMethod(DelegateExecution execution, String completeURL,
                                       RestTemplate restTemplate) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

//        MultiValueMap<String, String> requestMap = PreparedRequest.fillBody(execution, valueMapping);
        Map<String, String> requestMap = new HashMap<>();
        PreparedRequest.fillBody(execution, valueMapping, requestMap);
        log.info(requestMap.toString());
        if (requestMap.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestMap, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(completeURL, HttpMethod.POST, request, String.class);
            log.info("POST Method");
            log.info(response.getBody() + response.getStatusCode());

        } catch (Exception e) {

            log.error(e.getMessage());
        }
    }

    private void performPatchHTTPMethod(DelegateExecution execution, String completeURL,
                                        RestTemplate restTemplate) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate = new RestTemplate(requestFactory);

        try {
            ResponseEntity<String> response = restTemplate.exchange(completeURL, HttpMethod.PATCH, null, String.class);
            log.info("Patch Method");
            log.info(response.getBody() + response.getStatusCode());

        } catch (Exception e) {


            log.error(e.getMessage());
        }

    }

    private void performGetHTTPMethod(DelegateExecution execution, String completeURL,
                                      RestTemplate restTemplate) {

        if ((detailURL != null) && ((detailURL).getExpressionText().equals("torent/"))) {
            ResponseEntity<CarDTO[]> responseDTO = restTemplate.exchange(completeURL, HttpMethod.GET, null, CarDTO[].class);
            CarDTO[] cars = responseDTO.getBody();

            ObjectValue carsMap = Variables.objectValue(cars)
                    .serializationDataFormat(Variables.SerializationDataFormats.JAVA).create();

            execution.setVariable("carsMap", carsMap);
        }

        try {
            ResponseEntity<String> response = restTemplate.exchange(completeURL, HttpMethod.GET, null, String.class);
            log.info("get Method");
            log.info(response.getBody() + response.getStatusCode());
            execution.setVariable("existInDatabase", true);
            execution.setVariable(resp.getExpressionText(), response.getBody());
        } catch (HttpClientErrorException ee) {
            log.error(ee.getMessage());
            execution.setVariable("existInDatabase", false);
        }
    }

//    private void performHTTPMethod(DelegateExecution execution, String comleteURL, HttpEntity<MultiValueMap<String, String>> request) throws NoSuchFieldException, IllegalAccessException {
//        RestTemplate restTemplate = new RestTemplate();
////        restTemplate = checkIfPATCHMethod(restTemplate);
////        checkIfGetAvailableCars(execution, comleteURL, request, restTemplate);
//        try {
//            ResponseEntity<String> response = restTemplate.exchange(comleteURL, checkHttpMethod(), request, String.class);
//            log.info("################### {}", response);
//            execution.setVariable("carMap", response.getBody());
//
//            execution.setVariable("existInDatabase", true);
//        } catch (HttpClientErrorException ee) {
//            log.error(ee.getMessage());
//            execution.setVariable("existInDatabase", false);
//
//        }
//    }

//    private void checkIfGetAvailableCars(DelegateExecution execution, String comleteURL, HttpEntity<MultiValueMap<String, String>> request, RestTemplate restTemplate) {
//        if ((detailURL != null) && ((detailURL).equals("torent/"))) {
//            ResponseEntity<CarDTO[]> response = restTemplate.exchange(comleteURL, checkHttpMethod(), request, CarDTO[].class);
//            CarDTO[] cars = response.getBody();
//
//            ObjectValue carsMap = Variables.objectValue(cars)
//                    .serializationDataFormat(Variables.SerializationDataFormats.JAVA).create();
//
//            execution.setVariable("carsMap", carsMap);
//        }
//    }

//    private RestTemplate checkIfPATCHMethod(RestTemplate restTemplate) {
//        if (methodHTTP.getExpressionText().equals("PATCH")) {
//            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//            restTemplate = new RestTemplate(requestFactory);
//        }
//        return restTemplate;
//    }

//    private HttpEntity<MultiValueMap<String, String>> doSomeMagicToAddEntity(DelegateExecution execution) {
//        String loginForm = (String) execution.getVariable("login");
//        String nameForm = (String) execution.getVariable("name");
//        String passwordForm = (String) execution.getVariable("password");
//
//        log.info("############## {}", execution.getVariables());
//
//        HttpHeaders headers = new HttpHeaders();
//
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("login", loginForm);
//        map.add("name", nameForm);
//        map.add("password", passwordForm);
//
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
//        return request;
//    }

    private String createCompleteURL(DelegateExecution execution) throws NoSuchFieldException, IllegalAccessException {
        StringBuilder stringURL = new StringBuilder();
        String detail = "";

        if (detailURL != null) {
            detail = getEndpoint(detailURL);
        }
        String condition = "";

        if (!(getStringFromCheckID(execution).equals("ALL"))) {
            condition = getStringFromCheckID(execution);
        }

        stringURL.append(getEndpoint(URL)).append(detail).append(condition);

        return stringURL.toString();
    }

    private String getStringFromCheckID(DelegateExecution execution) {
        return checkID.getValue(execution).toString();
    }

    private String getEndpoint(Expression URL) throws NoSuchFieldException, IllegalAccessException {
        String endpoint = URL.getExpressionText();
        return String.valueOf(Commons.class.getField(endpoint).get(null));
    }
}
