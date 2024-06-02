package com.coderdot.controllers;

import com.coderdot.entities.Customer;
import com.coderdot.repository.CustomerRepository;
import com.coderdot.repository.DiagRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import com.coderdot.entities.Diag;

import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/diag")
@AllArgsConstructor
public class DiagController {

    private final DiagRepository diagRepository;
    private final CustomerRepository customerRepository;


    @GetMapping
    public Page<Diag> getAll(Pageable pageable) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUsername = loggedInUser.getUsername();

        Customer customer = customerRepository.findByEmail(loggedInUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with email " + loggedInUsername));

        return diagRepository.findAllByCreatedByAndDeletedFalse(customer, pageable);
    }

    @PostMapping
    public ResponseEntity<Diag> createDiag(@RequestBody Diag newDiag) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUsername = loggedInUser.getUsername();

        Customer loggedInCustomer = customerRepository.findByEmail(loggedInUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with email " + loggedInUsername));

        newDiag.setCreatedBy(loggedInCustomer);

        diagRepository.save(newDiag);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDiag);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Diag> update(@PathVariable Long id, @RequestBody Diag updateDiag) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUsername = loggedInUser.getUsername();

        Customer loggedInCustomer = customerRepository.findByEmail(loggedInUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with email " + loggedInUsername));

        Diag diag = diagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Observation not found with id " + id));

        diag.setNom(updateDiag.getNom());
        diag.setAge(updateDiag.getAge());
        diag.setObservation(updateDiag.getObservation());
        diag.setUpdatedBy(loggedInCustomer);

        Diag updatedDiag = diagRepository.save(diag);
        return ResponseEntity.ok(updatedDiag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteObservation(@PathVariable Long id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUsername = loggedInUser.getUsername();

        Customer loggedInCustomer = customerRepository.findByEmail(loggedInUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with email " + loggedInUsername));

        Diag diag = diagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Observation not found with id " + id));

        diag.setDeleted(true);
        diag.setDeletedBy(loggedInCustomer);
        diagRepository.save(diag);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Diag> getDiag(@PathVariable Long id) {

        Diag diag = diagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Observation not found with id " + id));
        return ResponseEntity.ok(diag);
    }



}