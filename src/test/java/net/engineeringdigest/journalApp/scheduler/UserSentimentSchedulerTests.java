package net.engineeringdigest.journalApp.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserSentimentSchedulerTests {

    @Autowired
    private UserSentimentScheduler scheduler;

    @Test
    public void testFetchUsersAndSendSaMail(){
        scheduler.fetchUsersAndSendSaMail();
    }
}
