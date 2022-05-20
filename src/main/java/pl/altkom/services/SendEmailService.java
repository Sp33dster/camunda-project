package pl.altkom.services;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SendEmailService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8081/cars/sendEmail", String.class);
            log.info("################### {}", response);


        } catch (HttpClientErrorException ee) {
            log.error(ee.getMessage());

        }
    }
}
