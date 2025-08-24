package co.com.powerup.api.mapper;

import org.mapstruct.Mapper;

import co.com.powerup.api.dtos.request.UserCreateDTO;
import co.com.powerup.api.dtos.response.UserReponse;
import co.com.powerup.model.user.User;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    UserReponse toResponse(User user);
    User toModel(UserCreateDTO userCreateDTO);

}
