package com.reliaquest.api.service;

import com.reliaquest.api.Model.Employee;
import com.reliaquest.api.Model.request.DeleteEmployeeRequest;
import com.reliaquest.api.Model.request.EmployeeRequest;
import com.reliaquest.api.Model.response.DeleteEmployeeResponse;
import com.reliaquest.api.Model.response.GetEmployeeResponse;
import com.reliaquest.api.Model.response.GetEmployeesResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class EmployeeService {
    private final RestTemplate restTemplate;

    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Employee> getEmployees() {
        String url = "http://localhost:8112/api/v1/employee";
        try {
            GetEmployeesResponse response = restTemplate.getForObject(url, GetEmployeesResponse.class);
            return response != null ? response.getData() : List.of();
        } catch (RestClientException | NullPointerException e) {
            log.error("Error in getEmployees: {}", e.getMessage());
            return List.of();
        }
    }

    public Employee getEmployeesById(String id) {
        String url = "http://localhost:8112/api/v1/employee/" + id;
        try {
            GetEmployeeResponse response = restTemplate.getForObject(url, GetEmployeeResponse.class);
            return response != null ? response.getData() : null;
        } catch (RestClientException | NullPointerException e) {
            log.error("Error in getEmployeesById: {}", e.getMessage());
            return null;
        }
    }

    public GetEmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        String url = "http://localhost:8112/api/v1/employee";
        try {
            return restTemplate.postForObject(url, employeeRequest, GetEmployeeResponse.class);
        } catch (RestClientException e) {
            log.error("Error in createEmployee: {}", e.getMessage());
            return null;
        }
    }

    public DeleteEmployeeResponse deleteEmployeeByName(DeleteEmployeeRequest deleteEmployee) {
        String url = "http://localhost:8112/api/v1/employee";
        try {
            HttpEntity<DeleteEmployeeRequest> requestEntity = new HttpEntity<>(deleteEmployee);
            return restTemplate
                    .exchange(url, HttpMethod.DELETE, requestEntity, DeleteEmployeeResponse.class)
                    .getBody();
        } catch (RestClientException e) {
            log.error("Error in deleteEmployeeByName: {}", e.getMessage());
            return null;
        }
    }
}
