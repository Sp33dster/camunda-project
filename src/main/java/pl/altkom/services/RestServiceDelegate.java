package pl.altkom.services;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.altkom.utils.Commons;


@Service
@Slf4j
public class RestServiceDelegate implements JavaDelegate {

    protected Expression URL;
    protected Expression checkID;
    RestTemplate restTemplate = new RestTemplate();

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        Commons commons = new Commons();
        log.info("Commons field: {}", commons.field);

        String condition = "";
        if (!(getStringFromCheckID(execution).equals("ALL"))) {
            condition = getStringFromCheckID(execution);
        }
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(getEndpoint(URL) + condition, String.class);
            log.info("################### {}", response);

            execution.setVariable("existInDatabase", true);
        } catch (HttpClientErrorException ee) {
            log.error(ee.getMessage());
            execution.setVariable("existInDatabase", false);

        }
    }

    private String getStringFromCheckID(DelegateExecution execution) {
        return checkID.getValue(execution).toString();
    }

    private String getEndpoint(Expression URL) throws NoSuchFieldException, IllegalAccessException {

        String endpoint = URL.getExpressionText();
        log.info("URL USSER: {}", endpoint);
        return String.valueOf(Commons.class.getField(endpoint).get(null));
    }
}
