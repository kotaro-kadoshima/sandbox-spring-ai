package com.example.spring.ai;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

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
        remixService.getEntitiesNames();
        // スケジュールの検索処理
        SearchCondition condition = SearchCondition.builder()
                .targetObjectName("schedule")
                .items(List.of(SearchConditionItem.builder()
                        .columnCode(1305)
                        .operator("partly")
                        .text("RESTテスト")
                        .build()))
                .build();
        ReqSearch reqSearch = ReqSearch.builder().searchCondition(condition).columnCodes(List.of(1301, 1302, 1305)).build();

        remixService.searchSchedule(reqSearch);
    }

    @Bean
    public ToolCallbackProvider weatherTools(EsmRemixService esmRemixService) {
        return MethodToolCallbackProvider.builder().toolObjects(esmRemixService).build();
    }
}
