package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;

    @Test
    @Disabled
    public void testAdd(){
        assertEquals(4, 2+1);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    @Disabled
    public void testSaveUser(User user){
        assertTrue(userService.saveUserWithEncodedPassword(user));
    }

    @Test
    public void testFindUserByUserName(){
        assertNotNull(userRepo.findByUserName("ram"));
    }

    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,2,5"
    })
    @Disabled
    public void test(int a, int b, int expected){
        assertEquals(expected, a + b, "failed for: " + a + " " + b);
    }
}
