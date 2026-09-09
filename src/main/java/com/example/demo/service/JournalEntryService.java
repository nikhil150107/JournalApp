package com.example.demo.service;

import com.example.demo.entity.JournalEntry;
import com.example.demo.entity.User;
import com.example.demo.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);
    @Transactional
    public  void saveEntry(JournalEntry journalEntry, String userName){
       try{
           journalEntry.setDate(LocalDateTime.now());
           User user = userService.findByUserName(userName);
           JournalEntry saved = journalEntryRepository.save(journalEntry);
           user.getJournalEntries().add(saved);
//           user.setUserName(null);
           userService.saveEntry(user,true);
       } catch (Exception e) {

           throw new RuntimeException(e);
       }
    }
    public  void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }


    public List<JournalEntry> getAll(){

        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findbyId(ObjectId id){

        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deletebyid(ObjectId id, String userName){
        boolean removed=false;
        try{
            User user=userService.findByUserName(userName);
            removed=user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(removed){
                userService.saveEntry(user,true);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            log.error("Error",e);
            throw new RuntimeException("An error occured while deleting the entry.",e);
        }
        return removed;
    }
}
