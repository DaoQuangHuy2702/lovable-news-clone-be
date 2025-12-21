package com.nhohantu.tcbookbe.cms.dto.response;

import com.nhohantu.tcbookbe.common.model.enums.ArticleTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleSummaryResponse {
    private String id;
    private String title;
    private String excerpt;
    private CategorySummary category;
    private Long views;
    private ArticleTypeEnum type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategorySummary {
        private String id;
        private String name;
        private String colorCode;
    }
}
