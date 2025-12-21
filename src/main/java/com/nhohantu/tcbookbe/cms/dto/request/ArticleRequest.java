package com.nhohantu.tcbookbe.cms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String excerpt;

    @NotBlank(message = "Content is required")
    private String content;

    @NotBlank(message = "Category is required")
    private String categoryId;

    @NotBlank(message = "Thumbnail is required")
    private String thumbnail;

    @NotBlank(message = "Type is required")
    private String type;
}
