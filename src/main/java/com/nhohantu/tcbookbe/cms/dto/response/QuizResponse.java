package com.nhohantu.tcbookbe.cms.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class QuizResponse {
    private String id;
    private String title;
    private String description;
    @JsonProperty("isActive")
    private boolean isActive;
    private List<QuestionResponse> questions;

    @Data
    @Builder
    public static class QuestionResponse {
        private String id;
        private String content;
        private List<OptionResponse> options;
    }

    @Data
    @Builder
    public static class OptionResponse {
        private String id;
        private String content;
        @JsonProperty("isCorrect")
        private boolean isCorrect;
    }
}
