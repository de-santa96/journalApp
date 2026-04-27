package net.engineeringdigest.journalApp.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepoImplTests {

    @Autowired
    private UserRepoImpl userRepoImpl;

    @Test
    public void testGetUsersForSentimentAnalysis(){
        Assertions.assertNotNull(userRepoImpl.getUsersForSentimentAnalysis());
    }
}
