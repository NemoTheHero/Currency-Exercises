package com.gossamer.voyant.dao;

import com.gossamer.voyant.entities.Keyword;
import jakarta.persistence.Table;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Table(name = "keywords")
public interface KeywordsDao
        extends CrudRepository<Keyword, Long> {
    Optional<Keyword> findById(Long userId);

    @Query("select k from Keyword k where k.keyword = ?1")
    Optional<Keyword> findByKeyword(String keyword);

    @Query("select k from Keyword k where k.keyword in ?1")
    List<Keyword> getKeywordsByNames(List<String> names);
}
