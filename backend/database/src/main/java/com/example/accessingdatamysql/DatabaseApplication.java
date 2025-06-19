// package com.example.accessingdatamysql;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.boot.autoconfigure.domain.EntityScan;

// @EntityScan(basePackages = "com.example.accessingdatamysql")
// @SpringBootApplication(scanBasePackages = "com.example.accessingdatamysql")
// public class DatabaseApplication {
//     public static void main(String[] args) {
//         SpringApplication.run(DatabaseApplication.class, args);
//     }
// }

package com.example.accessingdatamysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@EntityScan(basePackages = "com.example.accessingdatamysql")
@ComponentScan(basePackages = "com.example.accessingdatamysql")
@SpringBootApplication

public class DatabaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(DatabaseApplication.class, args);
    }
}


