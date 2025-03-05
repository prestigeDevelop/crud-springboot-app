package com.example;

import com.example.model.Actor;
import com.example.repository.ActorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)

public class ActorRepositoryMock {
    @Mock
    private ActorRepository actorRepository;

    @BeforeEach
    void setUp() {
        System.out.println("BeforeEach");
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "actorId"));
        List<Actor> mockActors = List.of(
                new Actor((short) 1, "Actor One", "Actor One", new Timestamp(System.currentTimeMillis())),
                new Actor((short) 2, "Actor Two", "Actor Two", new Timestamp(System.currentTimeMillis()))
        );
        Page<Actor> mockPage = new PageImpl<>(mockActors, pageable, mockActors.size());
        Mockito.when(actorRepository.findAll(pageable)).thenReturn(mockPage);

        // Test
        Page<Actor> all = actorRepository.findAll(pageable);
        assertNotNull(all);
        assertFalse(all.isEmpty());
        assertEquals(2, all.getContent().size());
        assertEquals("Actor One", all.getContent().get(0).getFirstName());
    }

    @Test
    void createActor() {
        Actor actorToSave = new Actor("Avi", "Gold", new Timestamp(System.currentTimeMillis()));
        Actor response = new Actor((short) 100, "Avi", "Gold", Timestamp.valueOf("2025-01-27 10:00:00"));
        Mockito.when(actorRepository.save(any(Actor.class))).thenReturn(response);

        Actor savedActor = actorRepository.save(actorToSave);
        assertEquals(response, savedActor);
        assertEquals("Avi", savedActor.getFirstName());
        assertEquals("Gold", savedActor.getLastName());
    }
}
