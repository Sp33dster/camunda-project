package pl.altkom.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Commons {
    public static String URL_USER;
    public static String URL_CAR;
    public static String URL_CAR_RESERVE;
    public static String URL_CAR_TORENT;
    public static String URL_CAR_RENT;
    public static String URL_CAR_CANCEL;
    @Value("${test.test}")
    public String field;

    @Value("${path.url.base.reserve}")
    public void setUrlCarReserve(String urlCarReserve) {
        Commons.URL_CAR_RESERVE = urlCarReserve;
    }

    @Value("${path.url.base.torent}")
    public void setUrlCarTorent(String urlCarTorent) {
        Commons.URL_CAR_TORENT = urlCarTorent;
    }

    @Value("${path.url.base.rent}")
    public void setUrlCarRent(String urlCarRent) {
        Commons.URL_CAR_RENT = urlCarRent;
    }
    @Value("${path.url.base.cancel}")
    public void setUrlCarCancel(String urlCarCancel) {
        Commons.URL_CAR_CANCEL = urlCarCancel;
    }

    @Value("${path.url.base.user}")
    public void setUrlUser(String urlUser) {
        Commons.URL_USER = urlUser;
    }

    @Value("${path.url.base.car}")
    public void setURL_CAR(String URL_CAR) {
        Commons.URL_CAR = URL_CAR;
    }
}
