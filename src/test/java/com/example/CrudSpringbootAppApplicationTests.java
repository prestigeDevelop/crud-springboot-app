package com.example;

import com.example.model.Actor;
import com.example.repository.ActorRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use the actual database, not H2
class CrudSpringbootAppApplicationTests {

    @Autowired
    private ActorRepository actorRepository;
    private Validator validator;
    @BeforeEach
    void setUp() {
        System.out.println("BeforeEach");
        // Clean up the database before each test if needed
        actorRepository.deleteAll();
        // Initialize the Validator
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testFindAll() {
        // Insert some test data
        Actor actor1 = new Actor("Actor One", "Actor One", new Timestamp(System.currentTimeMillis()));
        Actor actor2 = new Actor("Actor Two", "Actor Two", new Timestamp(System.currentTimeMillis()));
        actorRepository.save(actor1);
        actorRepository.save(actor2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "actorId"));
        List<Actor> all = actorRepository.findAll(pageable).getContent();

        assertNotNull(all);
        assertFalse(all.isEmpty());
        assertEquals(2, all.size());
        assertEquals("Actor One", all.get(0).getFirstName());
    }
    @Test
    void createActorWithNullFirstNameShouldFail() {
        Actor actorWithNullFirstName = new Actor(null, "Invalid", new Timestamp(System.currentTimeMillis()));
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            actorRepository.save(actorWithNullFirstName);
        });
        assertNotNull(exception, "Saving an actor with null first_name should throw a DataIntegrityViolationException");
        assertTrue(exception.getMessage().contains("must not be null"));
    }
    @Test
    void createActor() {
        Actor actorToSave = new Actor("Avi", "Gold", new Timestamp(System.currentTimeMillis()));
        Actor savedActor = actorRepository.save(actorToSave);

        assertNotNull(savedActor);
        assertEquals("Avi", savedActor.getFirstName());
        assertEquals("Gold", savedActor.getLastName());
    }
}