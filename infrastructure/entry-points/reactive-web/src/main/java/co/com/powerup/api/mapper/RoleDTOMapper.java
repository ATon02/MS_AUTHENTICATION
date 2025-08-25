package co.com.powerup.api.mapper;

import org.mapstruct.Mapper;

import co.com.powerup.api.dtos.request.RoleCreateDTO;
import co.com.powerup.api.dtos.response.RoleResponse;
import co.com.powerup.model.role.Role;

@Mapper(componentModel = "spring")
public interface RoleDTOMapper {

    RoleResponse toResponse(Role role);
    Role toModel(RoleCreateDTO roleCreateDTO);

}
