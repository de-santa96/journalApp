package net.engineeringdigest.journalApp.scheduler;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.model.SentimentData;
import net.engineeringdigest.journalApp.repository.UserRepoImpl;
import net.engineeringdigest.journalApp.service.EmailService;
import net.engineeringdigest.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserSentimentScheduler {

    @Autowired
    private UserRepoImpl userRepo;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Scheduled(cron = "0 * * * * *")
//    @Scheduled(cron = "*/2 * * * * *")
    public void fetchUsersAndSendSaMail(){
        List<User> users = userRepo.getUsersForSentimentAnalysis();
        for(User user : users){
            List<JournalEntry> journalEntryList = user.getJournalEntryList();
            List<Sentiment> filteredJournalEntries = journalEntryList.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(30, ChronoUnit.DAYS)))
                    .map(x -> x.getSentiment())
                    .collect(Collectors.toList());


            Map<Sentiment, Integer> sentimentCount = new HashMap<>();
            for(Sentiment sentiment : filteredJournalEntries){
                if(sentiment != null)
                    sentimentCount.put(sentiment, sentimentCount.getOrDefault(sentiment, 0) + 1);
            }

            int mxCount = 0;
            Sentiment sentiment = null;
            for(Map.Entry<Sentiment, Integer> entry : sentimentCount.entrySet()){
                if(entry.getValue() > mxCount){
                    mxCount = entry.getValue();
                    sentiment = entry.getKey();
                }
            }


//            String join = String.join("", filteredJournalEntries);
//            String sentiment = sentimentAnalysisService.getSentiment(join);

            if(sentiment != null){
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment(sentiment.toString()).build();
                try{
                    kafkaTemplate.send("weekly-sentiment", sentimentData.getEmail(), sentimentData);
                }catch (Exception e){
                    emailService.sendEmail(sentimentData.getEmail(), "Sentiment", sentimentData.getSentiment().toString());
                }
            }
        }
    }
}
