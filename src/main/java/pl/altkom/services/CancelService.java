package pl.altkom.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CancelService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        log.info("############ {}", execution.getVariables());
        Long selectedCarID = (Long) execution.getVariable("selectedCarID");
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        try {
            ResponseEntity<String> response = restTemplate.exchange("http://localhost:8081/cars/return/" + selectedCarID, HttpMethod.PATCH, null, String.class);

            log.info("################### {}", response);

        } catch (HttpClientErrorException ee) {
            log.error(ee.getMessage());

        }

    }
}
