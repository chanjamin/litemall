package org.linlinjava.litemall.wx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("org.linlinjava.litemall.db.dao")
@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"org.linlinjava.litemall.db",
        "org.linlinjava.litemall.core", "org.linlinjava.litemall.wx"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}