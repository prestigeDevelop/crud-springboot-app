package com.example;

import com.example.model.Actor;
import com.example.repository.ActorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CrudSpringbootAppApplicationTests {

    @Mock
    private ActorRepository actorRepository;


    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "actorId"));
        List<Actor> mockActors = List.of(
                new Actor((short) 1, "Actor One","Actor One", new Timestamp(System.currentTimeMillis())),
                new Actor((short) 1, "Actor two","Actor two", new Timestamp(System.currentTimeMillis()))
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
}