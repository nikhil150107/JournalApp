package com.example.demo.cache;

import com.example.demo.entity.ConfigJournalAppEntity;
import com.example.demo.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AppCache {
    public enum keys{
        WEATHER_API
    }

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;
    
    public Map<String,String> appCache;

    @PostConstruct
    public void init(){
        appCache=new HashMap<>();
        List<ConfigJournalAppEntity> all = configJournalAppRepository.findAll();
        for(ConfigJournalAppEntity configJournalAppEntity : all){
            appCache.put(configJournalAppEntity.getKey(),configJournalAppEntity.getValue());
        }

    }

}
