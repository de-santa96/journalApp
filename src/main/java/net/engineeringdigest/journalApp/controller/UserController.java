package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepo;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    // controller -> service -> repository

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<User> all = userService.getAll();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @GetMapping("/id/{myId}")
//    public ResponseEntity<?> getJournalEntryById(@PathVariable ObjectId myId) {
//        Optional<JournalEntry> journalEntry = journalEntryService.getById(myId);
//        if(journalEntry.isPresent()){
//            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
//        }
//
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }



    @DeleteMapping
    public ResponseEntity<?> deleteJournalEntryById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepo.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<?> updateJournalEntryById(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User userInDB = userService.findUserByUserName(userName);
        if(userInDB != null){
            userInDB.setUserName(user.getUserName() != null && !user.getUserName().equals("") ? user.getUserName() : userInDB.getUserName());
            userInDB.setPassword(user.getPassword() != null && !user.getPassword().equals("") ? user.getPassword() : userInDB.getPassword());

            userService.saveUserWithEncodedPassword(userInDB);

            return new ResponseEntity<>(userInDB, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
