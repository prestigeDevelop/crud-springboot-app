package com.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.model.Actor;
import com.example.repository.ActorRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
@Service
@Slf4j
public class ActorService {
/*
 add log to the class and methods
 */
    @Autowired
    private ActorRepository actorRepository;

    public Page<Actor> findAll(int page , int size) {
        //Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "actorId").and(Sort.by(Sort.Direction.DESC, "lastUpdate")));
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "actorId"));
        log.info("Fetching all actors");
        return actorRepository.findAll(pageable);
    }

    public Optional<Actor> findById(Short id) {
        log.info("Fetching actor by id");
        return actorRepository.findById(id);
    }

    public Actor save(Actor actor) {
        log.info("Saving actor:{}",actor.toString());
        return actorRepository.save(actor);
    }

    public void deleteById(Short id) {
        log.info("Deleting actor by id:{}",id);
        actorRepository.deleteById(id);
    }

    public Optional<Actor> updateActor(Short id, Actor actorDetails) {
        log.info("Updating actor by id:{}",id);
        return actorRepository.findById(id).map(actor -> {
            actor.setFirstName(actorDetails.getFirstName());
            actor.setLastName(actorDetails.getLastName());
            actor.setLastUpdate(new Timestamp(System.currentTimeMillis()));
            return actorRepository.save(actor);
        });
    }
}