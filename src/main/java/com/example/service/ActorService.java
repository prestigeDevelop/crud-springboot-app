package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.model.Actor;
import com.example.repository.ActorRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    public List<Actor> findAll() {
        return actorRepository.findAll();
    }

    public Optional<Actor> findById(Short id) {
        return actorRepository.findById(id);
    }

    public Actor save(Actor actor) {
        return actorRepository.save(actor);
    }

    public void deleteById(Short id) {
        actorRepository.deleteById(id);
    }

    public Optional<Actor> updateActor(Short id, Actor actorDetails) {
        return actorRepository.findById(id).map(actor -> {
            actor.setFirstName(actorDetails.getFirstName());
            actor.setLastName(actorDetails.getLastName());
            actor.setLastUpdate(new Timestamp(System.currentTimeMillis()));
            return actorRepository.save(actor);
        });
    }
}