package pl.altkom.services;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class AddUserService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        String loginForm = (String) execution.getVariable("login");
        String nameForm = (String) execution.getVariable("name");
        String passwordForm = (String) execution.getVariable("password");

        log.info("############## {}", execution.getVariables());

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("login", loginForm);
        map.add("name", nameForm);
        map.add("password", passwordForm);


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        log.info("################# json request {}", request);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/users", request, String.class);

        log.info(response.toString());

    }
}
