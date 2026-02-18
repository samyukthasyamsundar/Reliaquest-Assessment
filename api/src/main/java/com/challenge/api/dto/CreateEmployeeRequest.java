package com.challenge.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Data;

@Data
public class CreateEmployeeRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull @Min(0)
    private Integer salary;

    @NotNull @Min(1)
    private Integer age;

    @NotBlank
    private String jobTitle;

    @NotBlank
    @Email
    private String email;

    @NotNull private Instant contractHireDate;
}
