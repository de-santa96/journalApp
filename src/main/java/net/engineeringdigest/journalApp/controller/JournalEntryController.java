//package net.engineeringdigest.journalApp.controller;
//
//import net.engineeringdigest.journalApp.entity.JournalEntry;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/_journal")
//public class JournalEntryController {
//
//    Map<Long, JournalEntry> journalEntries = new HashMap<>();
//
//    @GetMapping
//    public List<JournalEntry> getAll(){
//        return new ArrayList<>(journalEntries.values());
//    }
//
//    @GetMapping("/id/{myId}")
//    public JournalEntry getJournalEntryById(@PathVariable long myId){
//        return journalEntries.get(myId);
//    }
//
//    @PostMapping
//    public boolean createJournalEntry(@RequestBody JournalEntry journalEntry){
//        journalEntries.put(journalEntry.getId(), journalEntry);
//        return true;
//    }
//
//    @DeleteMapping("/id/{myId}")
//    public JournalEntry deleteJournalEntryById(@PathVariable long myId){
//        return journalEntries.remove(myId);
//    }
//
//    @PutMapping("/id/{myId}")
//    public JournalEntry updateJournalEntryById(@PathVariable long myId, @RequestBody JournalEntry journalEntry){
//        return journalEntries.put(myId, journalEntry);
//    }
//}
