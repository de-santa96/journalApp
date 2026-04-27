package net.engineeringdigest.journalApp.service;

import org.springframework.stereotype.Service;

@Service
public class SentimentAnalysisService {

    // dummy sentiment analysis class
    public String getSentiment(String text){
        return "HAPPY";
    }
}
