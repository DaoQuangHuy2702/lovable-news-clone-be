package com.nhohantu.tcbookbe.cms.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CmsCreateProductRequest {
    private String name;
    private String desciption;
    private BigDecimal price;
    private Integer quantity;
    private Boolean active;
    private String mainImageUrl;
    private List<Long> categoryIds;
}