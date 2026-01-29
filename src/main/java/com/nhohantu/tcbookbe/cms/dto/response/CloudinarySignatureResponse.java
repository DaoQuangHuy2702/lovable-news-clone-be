package com.nhohantu.tcbookbe.cms.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CloudinarySignatureResponse {
    private String signature;
    private long timestamp;
    private String apiKey;
    private String cloudName;
}
