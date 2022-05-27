package pl.altkom.utils;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PreparedRequest {

    private static final String COMMA = ",";
    private static final String EQUALS = "=";

    public static Map<String, String> fillBody(DelegateExecution execution, Expression expression) {

        String executionString = expression.getExpressionText();
        log.info("Ourn string to be {}", executionString);
        Map<String, String> mapString = new HashMap<>();
        String[] executionStringsplitByComma = executionString.split(COMMA);

        Map<String, String> map = new HashMap<>();
        for (String s :
                executionStringsplitByComma) {
            String[] exectuionStringSplitByEquals = s.split(EQUALS);
            mapString.put(exectuionStringSplitByEquals[0], exectuionStringSplitByEquals[1]);
            log.info("Another split be like {}", Arrays.toString(exectuionStringSplitByEquals));
        }

        for (String value :
                mapString.values()) {

            String variableString = (String) execution.getVariable(value);
            if (variableString.equals("")) {
                map = null;
                break;
            }
            map.put(value, variableString);
        }

        return map;

    }

    public static void fillBody(DelegateExecution execution, Expression expression, Map<String, String> valueMap) {

        valueMap.putAll(fillBody(execution, expression));

    }
}
