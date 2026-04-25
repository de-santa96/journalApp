package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepo.save(journalEntry);
    }

    @Transactional
    public void saveJournalForUser(JournalEntry journalEntry, String userName){
        try {
            User user = userService.findUserByUserName(userName);

            journalEntry.setDate(LocalDateTime.now());
            JournalEntry savedEntry = journalEntryRepo.save(journalEntry);

            user.getJournalEntryList().add(savedEntry);
            userService.saveEntry(user);
        } catch (Exception e) {
            logger.info("LOGGSSS");
            throw new RuntimeException("An error occurred while saving the entry", e);
        }
    }

    public List<JournalEntry> getAllJournalsOfUser(String userName){
        User user = userService.findUserByUserName(userName);
        return user.getJournalEntryList();
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> getById(ObjectId id){
        return journalEntryRepo.findById(id);
    }

    public void deleteById(ObjectId id){
        journalEntryRepo.deleteById(id);
    }

    @Transactional
    public boolean deleteUserJournalById(String userName, ObjectId journalId) {
        boolean removed = false;
        try {
            User user = userService.findUserByUserName(userName);
            removed = user.getJournalEntryList().removeIf(x -> x.getId().equals(journalId));
            if(removed){
                userService.saveEntry(user);
                journalEntryRepo.deleteById(journalId);
            }
        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("Exception while deleting journal entry of user", e);
        }

        return removed;
    }
}
