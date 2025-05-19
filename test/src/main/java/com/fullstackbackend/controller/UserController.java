package com.fullstackbackend.controller;

import com.fullstackbackend.exception.NotFoundException;
import com.fullstackbackend.model.Account;
import com.fullstackbackend.model.EmailData;
import com.fullstackbackend.model.PhoneData;
import com.fullstackbackend.model.User;
import com.fullstackbackend.DAO.AccountRepository;
import com.fullstackbackend.DAO.EmailDataRepository;
import com.fullstackbackend.DAO.PhoneDataRepository;
import com.fullstackbackend.DAO.UserRepository;
import com.fullstackbackend.serviceJwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/authorization")
@CrossOrigin("http://localhost:3000")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailDataRepository emailDataRepository;
    @Autowired
    private PhoneDataRepository phoneDataRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private JwtUtil jwtUtil;
    public static int idForUser = -1;

    @PostMapping("/user")
    void newUser(@RequestBody User newUser, Account account, EmailData newEmail, PhoneData newPhone) {
        userRepository.save(newUser);

        account.setUser(userRepository.findById(idForUser).orElseThrow(() -> new NotFoundException(idForUser)));
        accountRepository.save(account);

        newEmail.setUser(userRepository.findById(idForUser).get());
        emailDataRepository.save(newEmail);

        newPhone.setUser(userRepository.findById(idForUser).get());
        phoneDataRepository.save(newPhone);
    }

    @GetMapping("/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    User getUserById(@PathVariable Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    @PutMapping("/userEmail/{id}")
    EmailData updateUserEmail(@RequestBody String email, @PathVariable Integer id) {
        return emailDataRepository.findByUser(id)
                .map(emailD -> {
                    emailD.setEmail(email);
                    return emailDataRepository.save(emailD);
                }).orElseThrow(() -> new NotFoundException(id));
    }

    @PutMapping("/userPhone/{id}")
    PhoneData updateUserPhone(@RequestBody String phone, @PathVariable Integer id) {
        return phoneDataRepository.findByUser(id)
                .map(user -> {
                    user.setPhone(phone);
                    return phoneDataRepository.save(user);
                }).orElseThrow(() -> new NotFoundException(id));
    }

    @DeleteMapping("/user/{id}")
    String deleteUser(@PathVariable Integer id){
        if(!userRepository.existsById(id)){
            throw new NotFoundException(id);
        }
        userRepository.deleteById(id);
        return  "User with id " + id + " has been deleted success.";
    }

    @PostMapping("/login")
    public String login(@RequestParam String password, String email) {
        if (emailDataRepository.findByEmail(email).equals(password)) {
            idForUser = emailDataRepository.findByEmail(email).get().getId();
            return jwtUtil.generateToken(email);
        }
        throw new RuntimeException("Invalid credentials");
    }
}
