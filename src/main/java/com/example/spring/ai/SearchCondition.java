package com.example.spring.ai;

import lombok.Data;

import java.util.List;

@Data
public class SearchCondition {
    private String targetObjectName;
    private List<SearchConditionItem> items;
    private boolean noteExists;
    private List<SearchCondition> relatedObjectConditions;
}
