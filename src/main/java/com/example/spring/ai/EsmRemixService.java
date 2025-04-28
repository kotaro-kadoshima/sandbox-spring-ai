package com.example.spring.ai;

import com.example.spring.ai.model.ReqSearch;
import com.example.spring.ai.model.SearchCondition;
import com.example.spring.ai.model.SearchConditionItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class EsmRemixService {

    private final RestClient restClient;

    public EsmRemixService(@Value("${remix.base-url}") String baseUrl,
                           @Value("${remix.admin-token}") String adminToken,
                           @Value("${remix.context_name}") String contextName
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl + "/" + contextName)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("X-Auth-API-Token", adminToken)
                .build();
    }

    public void getEntitiesNames() {
        String endPoint = "/rest/v1/entities/names";

        ResponseEntity<String> res = restClient.get().uri(endPoint).retrieve().toEntity(String.class);
        System.out.println(res);

    }

    public JsonNode searchSchedule(ReqSearch reqSearch) throws JsonProcessingException {
        String endPoint = "/rest/v1/entities/search";
        ResponseEntity<String> res = restClient.post().uri(endPoint).body(reqSearch).retrieve().toEntity(String.class);
//        return res.getBody();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(res.getBody());
        return jsonNode;
    }

    @Tool(description = "Get the calendar events from e-sales")
    public String getCalendar(
            @ToolParam(description = "Start date for the calendar events yyyy-MM-dd") String start
            , @ToolParam(description = "End date for the calendar events yyyy-MM-dd") String end
    ) throws JsonProcessingException {
        Date startDate = convertStringToDate(start);
        Date endDate = convertStringToDate(end);

        // スケジュールの検索処理
        SearchCondition condition = SearchCondition.builder()
                .targetObjectName("schedule")
                .items(List.of(
                                SearchConditionItem.builder()
                                        .columnCode(1303)
                                        .operator(">=")
                                        .date(startDate)
                                        .build(),
                                SearchConditionItem.builder()
                                        .columnCode(1304)
                                        .operator("<")
                                        .date(endDate)
                                        .build()
                        )
                )
                .build();
        ReqSearch reqSearch = ReqSearch.builder().searchCondition(condition).columnCodes(List.of(1301, 1302, 1303, 1304, 1305)).build();
        JsonNode jsonNode = searchSchedule(reqSearch);
        return jsonNode.toString();
    }

    // 追加：String(yyyy-MM-dd) → java.util.Date変換用メソッド
    private Date convertStringToDate(String dateStr) {
        LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
