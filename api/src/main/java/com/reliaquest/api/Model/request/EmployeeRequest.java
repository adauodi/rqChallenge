package com.reliaquest.api.Model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class EmployeeRequest {
    private String name;
    private Integer salary;
    private Integer age;
    private String title;
}
