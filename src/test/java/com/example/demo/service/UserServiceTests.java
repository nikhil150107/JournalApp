package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private UserService userService;

    //    @BeforeEach
    //Before each(for loop  type method) testcase this test should run

    //@BeforeAll Before all each testcase this test should run

    //same for @Afterall and @Aftereach
    @Disabled
    @Test
    public void testFindByUserName(){

        assertEquals(4,2+2);
        assertNotNull(userRepository.findByUserName("ram"));
        User user=userRepository.findByUserName("ram");
        assertTrue(user.getJournalEntries().isEmpty());
        assertTrue(5>3);
    }
    @ParameterizedTest
//    @CsvSource({
//            "ram",
//            "nikhil",
//            "shyam"
//    })

//    @ValueSource(strings = {
//            "ram",
//            "nikhil",
//            "shyam"
//    })

    @ArgumentsSource(UserArgumentsProvider.class)
    public void testFindByUserNameIsNull(User user){
//        assertNotNull(userRepository.findByUserName(name));
        assertTrue(userService.saveEntry(user,false));
    }


    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,10,12",
            "3,3,6"
    })
    public void test(int a,int b,int expected){
        assertEquals(expected,a+b);
    }

}
