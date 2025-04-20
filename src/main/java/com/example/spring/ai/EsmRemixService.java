package com.example.spring.ai;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class EsmRemixService {

    private final RestClient restClient;

    public EsmRemixService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://remix.softbrain.co.jp/")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }


}
