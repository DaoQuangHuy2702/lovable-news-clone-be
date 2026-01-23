package com.nhohantu.tcbookbe.cms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class QuizRequest {
    private String title;
    private String description;
    @JsonProperty("isActive")
    private boolean isActive;
    private List<QuestionRequest> questions;

    @Data
    public static class QuestionRequest {
        private String id; // Optional, for updates
        private String content;
        private List<OptionRequest> options;
    }

    @Data
    public static class OptionRequest {
        private String id; // Optional, for updates
        private String content;
        @JsonProperty("isCorrect")
        private boolean isCorrect;
    }
}
