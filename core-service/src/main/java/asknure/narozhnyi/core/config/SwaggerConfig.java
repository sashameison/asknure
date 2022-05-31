package asknure.narozhnyi.core.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.AuthorizationCodeGrant;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

@Configuration
@Log4j2
public class SwaggerConfig {

  @Value("${spring.security.oauth2.client.provider.google.token-uri}")
  private String token;
  @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
  private String accessTokenUri;
  @Value("${spring.security.oauth2.client.registration.google.client-id}")
  private String clientId;
  @Value("${spring.security.oauth2.client.registration.google.client-secret}")
  private String clientSecret;

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .securitySchemes(List.of(securitySchema()))
        .securityContexts(List.of(securityContext()));
  }

  private SecurityScheme securitySchema() {
    var grantTypes = new ArrayList<GrantType>();
    var grantType = new AuthorizationCodeGrant(
        new TokenRequestEndpoint(accessTokenUri, clientId, clientSecret),
        new TokenEndpoint(token, "token")
    );
    grantTypes.add(grantType);
    return new OAuth("spring_oauth",
        List.of(authScopes()),
        grantTypes,
        List.of(new StringVendorExtension("x-tokenName", "id_token"))
    );
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(List.of(defaultAuth()))
        .build();
  }

  private SecurityReference defaultAuth() {
    return new SecurityReference("spring_oauth", authScopes());
  }

  private AuthorizationScope[] authScopes() {
    return new AuthorizationScope[] {
        new AuthorizationScope("profile email", "profile email")
    };
  }

  @Bean
  public SecurityConfiguration securityInfo() {
    return SecurityConfigurationBuilder.builder()
        .clientId(clientId)
        .clientSecret(clientSecret)
        .additionalQueryStringParams(Map.of("access_type", "offline"))
        .build();
  }
}
