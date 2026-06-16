package cl.duoc.api_compras.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI OpenApiConfig(){
        return new OpenAPI()
                .info(new Info()
                        .title("API de gestion de las ordenes de compra")
                        .description("Ahi se gestionara todas las ordenes de compra")
                        .version("1.0")
                        .contact(new Contact()
                                .name("GRUPO1")
                                .email("jrdarius.jr@duocuc.cl"))
                        .license(new License()
                                .name("solo para uso academico"))
                );
    }

}
