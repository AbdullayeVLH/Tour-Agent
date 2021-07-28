package az.code.touragent.utils;

import az.code.touragent.dtos.UserData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

//@Configuration
public class Util {
    public static UserData convertToken(String auth) throws JsonProcessingException {
        UserData user = new UserData();
        String[] chunks = auth.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String data = new String(decoder.decode(chunks[1]));
        JsonNode payload = new ObjectMapper().readValue(data, JsonNode.class);
        if (!payload.get("email_verified").booleanValue())
            return null; //TODO: throw new EmailNotVerified();
        user.setEmail(payload.get("email").textValue());
        user.setFirstName(payload.get("name").textValue());
        user.setLastName(payload.get("lastName").textValue());
        user.setVoen(payload.get("email").textValue());
        user.setRegistrationTime(
                LocalDateTime.ofEpochSecond(payload.get("createdDate").longValue(),
                        0,
                        ZoneOffset.ofHours(4)));
        return user;
    }

//    @Bean
//    public Keycloak keycloakClient(){
//        return KeycloakBuilder.builder()
//                .serverUrl("http://localhost:8080/auth")
//                .realm("Tour")
//                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
//                .clientId("tour-agent")
//                .clientSecret("dac3b0ea-5d48-4307-8ea9-ba007fde0f7d")
//                .build();
//    }
}
