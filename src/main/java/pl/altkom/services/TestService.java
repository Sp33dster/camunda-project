package pl.altkom.services;


import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestService implements JavaDelegate {


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("I'm checking CheckClientService.class");
    }
}
