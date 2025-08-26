package com.gossamer.voyant.dao;

import com.gossamer.voyant.entities.Keywords;
import com.gossamer.voyant.entities.User;
import jakarta.persistence.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Table(name = "keywords")
public interface KeywordsDao
        extends CrudRepository<Keywords, Long> {
    Optional<Keywords> findById(Long userId);

}
