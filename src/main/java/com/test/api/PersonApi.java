package com.test.api;

import com.test.entity.Person;
import com.test.request.PersonRequest;
import com.test.service.impl.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/persons")
public class PersonApi {

    private final PersonService personService;

    @Autowired
    public PersonApi(PersonService personService) {
        this.personService = personService;
    }

    @Operation(summary = "Save Person to in-memory database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stored the person in database.",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Person.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid attribute supplied in request",
                    content = @Content)})
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> save(@RequestBody @Valid PersonRequest personRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.save(personRequest));
    }

    @Operation(summary = "Find all Person from in-memory database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the list of all person",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Person.class))})})
    @GetMapping
    public ResponseEntity<List<Person>> findAllPerson(@RequestParam(defaultValue = "0") Integer pageNo,
                                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                                      @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(personService.findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy))));
    }

    @Operation(summary = "Search all Person that matches first or last name from in-memory database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the list of all person based on search criteria.",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Person.class))})})
    @GetMapping("/search")
    public ResponseEntity<List<Person>> findPerson(@Parameter(description = "FirstName of person to be searched")
                                                   @RequestParam(value = "firstName", required = false) String firstName,
                                                   @Parameter(description = "LastName of person to be searched")
                                                   @RequestParam(value = "lastName", required = false) String lastName) {
        return ResponseEntity.ok(personService.findAllByName(firstName, lastName));
    }

    @Operation(summary = "Find a Person using id from in-memory database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the person",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Person.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Person not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<Person> findPersonById(@Parameter(description = "id of person to be searched")
                                                 @PathVariable("id") Long id) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the person",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HttpStatus.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Person not found",
                    content = @Content)})
    @Operation(summary = "Delete a Person using id from in-memory database.")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "id of person to be deleted")
                                             @PathVariable("id") Long id) {
        personService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the person",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HttpStatus.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id and/or request supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Person not found to update",
                    content = @Content)})
    @Operation(summary = "Update a person information")
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@Parameter(description = "id of person to be updated.") @PathVariable("id") Long id,
                                             @RequestBody @Valid PersonRequest person) {
        personService.update(id, person);
        return ResponseEntity.ok().build();
    }

}
