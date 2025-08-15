package com.reliaquest.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.Model.Employee;
import com.reliaquest.api.Model.request.DeleteEmployeeRequest;
import com.reliaquest.api.Model.request.EmployeeRequest;
import com.reliaquest.api.Model.response.DeleteEmployeeResponse;
import com.reliaquest.api.Model.response.GetEmployeeResponse;
import com.reliaquest.api.service.EmployeeService;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ApiApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(
                new Employee(UUID.randomUUID(), "John Doe", 60000, 30, "IT", "john.doe@example.com"),
                new Employee(UUID.randomUUID(), "Jane Smith", 75000, 35, "HR", "jane.smith@example.com"));
        when(employeeService.getEmployees()).thenReturn(employees);

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));
    }

    @Test
    void getEmployeesByNameSearch() throws Exception {
        List<Employee> employees = Arrays.asList(
                new Employee(UUID.randomUUID(), "John Doe", 60000, 30, "IT", "john.doe@example.com"),
                new Employee(UUID.randomUUID(), "Jane Smith", 75000, 35, "HR", "jane.smith@example.com"),
                new Employee(UUID.randomUUID(), "Doe something", 80000, 40, "Sales", "doe.something@example.com"));
        when(employeeService.getEmployees()).thenReturn(employees);

        mockMvc.perform(get("/search/Doe"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Doe something"));
    }

    @Test
    void getEmployeeById() throws Exception {
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee(employeeId, "John Doe", 60000, 30, "IT", "john.doe@example.com");
        when(employeeService.getEmployeesById(any(String.class))).thenReturn(employee);

        mockMvc.perform(get("/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getHighestSalaryOfEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(
                new Employee(UUID.randomUUID(), "John Doe", 60000, 30, "IT", "john.doe@example.com"),
                new Employee(UUID.randomUUID(), "Jane Smith", 75000, 35, "HR", "jane.smith@example.com"),
                new Employee(UUID.randomUUID(), "Bob Johnson", 80000, 40, "Sales", "bob.johnson@example.com"));
        when(employeeService.getEmployees()).thenReturn(employees);

        mockMvc.perform(get("/highestSalary"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("80000"));
    }

    @Test
    void getTopTenHighestEarningEmployeeNames() throws Exception {
        List<Employee> employees = Arrays.asList(
                new Employee(UUID.randomUUID(), "Employee1", 100000, 25, "IT", "e1@example.com"),
                new Employee(UUID.randomUUID(), "Employee2", 90000, 28, "HR", "e2@example.com"),
                new Employee(UUID.randomUUID(), "Employee3", 80000, 30, "Sales", "e3@example.com"),
                new Employee(UUID.randomUUID(), "Employee4", 70000, 32, "Marketing", "e4@example.com"),
                new Employee(UUID.randomUUID(), "Employee5", 60000, 35, "Finance", "e5@example.com"),
                new Employee(UUID.randomUUID(), "Employee6", 50000, 38, "IT", "e6@example.com"),
                new Employee(UUID.randomUUID(), "Employee7", 40000, 40, "HR", "e7@example.com"),
                new Employee(UUID.randomUUID(), "Employee8", 30000, 42, "Sales", "e8@example.com"),
                new Employee(UUID.randomUUID(), "Employee9", 20000, 45, "Marketing", "e9@example.com"),
                new Employee(UUID.randomUUID(), "Employee10", 10000, 48, "Finance", "e10@example.com"),
                new Employee(UUID.randomUUID(), "Employee11", 5000, 50, "IT", "e11@example.com"));
        when(employeeService.getEmployees()).thenReturn(employees);

        mockMvc.perform(get("/topTenHighestEarningEmployeeNames"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0]").value("Employee1"))
                .andExpect(jsonPath("$[9]").value("Employee10"));
    }

    @Test
    void createEmployee() throws Exception {
        EmployeeRequest newEmployeeRequest = new EmployeeRequest("New Employee", 55000, 25, "New Department");
        UUID createdEmployeeId = UUID.randomUUID();
        Employee createdEmployee = new Employee(
                createdEmployeeId, "New Employee", 55000, 25, "New Department", "new.employee@example.com");
        when(employeeService.createEmployee(any(EmployeeRequest.class)))
                .thenReturn(new GetEmployeeResponse(createdEmployee, "Success"));

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployeeRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Employee"));
    }

    @Test
    void deleteEmployeeById() throws Exception {
        UUID employeeToDeleteId = UUID.randomUUID();
        Employee employeeToDelete =
                new Employee(employeeToDeleteId, "John Doe", 60000, 30, "IT", "john.doe@example.com");
        when(employeeService.getEmployeesById("1")).thenReturn(employeeToDelete);
        when(employeeService.deleteEmployeeByName(any(DeleteEmployeeRequest.class)))
                .thenReturn(new DeleteEmployeeResponse(true, "Success"));

        mockMvc.perform(delete("/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }
}
