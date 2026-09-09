package com.example.demo.controller;

import com.example.demo.entity.JournalEntry;
import com.example.demo.entity.User;
import com.example.demo.service.JournalEntryService;
import com.example.demo.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/getall")
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User user = userService.findByUserName(userName);

        List<JournalEntry> all= user.getJournalEntries();
        if(all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/add")
    public ResponseEntity<JournalEntry> createJournal(@RequestBody JournalEntry newEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName=authentication.getName();
            journalEntryService.saveEntry(newEntry,userName);
            return new ResponseEntity<>(newEntry, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getbyid/{journalid}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId journalid){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(journalid)).collect(Collectors.toList());

        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry=journalEntryService.findbyId(journalid);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{journalid}")
    public ResponseEntity<?> DeleteJournalEntryById(@PathVariable ObjectId journalid){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        boolean removed=journalEntryService.deletebyid(journalid,userName);
        if(removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{journalid}")
    public ResponseEntity<?> UpdateJournalEntryById(@PathVariable ObjectId journalid,
                                                    @RequestBody JournalEntry newEntry)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);

        boolean belongsToUser = user.getJournalEntries().stream()
                .anyMatch(entry -> entry.getId().equals(journalid));

        if (!belongsToUser) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        JournalEntry old = journalEntryService.findbyId(journalid).orElse(null);
        if (old == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (newEntry.getTitle() != null && !newEntry.getTitle().isBlank()) {
            old.setTitle(newEntry.getTitle());
        }
        if (newEntry.getContent() != null && !newEntry.getContent().isBlank()) {
            old.setContent(newEntry.getContent());
        }

        journalEntryService.saveEntry(old);
        return new ResponseEntity<>(old, HttpStatus.OK);
    }

//    @PatchMapping("/partiallyupdate/{journalid}")
//    public JournalEntry  PartiallyUpdateJournalEntryById(@PathVariable ObjectId journalid,@RequestBody JournalEntry partiallyupdateEntry){
//        return null;
//    }

}
