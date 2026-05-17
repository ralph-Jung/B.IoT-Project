package ac.gachon.iot.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Authorization");

        Content errorContent = new Content().addMediaType("application/json",
                new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")));

        return new OpenAPI()
                .info(new Info()
                        .title("B.IoT API")
                        .description("IoT Backend API")
                        .version("v1.0.0"))
                .components(new Components()
                        .addResponses("400", new ApiResponse().description("잘못된 요청").content(errorContent))
                        .addResponses("403", new ApiResponse().description("권한 없음").content(errorContent))
                        .addResponses("404", new ApiResponse().description("데이터 없음").content(errorContent))
                        .addSecuritySchemes("bearerAuth", securityScheme));
    }
}
