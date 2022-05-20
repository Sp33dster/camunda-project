package pl.altkom.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.altkom.domain.CarDTO;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class GetAvailableCarsService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {



        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<CarDTO[]> response = restTemplate
                    .exchange("http://localhost:8081/cars/torent", HttpMethod.GET, null, CarDTO[].class);

            log.info("################### {}", response);

            CarDTO[] cars = response.getBody();

            ObjectValue carsMap = Variables.objectValue(cars)
                    .serializationDataFormat(Variables.SerializationDataFormats.JAVA).create();

            log.info(cars[0].toString());
            execution.setVariable("carsMap", carsMap);
            execution.setVariable("getAvailableCars", true);
        } catch (HttpClientErrorException ee) {
            log.error(ee.getMessage());
            execution.setVariable("getAvailableCars", false);

        }

    }
}
