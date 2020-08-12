package com.wanghowie.repository;

import com.wanghowie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @auther 王辉
 * @create 2020-08-13 1:21
 * @Description
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
