package pl.altkom.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class CarDTO extends ModelDTO implements Serializable {

    private String brand;

    private String model;

    private String registrationNumber;

    private int engineCapacity;

    private String carStatus;

}
