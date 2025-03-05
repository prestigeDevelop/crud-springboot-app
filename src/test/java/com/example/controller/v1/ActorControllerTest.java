package com.example.controller.v1;

import com.example.model.Actor;
import com.example.repository.ActorRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ActorControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ActorRepository actorRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/actors";
        actorRepository.deleteAll(); // Clear the database before each test
    }

    @Test
    void testGetAllActors() throws JsonProcessingException {
        // Prepare test data programmatically
        Actor actor1 = new Actor("Actor One", "Actor One", new Timestamp(System.currentTimeMillis()));
        Actor actor2 = new Actor("Actor Two", "Actor Two", new Timestamp(System.currentTimeMillis()));
        actorRepository.saveAll(Arrays.asList(actor1, actor2));

        // Perform GET request and get raw JSON response
        ResponseEntity<String> responseString = restTemplate.getForEntity(
                baseUrl + "?page=0&size=10",
                String.class
        );
        assertEquals(HttpStatus.OK, responseString.getStatusCode());
        System.out.println("Raw Response: " + responseString.getBody()); // Debug the response

        // Parse JSON to extract the 'content' array
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseString.getBody());
        JsonNode contentNode = rootNode.path("content");
        Actor[] actors = objectMapper.treeToValue(contentNode, Actor[].class);
        List<Actor> actorList = Arrays.asList(actors);

        // Assertions
        assertNotNull(actorList);
        assertEquals(2, actorList.size());
        assertEquals("Actor One", actorList.get(0).getFirstName());
    }

    @Test
    void testGetActorById() {
        // Prepare test data
        Actor actor = new Actor("Avi", "Gold", new Timestamp(System.currentTimeMillis()));
        actorRepository.save(actor);
        Short actorId = actor.getActorId();

        // Perform GET request
        ResponseEntity<Actor> response = restTemplate.getForEntity(baseUrl + "/{id}", Actor.class, actorId);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Avi", response.getBody().getFirstName());
        assertEquals("Gold", response.getBody().getLastName());
    }

    @Test
    void testGetActorByIdNotFound() {
        // Perform GET request with non-existent ID
        ResponseEntity<Actor> response = restTemplate.getForEntity(baseUrl + "/999", Actor.class);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCreateActor() {
        // Prepare request body
        Actor actor = new Actor("NewActor", "NewLastName", new Timestamp(System.currentTimeMillis()));
        HttpEntity<Actor> request = new HttpEntity<>(actor);

        // Perform POST request
        ResponseEntity<Actor> response = restTemplate.postForEntity(baseUrl, request, Actor.class);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("NewActor", response.getBody().getFirstName());
        assertTrue(response.getHeaders().containsKey("Location")); // Check for Location header
    }

    @Test
    void testCreateActorWithNullFirstName() {
        // Prepare request body with invalid data
        Actor actor = new Actor(null, "InvalidLastName", new Timestamp(System.currentTimeMillis()));
        HttpEntity<Actor> request = new HttpEntity<>(actor);

        // Perform POST request
        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl, request, Map.class);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"), "Response should contain an error key");
        assertTrue(response.getBody().get("message").toString().contains("firstName"), "Error should mention firstName");
    }

    @Test
    void testUpdateActor() {
        // Prepare test data
        Actor actor = new Actor("OldActor", "OldLastName", new Timestamp(System.currentTimeMillis()));
        actorRepository.save(actor);
        Short actorId = actor.getActorId();
        Actor updatedActor = new Actor("UpdatedActor", "UpdatedLastName", new Timestamp(System.currentTimeMillis()));

        // Perform PUT request
        HttpEntity<Actor> request = new HttpEntity<>(updatedActor);
        ResponseEntity<Actor> response = restTemplate.exchange(
                baseUrl + "/{id}", HttpMethod.PUT, request, Actor.class, actorId);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UpdatedActor", response.getBody().getFirstName());
        assertEquals("UpdatedLastName", response.getBody().getLastName());
    }

    @Test
    void testUpdateActorNotFound() {
        // Perform PUT request with non-existent ID
        Actor updatedActor = new Actor("UpdatedActor", "UpdatedLastName", new Timestamp(System.currentTimeMillis()));
        HttpEntity<Actor> request = new HttpEntity<>(updatedActor);
        ResponseEntity<Actor> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.PUT, request, Actor.class);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteActor() {
        // Prepare test data
        Actor actor = new Actor("ToDelete", "DeleteLastName", new Timestamp(System.currentTimeMillis()));
        actorRepository.save(actor);
        Short actorId = actor.getActorId();

        // Perform DELETE request
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{id}", HttpMethod.DELETE, null, Void.class, actorId);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        assertFalse(actorRepository.findById(actorId).isPresent());
    }

    @Test
    void testDeleteActorNotFound() {
        // Perform DELETE request with non-existent ID
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.DELETE, null, Void.class);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}