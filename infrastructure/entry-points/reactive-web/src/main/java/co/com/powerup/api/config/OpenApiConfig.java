package co.com.powerup.api.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

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
            openApi.getComponents()
                    .addSecuritySchemes("bearerAuth",
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT"));
            openApi.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
            // PATHS DE ROLES
            PathItem rolePath = new PathItem()
                    .get(new Operation()
                            .operationId("findRoles")
                            .tags(List.of("Role"))
                            .summary("Obtiene todos los roles")
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Lista de roles")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new ArraySchema()
                                                                            .items(new Schema<>().$ref(
                                                                                    "#/components/schemas/RoleResponse"))))))))
                    .post(new Operation()
                            .operationId("saveRole")
                            .tags(List.of("Role"))
                            .summary("Crea un nuevo rol")
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                            .requestBody(new RequestBody()
                                    .description("DTO para crear un rol")
                                    .required(true)
                                    .content(new Content()
                                            .addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>()
                                                                    .$ref("#/components/schemas/RoleCreateDTO")))))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Rol creado")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new Schema<>().$ref(
                                                                            "#/components/schemas/RoleResponse")))))));
            openApi.path("/api/v1/roles", rolePath);
            // PATHS DE USUARIOS
            PathItem userPath = new PathItem()
                    .get(new Operation()
                            .operationId("findUsers")
                            .tags(List.of("User"))
                            .summary("Obtiene todos los usuarios")
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Lista de usuarios")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new ArraySchema()
                                                                            .items(new Schema<>().$ref(
                                                                                    "#/components/schemas/UserResponse"))))))))
                    .post(new Operation()
                            .operationId("saveUser")
                            .tags(List.of("User"))
                            .summary("Crea un nuevo usuario")
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                            .requestBody(new RequestBody()
                                    .description("DTO para crear un usuario")
                                    .required(true)
                                    .content(new Content()
                                            .addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>()
                                                                    .$ref("#/components/schemas/UserCreateDTO")))))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Usuario creado")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new Schema<>().$ref(
                                                                            "#/components/schemas/UserResponse")))))));
            openApi.path("/api/v1/users", userPath);
            PathItem userFindByEmailPath = new PathItem()
                    .get(new Operation()
                            .operationId("findUserByEmail")
                            .tags(List.of("User"))
                            .summary("Obtiene un usuario por su email")
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                            .parameters(List.of(
                                    new Parameter()
                                            .name("email")
                                            .in("query")
                                            .description("Email del usuario a buscar")
                                            .required(true)
                                            .schema(new StringSchema())
                            ))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Usuario encontrado")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new Schema<>().$ref(
                                                                            "#/components/schemas/UserResponse")))))));  
            openApi.path("/api/v1/users/find-by-email", userFindByEmailPath);    
            PathItem saveAdminPath = new PathItem()
                    .post(new Operation()
                            .operationId("saveUserAdmin")
                            .tags(List.of("User"))
                            .summary("Crea un nuevo usuario como administrador")
                            .description("Este endpoint permite crear usuarios únicamente por administradores. ")
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                            .requestBody(new RequestBody()
                                    .description("DTO para crear un usuario con rol admin o asesor")
                                    .required(true)
                                    .content(new Content()
                                            .addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>()
                                                                    .$ref("#/components/schemas/UserCreateDTOAdmin")))))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Usuario administrador creado")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new Schema<>().$ref(
                                                                            "#/components/schemas/UserResponse")))))));

            openApi.path("/api/v1/users/admin", saveAdminPath);                                     
            // PATHS DE AUTH
            PathItem loginPath = new PathItem()
                    .post(new Operation()
                            .operationId("login")
                            .tags(List.of("Auth"))
                            .summary("Inicia sesión y obtiene un JWT")
                            .requestBody(new RequestBody()
                                    .description("Credenciales de acceso")
                                    .required(true)
                                    .content(new Content()
                                            .addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>()
                                                                    .$ref("#/components/schemas/LoginRequest"))
                                                            .examples(new java.util.LinkedHashMap<>() {
                                                                {
                                                                    put("admin",
                                                                            new io.swagger.v3.oas.models.examples.Example()
                                                                                    .summary("Usuario Administrador")
                                                                                    .value(new java.util.LinkedHashMap<String, Object>() {
                                                                                        {
                                                                                            put("email",
                                                                                                    "root@admin.com");
                                                                                            put("password",
                                                                                                    "rootAdmin");
                                                                                        }
                                                                                    }));
                                                                    put("asesor",
                                                                            new io.swagger.v3.oas.models.examples.Example()
                                                                                    .summary("Usuario Asesor")
                                                                                    .value(new java.util.LinkedHashMap<String, Object>() {
                                                                                        {
                                                                                            put("email",
                                                                                                    "root@asesor.com");
                                                                                            put("password",
                                                                                                    "rootAdmin");
                                                                                        }
                                                                                    }));
                                                                    put("cliente",
                                                                            new io.swagger.v3.oas.models.examples.Example()
                                                                                    .summary("Usuario Cliente")
                                                                                    .value(new java.util.LinkedHashMap<String, Object>() {
                                                                                        {
                                                                                            put("email",
                                                                                                    "andersone@xamples.com");
                                                                                            put("password", "Anderson");
                                                                                        }
                                                                                    }));
                                                                }
                                                            }))))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("JWT generado")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new Schema<>().$ref(
                                                                            "#/components/schemas/LoginResponse")))))));

            openApi.path("/api/v1/login", loginPath);

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
                            .addProperty("password", new StringSchema().format("password"))
                            .addProperty("baseSalary", new NumberSchema().format("double"))
                            .addProperty("roleId", new IntegerSchema().format("int64")))
                    .addSchemas("UserCreateDTOAdmin", new Schema<UserCreateDTO>()
                            .addProperty("name", new StringSchema())
                            .addProperty("lastName", new StringSchema())
                            .addProperty("dateOfBirth", new StringSchema().format("date"))
                            .addProperty("address", new StringSchema())
                            .addProperty("phone", new StringSchema())
                            .addProperty("email", new StringSchema())
                            .addProperty("password", new StringSchema().format("password"))
                            .addProperty("baseSalary", new NumberSchema().format("double"))
                            .addProperty("roleId", new IntegerSchema().format("int64"))
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
                            .addProperty("timestamp", new StringSchema().format("date-time")))
                    .addSchemas("LoginRequest", new Schema<>()
                            .addProperty("email", new StringSchema())
                            .addProperty("password", new StringSchema().format("password")))
                    .addSchemas("LoginResponse", new Schema<>()
                            .addProperty("token", new StringSchema()));
        };
    }
}
