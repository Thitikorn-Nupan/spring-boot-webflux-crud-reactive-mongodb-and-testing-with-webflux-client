package com.ttknp.springbootwebflux.repository;

import com.ttknp.springbootwebflux.model.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface EmployeeRepo extends ReactiveMongoRepository<Employee, Long> { // ** ReactiveCrudRepository is Repository sub port Reactive CRUD operation work like  *** MongoRepository
    Flux<Employee> findBySalaryGreaterThan(Float salaryIsGreaterThan);
}
