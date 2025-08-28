package co.com.powerup.r2dbc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class UserEntity {

    @Id
    private Long idUser;
    private String name;
    private String lastName;
    private String email;
    private String identityDocument;
    private String phone;
    private Long roleId; 
    private Double baseSalary;
    private LocalDate dateOfBirth;
    private String address;
    private String password;
}
