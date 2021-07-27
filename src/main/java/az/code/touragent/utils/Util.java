package az.code.touragent.utils;

import az.code.touragent.dtos.UserData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

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
}
