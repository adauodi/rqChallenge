package com.reliaquest.api.Model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DeleteEmployeeResponse {
    private Boolean data;
    private String status;
}
