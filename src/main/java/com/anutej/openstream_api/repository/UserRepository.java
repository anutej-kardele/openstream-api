package com.anutej.openstream_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anutej.openstream_api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByHandle(String handle);

    boolean existsByHandle(String handle);

}