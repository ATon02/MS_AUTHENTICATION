package co.com.powerup.api.dtos.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public record UserCreateDTO(
    String name,
    String lastName,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth,
    String address,
    String phone,
    String email,
    Double baseSalary,
    Long roleId
) {}
