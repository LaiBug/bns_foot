package org.sso;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.out.println(new Date().getTime());
        SpringApplication.run(Application.class, args);
    }
}
