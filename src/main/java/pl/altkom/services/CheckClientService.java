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
public class CheckClientService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {


        log.info("############ {}", delegateExecution.getVariables());
        Long userID = (Long) delegateExecution.getVariable("userID");
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/users/" + userID, String.class);
            log.info("################### {}", response);

            delegateExecution.setVariable("existInDatabase", true);
        } catch (HttpClientErrorException ee) {
            log.error(ee.getMessage());
            delegateExecution.setVariable("existInDatabase", false);

        }


//        URL url = new URL("http://localhost:8080/users");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//        connection.connect();
//
//        int responseCode = connection.getResponseCode();
//        JSONArray dataObject;
//        if (responseCode != 200) {
//            throw new RuntimeException("HttpResponseCode: " + responseCode);
//        } else {
//            StringBuilder allUsersData = new StringBuilder();
//            Scanner scanner = new Scanner(url.openStream());
//
//            while (scanner.hasNext()) {
//                allUsersData.append(scanner.nextLine());
//            }
//
//            scanner.close();
//
//            System.out.println(allUsersData);
//
//            JSONParser parser = new JSONParser();
//            dataObject = (JSONArray) parser.parse(String.valueOf(allUsersData));
//
//            System.out.println(dataObject.get(0));
//
//            JSONObject data = (JSONObject) dataObject.get(0);
//
//            System.out.println(data);
//        }
//        delegateExecution.setVariable("existInDatabase", AppUtil.randomBoolean());
//
//        if (delegateExecution.getVariable("existInDatabase").equals(false)){
//            delegateExecution.setVariable("toAddUser", AppUtil.randomBoolean());
//        }
    }
}
