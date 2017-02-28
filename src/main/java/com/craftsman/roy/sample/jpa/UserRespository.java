package com.craftsman.roy.sample.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.craftsman.roy.sample.model.User;

/**
 * 
 * @author luoyh
 * @date Feb 24, 2017
 */
@Repository("userRepository")
public interface UserRespository extends JpaRepository<User, Long> {
}
