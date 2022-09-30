package com.thomasvitale.bookservice.util;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Locale;

import static com.thomasvitale.bookservice.extention.KeycloakExtension.CLIENT_ID;
import static com.thomasvitale.bookservice.extention.KeycloakExtension.CLIENT_SECRET;
import static com.thomasvitale.bookservice.extention.KeycloakExtension.KEYCLOAK_CONTAINER;
import static com.thomasvitale.bookservice.extention.KeycloakExtension.REALM;
import static org.keycloak.admin.client.CreatedResponseUtil.getCreatedId;

public final class KeycloakUtil {

    private KeycloakUtil() {
    }

    public static String getAuthToken(String user, String password) {

        return KeycloakBuilder.builder()
            .serverUrl(KEYCLOAK_CONTAINER.getAuthServerUrl())
            .grantType(OAuth2Constants.PASSWORD)
            .realm(REALM)
            .clientId(CLIENT_ID)
            .clientSecret(CLIENT_SECRET)
            .username(user)
            .password(password)
            .build()
            .tokenManager()
            .getAccessToken()
            .getToken();
    }

    public static UserRepresentation createUser(String username, String password) {
        String seed = RandomStringUtils.randomAlphabetic(10).toLowerCase(Locale.ROOT);

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setFirstName("firstName_ " + seed);
        user.setLastName("lastName_" + seed);
        user.setEmail(seed + username + "_email@gmail.com");
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setCredentials(List.of(passwordCred));

        Response response = KEYCLOAK_CONTAINER.getKeycloakAdminClient()
            .realm(REALM)
            .users()
            .create(user);

        return KEYCLOAK_CONTAINER.getKeycloakAdminClient()
            .realm(REALM)
            .users()
            .get(getCreatedId(response))
            .toRepresentation();
    }
}
