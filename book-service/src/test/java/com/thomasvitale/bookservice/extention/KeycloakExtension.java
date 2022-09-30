package com.thomasvitale.bookservice.extention;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.extension.Extension;

public class KeycloakExtension implements Extension {

    public static final String REALM = "PolarBookshop";
    public static final String CLIENT_ID = "edge-service";
    public static final String CLIENT_SECRET = "polar-keycloak-secret";

    public static final KeycloakContainer KEYCLOAK_CONTAINER;

    static {
        KEYCLOAK_CONTAINER = new KeycloakContainer()
            .withAdminUsername("user")
            .withAdminPassword("password")
            .withRealmImportFile("keycloak-realm.json");

        KEYCLOAK_CONTAINER.start();

        // register spring boot property
        System.setProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri",
            String.format("%srealms/%s", KEYCLOAK_CONTAINER.getAuthServerUrl(), REALM));

    }
}
