package com.gossamer.voyant.dao;

import com.gossamer.voyant.entities.User;
import jakarta.persistence.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Table(name = "user")
public interface UserDao
        extends CrudRepository<User, Long> {


    Optional<User> findById(Long userId);

    List<User> findAll();

}
