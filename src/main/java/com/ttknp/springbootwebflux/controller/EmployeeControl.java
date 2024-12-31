package com.ttknp.springbootwebflux.controller;

import com.ttknp.springbootwebflux.model.Employee;
import com.ttknp.springbootwebflux.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api")
public class EmployeeControl {
    private EmployeeService employeeService;
    @Autowired
    public EmployeeControl(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @GetMapping(value = {"/employees/", "/employees"})
    private ResponseEntity<Flux<Employee>> getAllEmployees() {
        return ResponseEntity
                .status(202)
                .body(employeeService.findAll());
    }

    @GetMapping(value = "/employees/greater")
    private ResponseEntity<Flux<Employee>> getAllBySalaryGreaterThan(@RequestParam float salary) {
        return ResponseEntity
                .status(202)
                .body(employeeService.findBySalary(salary));
    }

    @GetMapping(value = "/employees/unique")
    private ResponseEntity<Mono<Employee>> getEmployee(@RequestParam long id) {
        return ResponseEntity
                .status(202)
                .body(employeeService.findById(id));
    }

    @PostMapping(value = "/employees")
    private ResponseEntity<Mono<Employee>> saveEmployee(@RequestBody Mono<Employee> employee) {
        return ResponseEntity
                .status(201)
                .body(employeeService.save(employee));
    }

    @DeleteMapping(value = "/employees")
    private ResponseEntity<Mono<Boolean>> deleteEmployee(@RequestParam long id){
        return ResponseEntity
                .status(202)
                .body(employeeService.deleteById(id));
    }

    @PutMapping(value = "/employees")
    private ResponseEntity<Mono<Boolean>> updateEmployee(@RequestBody Mono<Employee> employee,@RequestParam long id){
        return ResponseEntity
                .status(202)
                .body(employeeService.update(employee,id));
    }
}
