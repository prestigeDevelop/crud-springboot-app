package com.example.controller.v1;


import com.example.model.Actor;
import com.example.service.ActorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
@Slf4j
@Tag(name = "Actors", description = "Endpoints for managing actors(V1)")
@RestController
@RequestMapping("/api/v1/actors")
public class ActorController {

    @Autowired
    private ActorService actorService;

    @Operation(summary = "Get all actors", description = "Retrieve a list of all actors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all actors",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Actor.class))},
                    links = {
                            @Link(
                                    name = "getActorById",
                                    operationId = "getActorById",
                                    parameters = {
                                            @LinkParameter(name = "id", expression = "$response.body#/id")
                                    }
                            )
                    })
    })
    @GetMapping
    public Page<Actor> getAllActors(@RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        return actorService.findAll(page,size);
    }

    @Operation(summary = "Get actor by ID", description = "Retrieve an actor by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actor found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Actor.class))}),
            @ApiResponse(responseCode = "404", description = "Actor not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable(value = "id") Short actorId) {
        log.info(" haha haha haaha ha ha Fetching actor by id:{}",actorId);
        return actorService.findById(actorId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new actor", description = "Add a new actor to the database")
    @PostMapping
    public ResponseEntity<Actor> createActor(@Valid @RequestBody Actor actor) {
        Actor savedActor = actorService.save(actor);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedActor.getActorId())
                .toUri();
        return ResponseEntity.created(location).body(savedActor);
    }

    @Operation(summary = "Update an actor", description = "Update an existing actor's details")
    @PutMapping("/{id}")
    public ResponseEntity<Actor> updateActor(@PathVariable(value = "id") Short actorId,@Valid @RequestBody Actor actorDetails) {
        return actorService.updateActor(actorId, actorDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete an actor", description = "Remove an actor by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Actor successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Actor not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable(value = "id") Short actorId) {
        try{
            actorService.deleteById(actorId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}