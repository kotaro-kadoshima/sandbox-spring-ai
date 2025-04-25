package com.example.spring.ai;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchConditionItem {
    private long columnCode;
    private String operator;
    private String text;
}
