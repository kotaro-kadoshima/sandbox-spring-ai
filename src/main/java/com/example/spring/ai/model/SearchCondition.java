package com.example.spring.ai.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchCondition {
    private String targetObjectName;
    private List<SearchConditionItem> items;
    private boolean notExists = false;
    private List<SearchCondition> relatedObjectConditions;
}
