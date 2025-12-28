package com.nhohantu.tcbookbe.common.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommuneResponse {
    private String id;
    private String name;
    private String provinceCode;
}
