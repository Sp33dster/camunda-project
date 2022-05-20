package pl.altkom.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService extends RestServiceDelegate{
    static final String BASE_URL = "http://localhost:8080/users";

}
