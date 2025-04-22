package com.example.spring.ai;

import lombok.Data;

@Data
public class SearchConditionItem {
    private long columnCode;
    private String operator;
    private String text;
}
