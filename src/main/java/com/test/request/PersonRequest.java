package com.test.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class PersonRequest implements Serializable {

    @JsonProperty("first_name")
    @NotBlank(message = "Person first name can't be empty.")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "Person last name can't be empty.")
    private String lastName;

    @JsonProperty("age")
    @NotNull(message = "Person age can't be empty.")
    private Integer age;

    @JsonProperty("favourite_colour")
    @NotBlank(message = "Person favourite colour can't be empty.")
    private String favouriteColour;
}
