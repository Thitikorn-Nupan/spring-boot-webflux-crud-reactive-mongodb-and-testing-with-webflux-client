package com.ttknp.springbootwebflux;

import com.ttknp.springbootwebflux.model.Employee;
import com.ttknp.springbootwebflux.repository.EmployeeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;


import java.util.List;

// ** remember we're mocking so we don't access database we access only layer (service) logic
// ** response follow your api
@ExtendWith(SpringExtension.class) // We are using @ExtendWith( SpringExtension.class ) to support testing in Junit 5. In Junit 4
@SpringBootTest
public class WebFluxClientTesting {

    private WebTestClient webTestClient;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private EmployeeRepo employeeRepo;


    @BeforeEach
    public void setUp() throws Exception {
        webTestClient = WebTestClient.bindToApplicationContext(applicationContext).build();
    }

    @Test
    public void testHttpGetFindEmployee() {
        // Employee employee = getEmployee();
        // ** first save employee for mocking
        // Long mockedId = employeeRepo.save(employee).map(Employee::getId).block();
        // ** last mocking req api
        // ** testing with real response
        webTestClient.get()
                .uri(String.format("/api/employees/unique?id=%s", 1))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody()
                .jsonPath("id").isEqualTo(1L);
    }

    @Test
    public void testHttpGetFindEmployeesBySalary() {
        // Employee employee = getEmployee();
        // ** first save employee for mocking
        // Flux<Employee> employeeFlux = employeeRepo.saveAll(List.of(employee,employee));
        // ** last mocking req api
        webTestClient.get()
                .uri(String.format("/api/employees/greater?salary=%s", 60000))
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(Employee.class)
                .hasSize(0);
    }

    @Test
    public void testHttpGetFindAllEmployees() {
        List<Employee> employeeList = getEmployees();
        employeeRepo.saveAll(employeeList).blockLast();
        webTestClient.get()
                .uri("/api/employees")
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(Employee.class)
                .contains(employeeList.get(0), employeeList.get(1));
    }

    @Test
    public void testHttpPostSaveEmployee() {
        Employee employee = getEmployee();
        // ** first save employee for mocking
        employeeRepo.save(employee).block();
        // ** last mocking req api
        webTestClient.post()
                .uri(String.format("/api/employees"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(employee))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("id").isEqualTo(1L);
    }

    @Test
    public void testHttpPutUpdateEmployee() {
        Employee employee = getEmployee();
        // ** first save employee for mocking
        employeeRepo.save(employee).block();
        // ** last mocking req api
        webTestClient.put()
                .uri(String.format("/api/employees?id=%s", employee.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(employee))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(Boolean.class)
                .isEqualTo(true);
    }

    @Test
    public void testHttpDeleteRemoveEmployee() {
        Employee employee = getEmployee();
        // ** first save employee for mocking
        employeeRepo.save(employee).block();
        // ** last mocking req api
        webTestClient.delete()
                .uri(String.format("/api/employees?id=%s", employee.getId()))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(Boolean.class)
                .isEqualTo(true);
    }

    private Employee getEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setAge((short) 26);
        employee.setFullname("Test Test");
        employee.setGender("Male");
        return employee;
    }

    private List<Employee> getEmployees() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setAge((short) 26);
        employee.setFullname("Test Test");
        employee.setGender("Male");
        return List.of(employee,employee);
    }

}
