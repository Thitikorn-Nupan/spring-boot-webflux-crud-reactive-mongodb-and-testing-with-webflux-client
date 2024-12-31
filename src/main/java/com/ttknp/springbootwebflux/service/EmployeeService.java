package com.ttknp.springbootwebflux.service;

import com.ttknp.springbootwebflux.model.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {
    Flux<Employee> findAll();
    Flux<Employee> findBySalary(float salary);
    Mono<Employee> findById(long id);
    Mono<Employee> save(Mono<Employee> employee);
    Mono<Boolean> deleteById(long id);
    Mono<Boolean> update(Mono<Employee> employee,long id);


}
