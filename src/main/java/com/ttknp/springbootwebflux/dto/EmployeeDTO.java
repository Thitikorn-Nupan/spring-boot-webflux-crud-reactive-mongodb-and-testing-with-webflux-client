package com.ttknp.springbootwebflux.dto;

import com.ttknp.springbootwebflux.model.Employee;
import com.ttknp.springbootwebflux.repository.EmployeeRepo;
import com.ttknp.springbootwebflux.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Flux — เก็บลำดับข้อมูล 0..N (N element) ใช้กับ getCustomers ซึ่ง return ข้อมูลมากว่า 1 element
 * Mono — เก็บลำดับข้อมูล 0..1 (1 element) ใช้กับ method อื่นๆ ที่ return ข้อมูลเพียง element เดียว
 * Flux หรือ Mono ซึ่งเป็น Reactive Stream Publisher
 */
@Service
public class EmployeeDTO implements EmployeeService {

    private final Logger logger;
    private final EmployeeRepo employeeRepo;

    @Autowired
    public EmployeeDTO( EmployeeRepo employeeRepo) {
        this.logger =  LoggerFactory.getLogger(EmployeeDTO.class);
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Flux<Employee> findAll() {
        Flux<Employee> employees = employeeRepo.findAll();
        // employees.log() FluxLog employees.collectList() MonoCollectList employees FluxUsingWhe
        logger.info("employees.log() {} employees.collectList() {} employees {}",employees.log(),employees.collectList(),employees);
        return employees;
    }

    @Override
    public Flux<Employee> findBySalary(float salary) {
        Flux<Employee> employees = employeeRepo.findBySalaryGreaterThan(salary);
        logger.info("employees {}",employees);
        return employees;
    }

    @Override
    public Mono<Employee> findById(long id) {
        return employeeRepo.findById(id);
    }

    /**
     * Stream flatMap(Function mapper) returns a stream consisting of the results of replacing
     * each element of this stream with the contents of a mapped stream produced by applying
     * the provided mapping function to each element. Stream flatMap(Function mapper) is an intermediate operation.
     * These operations are always lazy. Intermediate operations are invoked on a Stream instance
     * and after they finish their processing, they give a Stream instance as output.
     */
    @Override
    public Mono<Employee> save(Mono<Employee> employeeMono) {
        return employeeMono.flatMap((Employee employee) -> { // ** first i have to get member from Mono<Member>
            logger.info("employeeMono {} and employee {}",employeeMono,employee);
            return employeeRepo.save(employee); // ** then save to database
        });
    }

    @Override
    public Mono<Boolean> update(Mono<Employee> employeeMono, long id) {
        return employeeMono.flatMap((Employee employee) ->
                // after get employee from employee mono
                findById(id).flatMap((Employee employeeSearch) -> {
                    logger.debug("memberSearch {}", employeeSearch);
                    employee.setId(employeeSearch.getId());
                    return employeeRepo.save(employee).then(Mono.just(true));
                })).hasElement();
    }


    @Override
    public Mono<Boolean> deleteById(long id) {
        return findById(id).flatMap((Employee employeeSearch) -> {
            logger.debug("memberSearch {}", employeeSearch);
            return employeeRepo.delete(employeeSearch).then(Mono.just(true));
        }).hasElement(); // ** use hasElement() will be false (or null) if not found
    }

}
