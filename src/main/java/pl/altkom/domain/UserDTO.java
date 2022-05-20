package pl.altkom.domain;

import lombok.Data;

@Data
public class UserDTO extends ModelDTO {

    private String login;

    private String name;

    private String password;
}
