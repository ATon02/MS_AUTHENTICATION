package co.com.powerup.api.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

import java.util.List;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import co.com.powerup.api.dtos.request.RoleCreateDTO;
import co.com.powerup.api.dtos.request.UserCreateDTO;
import co.com.powerup.api.dtos.response.ErrorResponse;
import co.com.powerup.api.dtos.response.RoleResponse;
import co.com.powerup.api.dtos.response.UserResponse;



@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi publicApi(OpenApiCustomizer customizer) {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/v1/**")
                .addOpenApiCustomizer(customizer)
                .build();
    }

    @Bean
    @Primary
    public OpenApiCustomizer customizer() {
        return openApi -> {

        PathItem rolePath = new PathItem()
                .get(new Operation()
                        .operationId("findRoles")
                        .tags(List.of("Role"))
                        .summary("Obtiene todos los roles")
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("Lista de roles")
                                        .content(new Content()
                                                .addMediaType("application/json",
                                                        new io.swagger.v3.oas.models.media.MediaType()
                                                                .schema(new ArraySchema()
                                                                        .items(new Schema<>().$ref("#/components/schemas/RoleResponse"))
                                                                )
                                                )
                                        )
                                )
                        )
                )
                .post(new Operation()
                        .operationId("saveRole")
                        .tags(List.of("Role"))
                        .summary("Crea un nuevo rol")
                        .requestBody(new RequestBody()
                                .description("DTO para crear un rol")
                                .required(true)
                                .content(new Content()
                                        .addMediaType("application/json",
                                                new io.swagger.v3.oas.models.media.MediaType()
                                                        .schema(new Schema<>().$ref("#/components/schemas/RoleCreateDTO")))))
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("Rol creado")
                                        .content(new Content()
                                                .addMediaType("application/json",
                                                        new io.swagger.v3.oas.models.media.MediaType()
                                                                .schema(new Schema<>().$ref("#/components/schemas/RoleResponse")))))));

        openApi.path("/api/v1/roles", rolePath);

        PathItem userPath = new PathItem()
                .get(new Operation()
                        .operationId("findUsers")
                        .tags(List.of("User"))
                        .summary("Obtiene todos los usuarios")
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("Lista de usuarios")
                                        .content(new Content()
                                                .addMediaType("application/json",
                                                        new io.swagger.v3.oas.models.media.MediaType()
                                                                .schema(new ArraySchema()
                                                                        .items(new Schema<>().$ref("#/components/schemas/UserResponse"))
                                                                )
                                                )
                                        )
                                )
                        )
                )
                .post(new Operation()
                        .operationId("saveUser")
                        .tags(List.of("User"))
                        .summary("Crea un nuevo usuario")
                        .requestBody(new RequestBody()
                                .description("DTO para crear un usuario")
                                .required(true)
                                .content(new Content()
                                        .addMediaType("application/json",
                                                new io.swagger.v3.oas.models.media.MediaType()
                                                        .schema(new Schema<>().$ref("#/components/schemas/UserCreateDTO")))))
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("Usuario creado")
                                        .content(new Content()
                                                .addMediaType("application/json",
                                                        new io.swagger.v3.oas.models.media.MediaType()
                                                                .schema(new Schema<>().$ref("#/components/schemas/UserResponse")))))));

        openApi.path("/api/v1/users", userPath);

        openApi.getComponents()
                .addSchemas("RoleCreateDTO", new Schema<RoleCreateDTO>()
                        .addProperty("name", new StringSchema())
                        .addProperty("description", new StringSchema()))
                .addSchemas("RoleResponse", new Schema<RoleResponse>()
                        .addProperty("id", new IntegerSchema().format("int64"))
                        .addProperty("name", new StringSchema())
                        .addProperty("description", new StringSchema()))
                .addSchemas("UserCreateDTO", new Schema<UserCreateDTO>()
                        .addProperty("name", new StringSchema())
                        .addProperty("lastName", new StringSchema())
                        .addProperty("dateOfBirth", new StringSchema().format("date"))
                        .addProperty("address", new StringSchema())
                        .addProperty("phone", new StringSchema())
                        .addProperty("email", new StringSchema())
                        .addProperty("baseSalary", new NumberSchema().format("double"))
                        .addProperty("roleId", new IntegerSchema().format("int64")))
                .addSchemas("UserResponse", new Schema<UserResponse>()
                        .addProperty("idUser", new IntegerSchema().format("int64"))
                        .addProperty("name", new StringSchema())
                        .addProperty("lastName", new StringSchema())
                        .addProperty("email", new StringSchema())
                        .addProperty("identityDocument", new StringSchema())
                        .addProperty("phone", new StringSchema())
                        .addProperty("roleId", new IntegerSchema().format("int64"))
                        .addProperty("baseSalary", new NumberSchema().format("double"))
                        .addProperty("dateOfBirth", new StringSchema().format("date"))
                        .addProperty("address", new StringSchema()))
                .addSchemas("ErrorResponse", new Schema<ErrorResponse>()
                        .addProperty("status", new IntegerSchema().format("int32"))
                        .addProperty("message", new StringSchema())
                        .addProperty("path", new StringSchema())
                        .addProperty("timestamp", new StringSchema().format("date-time")));
        };
    }


    
}


