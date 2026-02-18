package com.challenge.api.service;

import com.challenge.api.model.Employee;
import com.challenge.api.model.EmployeeEntity;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final Map<UUID, Employee> employeeStore = new ConcurrentHashMap<>();

    public EmployeeServiceImpl() {
        seedMockData();
    }

    @Override
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employeeStore.values());
    }

    @Override
    public Employee getEmployeeByUuid(UUID uuid) {
        Employee employee = employeeStore.get(uuid);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with UUID: " + uuid);
        }
        return employee;
    }

    @Override
    public Employee createEmployee(
            String firstName,
            String lastName,
            Integer salary,
            Integer age,
            String jobTitle,
            String email,
            Instant contractHireDate) {
        EmployeeEntity employee = EmployeeEntity.builder()
                .uuid(UUID.randomUUID())
                .firstName(firstName)
                .lastName(lastName)
                .fullName(firstName + " " + lastName)
                .salary(salary)
                .age(age)
                .jobTitle(jobTitle)
                .email(email)
                .contractHireDate(contractHireDate)
                .contractTerminationDate(null)
                .build();
        employeeStore.put(employee.getUuid(), employee);
        return employee;
    }

    private void seedMockData() {
        createEmployee(
                "John",
                "Doe",
                75000,
                30,
                "Software Engineer",
                "john.doe@company.com",
                Instant.parse("2022-01-15T00:00:00Z"));
        createEmployee(
                "Jane",
                "Smith",
                85000,
                35,
                "Senior Developer",
                "jane.smith@company.com",
                Instant.parse("2021-06-01T00:00:00Z"));
        createEmployee(
                "Bob",
                "Johnson",
                65000,
                28,
                "Junior Developer",
                "bob.johnson@company.com",
                Instant.parse("2023-03-20T00:00:00Z"));
    }
}
