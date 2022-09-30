package com.thomasvitale.bookservice;

import com.thomasvitale.bookservice.extention.KeycloakExtension;
import com.thomasvitale.bookservice.util.KeycloakUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(KeycloakExtension.class)
class BookServiceApplicationTests {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void whenGetRequestsThenReturnBooks() {

        KeycloakUtil.createUser("user1", "pass");

        webTestClient
            .get()
            .uri("books")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + KeycloakUtil.getAuthToken("user1", "pass"))
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Book.class)
            .consumeWith(System.out::println)
            .contains(
                new Book("Harry Potter"),
                new Book("His Dark Materials"),
                new Book("The Hobbit"),
                new Book("The Lord of the Rings")
            );
    }

}
