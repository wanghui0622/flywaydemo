package com.wanghowie.model;

import lombok.Data;

import javax.persistence.*;


/**
 * @auther 王辉
 * @create 2020-08-13 1:17
 * @Description
 */
@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private Integer age;
}
