package com.example.spring.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private final EsmRemixService remixService;

    public Application(EsmRemixService remixService) {
        this.remixService = remixService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {

        try {
            String res = remixService.getCalendar("2025-04-23", "2025-04-29");
            System.out.println(res);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ToolCallbackProvider weatherTools(EsmRemixService esmRemixService) {
        return MethodToolCallbackProvider.builder().toolObjects(esmRemixService).build();
    }
}
