package com.reliaquest.api.controller;

import com.reliaquest.api.Model.Employee;
import com.reliaquest.api.Model.request.DeleteEmployeeRequest;
import com.reliaquest.api.Model.request.EmployeeRequest;
import com.reliaquest.api.service.EmployeeService;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class EmployeeController implements IEmployeeController<Employee, EmployeeRequest> {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getEmployees();
        log.info("Employees information Requested");
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        log.info("Employee name containing:{}, Requested", searchString);
        return ResponseEntity.ok(employeeService.getEmployees().stream()
                .filter(employee -> employee.getName().contains(searchString))
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        log.info("Employee with {}, Requested", id);
        return ResponseEntity.ok(employeeService.getEmployeesById(id));
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return ResponseEntity.ok(employeeService.getEmployees().stream()
                .max(Comparator.comparing(Employee::getSalary))
                .map(Employee::getSalary)
                .orElse(null));
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return ResponseEntity.ok(employeeService.getEmployees().stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .limit(10)
                .map(Employee::getName)
                .toList());
    }

    @Override
    public ResponseEntity<Employee> createEmployee(EmployeeRequest employeeInput) {
        log.info("Employee created: {}", employeeInput);
        return ResponseEntity.ok(employeeService.createEmployee(employeeInput).getData());
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        log.info("Employee {} requested to be deleted", id);
        String name = employeeService.getEmployeesById(id).getName();
        DeleteEmployeeRequest deleteEmployee = new DeleteEmployeeRequest(name);
        return ResponseEntity.ok(
                employeeService.deleteEmployeeByName(deleteEmployee).getStatus());
    }
}
