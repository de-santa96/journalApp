package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    // controller -> service -> repository

    @Autowired
    JournalEntryService journalEntryService;

    @Autowired
    UserService userService;


    @GetMapping("/all")
    public List<JournalEntry> getAll(){
        return journalEntryService.getAll();
    }

    @GetMapping("/id/{journalId}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable ObjectId journalId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findUserByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntryList().stream().filter(x -> x.getId().equals(journalId)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.getById(journalId);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<JournalEntry> all = journalEntryService.getAllJournalsOfUser(userName);
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @PostMapping("/user")
    public ResponseEntity<?> createJournalEntry(@RequestBody JournalEntry journalEntry) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.saveJournalForUser(journalEntry, userName);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/id/{journalId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId journalId) {
        journalEntryService.deleteById(journalId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/user/{journalId}")
    public ResponseEntity<?> deleteJournalEntryOfUserByJournalId(@PathVariable ObjectId journalId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = journalEntryService.deleteUserJournalById(userName, journalId);

        if(removed)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PutMapping("/id/{journalId}")
    public ResponseEntity<?> updateJournalEntryById(@PathVariable ObjectId journalId, @RequestBody JournalEntry newJournalEntry) {
        JournalEntry oldEntry = journalEntryService.getById(journalId).orElse(null);
        if(oldEntry != null){
            oldEntry.setTitle(newJournalEntry.getTitle() != null && !newJournalEntry.getTitle().equals("") ? newJournalEntry.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(newJournalEntry.getContent() != null && !newJournalEntry.getContent().equals("") ? newJournalEntry.getContent() : oldEntry.getContent());
            journalEntryService.saveEntry(oldEntry);

            return new ResponseEntity<>(oldEntry, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/user/{journalId}")
    public ResponseEntity<?> updateJournalEntryOfUserById(@PathVariable ObjectId journalId,
                                                          @RequestBody JournalEntry newJournalEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.findUserByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntryList().stream().filter(x -> x.getId().equals(journalId)).collect(Collectors.toList());

        if(!collect.isEmpty()){
            JournalEntry oldEntry = journalEntryService.getById(journalId).orElse(null);
            oldEntry.setTitle(newJournalEntry.getTitle() != null && !newJournalEntry.getTitle().equals("") ? newJournalEntry.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(newJournalEntry.getContent() != null && !newJournalEntry.getContent().equals("") ? newJournalEntry.getContent() : oldEntry.getContent());
            journalEntryService.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
