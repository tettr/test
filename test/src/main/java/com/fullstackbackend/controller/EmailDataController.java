package com.fullstackbackend.controller;

import com.fullstackbackend.exception.NotFoundException;
import com.fullstackbackend.model.EmailData;
import com.fullstackbackend.DAO.EmailDataRepository;
import com.fullstackbackend.DAO.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.fullstackbackend.controller.UserController.idForUser;


@RestController
@CrossOrigin("http://localhost:3000")
public class EmailDataController {
    @Autowired
    private EmailDataRepository emailDataRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/emailData")
    EmailData newEmail(@RequestBody EmailData newEmail) {
        newEmail.setUser(userRepository.findById(idForUser).get());
        return emailDataRepository.save(newEmail);
    }

    @PutMapping("/userEmail/{id}")
    EmailData updateUserEmail(@RequestBody String email, @PathVariable Integer id) {
        return emailDataRepository.findByUser(id)
                .map(emailD -> {
                    emailD.setEmail(email);
                    return emailDataRepository.save(emailD);
                }).orElseThrow(() -> new NotFoundException(id));
    }
}
