package com.example.spring.ai;

import com.example.spring.ai.model.ReqSearch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

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

    @Tool(description = "eセールスマネージャーのスケジュールを検索")
    public JsonNode searchSchedule(ReqSearch reqSearch) throws JsonProcessingException {
        String endPoint = "/rest/v1/entities/search";
        ResponseEntity<String> res = restClient.post().uri(endPoint).body(reqSearch).retrieve().toEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(res.getBody());
        return jsonNode;
    }
}
