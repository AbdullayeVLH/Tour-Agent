package az.code.touragent.security;

import az.code.touragent.dtos.LoginDto;
import az.code.touragent.dtos.LoginResponseDto;
import az.code.touragent.dtos.RegisterDto;
import az.code.touragent.dtos.RegisterResponseDto;
import az.code.touragent.exceptions.InvalidVerificationToken;
import az.code.touragent.exceptions.UserNotFound;
import az.code.touragent.models.User;
import az.code.touragent.models.Verification;
import az.code.touragent.repositories.UserRepository;
import az.code.touragent.repositories.VerificationRepository;
import az.code.touragent.utils.MailUtil;
import az.code.touragent.utils.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import javax.ws.rs.core.Response;
import java.util.*;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String clientId;
//    @Value("${keycloak.resource}")
    private final String role = "app-user";
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;
    @Value("${app.keycloak.username}")
    private String adminUsername;
    @Value("${app.keycloak.password}")
    private String adminPassword;
    @Value("${mail.auth.verification.subject}")
    private String verificationSubject;
    @Value("${mail.auth.verification.context}")
    private String verificationContext;
    @Value("${mail.auth.verification.url}")
    private String verificationUrl;

    MailUtil mailUtil;
    VerificationRepository verificationRepo;
    UserRepository userRepo;

    public SecurityServiceImpl(MailUtil mailUtil,
                               VerificationRepository verificationRepo,
                               UserRepository userRepo) {
        this.mailUtil = mailUtil;
        this.verificationRepo = verificationRepo;
        this.userRepo = userRepo;
    }

    @Override
    public AccessTokenResponse login(LoginDto user) throws LoginException {
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", clientSecret);
        clientCredentials.put("grant_type", "password");
        Configuration configuration =
                new Configuration(authServerUrl, realm, clientId, clientCredentials, null);
        AuthzClient authzClient = AuthzClient.create(configuration);
        try {
            AccessTokenResponse response =
                    authzClient.obtainAccessToken(user.getEmail(), user.getPassword());
            return response;
        } catch (HttpResponseException e) {
            throw new LoginException();
        }
    }

    @Override
    public RegisterResponseDto register(RegisterDto register) {
        Keycloak keycloak = getRealmCli();
        UserRepresentation user = createUser(register);
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        Response response = usersResource.create(user);
        if (response.getStatus() == 201) {
            UserResource userResource = changeTemporaryPassword(register, usersResource, response);
            RoleRepresentation realmRoleUser = realmResource.roles().get(role).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(realmRoleUser));
            sendVerificationEmail(register);
        }
        keycloak.tokenManager().getAccessToken();
        return RegisterResponseDto.builder().message(response.getStatusInfo().toString()).build();
    }

    @Override
    public String verify(String token, String username) {
        Keycloak keycloak = getRealmCli();
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        List<UserRepresentation> users = usersResource.search(username);
        UserRepresentation search;
        if (users.size() == 0)
            throw new UserNotFound();
        search = users.get(0);
        UserResource user = usersResource.get(search.getId());
        search.setEmailVerified(true);
        Optional<Verification> verf = verificationRepo.findById(token);
        if (verf.isEmpty())
            throw new InvalidVerificationToken();
        verificationRepo.delete(verf.get());
        user.update(search);
        return "User verified.";
    }

    private Keycloak getRealmCli() {
        return KeycloakBuilder.builder().serverUrl(authServerUrl)
                .grantType(OAuth2Constants.PASSWORD).realm("master").clientId("admin-cli")
                .username(adminUsername).password(adminPassword)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
    }

    private UserRepresentation createUser(RegisterDto register) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setEmail(register.getEmail());
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setAttributes(Collections.singletonMap("companyName", Collections.singletonList(register.getCompanyName())));
        user.setAttributes(Collections.singletonMap("voen", Collections.singletonList(register.getVoen())));
        return user;
    }

    private UserResource changeTemporaryPassword(RegisterDto register, UsersResource usersResource, Response response) {
        String userId = CreatedResponseUtil.getCreatedId(response);
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(register.getPassword());
        UserResource userResource = usersResource.get(userId);
        userResource.resetPassword(passwordCred);
        return userResource;
    }

    private void sendVerificationEmail(RegisterDto register) {
        String token = UUID.randomUUID().toString();
        userRepo.save(new User(register));
        verificationRepo.save(Verification.builder()
                .token(token)
                .userEmail(User.builder().email(register.getEmail()).build()).build());
        mailUtil.sendNotificationEmail(register.getEmail(), verificationSubject,
                verificationContext.formatted(verificationUrl.formatted(token, register.getEmail())));
    }


}

