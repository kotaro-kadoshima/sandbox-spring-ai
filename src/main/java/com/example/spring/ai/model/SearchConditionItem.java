package com.example.spring.ai.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SearchConditionItem {
    private long columnCode;
    private String operator;
    private String text;
    private Date date;
}
