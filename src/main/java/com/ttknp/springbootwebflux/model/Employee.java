package com.ttknp.springbootwebflux.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@NoArgsConstructor

@Document(collection = "employees" ) // this is way it will be mapped to a Document in the MongoDB database.
public class Employee {
    @Id // ** it will map _id on collection auto **  and it is not auto increment
    private Long id;
    private String fullname;
    private Short age;
    private Float salary;
    private String gender;
}
