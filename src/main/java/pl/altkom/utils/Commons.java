package pl.altkom.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Commons {
    public static String URL_USER;
    public static String URL_CAR;
    @Value("${test.test}")
    public String field;

    @Value("${base.url.path.user}")
    public void setUrlUser(String urlUser) {
        Commons.URL_USER = urlUser;
    }

    @Value("${base.url.path.car}")
    public void setURL_CAR(String URL_CAR) {
        Commons.URL_CAR = URL_CAR;
    }
}
