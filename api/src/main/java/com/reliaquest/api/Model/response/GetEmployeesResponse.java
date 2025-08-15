package com.reliaquest.api.Model.response;

import com.reliaquest.api.Model.Employee;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class GetEmployeesResponse {
    private List<Employee> data;
}
