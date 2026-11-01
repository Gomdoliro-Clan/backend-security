package com.acme.procure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProcureFlowApplication {
  public static void main(String[] args) {
    SpringApplication.run(ProcureFlowApplication.class, args);
  }
}
