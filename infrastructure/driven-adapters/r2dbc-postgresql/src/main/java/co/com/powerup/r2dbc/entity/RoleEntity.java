package co.com.powerup.r2dbc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;



@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {

    @Id
    private Long id;
    private String name;
    private String description;
}
