package com.michael.book_social_network.repository;

import com.michael.book_social_network.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
