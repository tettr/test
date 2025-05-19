package com.fullstackbackend.controller;

import com.fullstackbackend.exception.NotFoundException;
import com.fullstackbackend.model.PhoneData;
import com.fullstackbackend.DAO.PhoneDataRepository;
import com.fullstackbackend.DAO.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.fullstackbackend.controller.UserController.idForUser;


@RestController
@CrossOrigin("http://localhost:3000")
public class PhoneDataController {
    @Autowired
    private PhoneDataRepository phoneDataRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/phoneData")
    PhoneData newPhone(@RequestBody PhoneData newPhone) {
        newPhone.setUser(userRepository.findById(idForUser).get());
        return phoneDataRepository.save(newPhone);
    }

    @PutMapping("/userEmail/{id}")
    PhoneData updateUserPhone(@RequestBody String phone, @PathVariable Integer id) {
        return phoneDataRepository.findByUser(id)
                .map(phoneD -> {
                    phoneD.setPhone(phone);
                    return phoneDataRepository.save(phoneD);
                }).orElseThrow(() -> new NotFoundException(id));
    }
}
