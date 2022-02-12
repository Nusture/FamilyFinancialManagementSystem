package com.nusture;

import com.nusture.service.impl.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.text.ParseException;

@EnableAsync
@SpringBootApplication
public class FamilyFinancialManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamilyFinancialManagementSystemApplication.class, args);
    }

}
