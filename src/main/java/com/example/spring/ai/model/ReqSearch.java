package com.example.spring.ai.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReqSearch {
    private SearchCondition searchCondition;
    private List<Integer> columnCodes;
    // TODO: sortKeysとfromIndexを実装
}
