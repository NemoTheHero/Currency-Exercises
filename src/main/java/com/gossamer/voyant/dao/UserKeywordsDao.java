package com.gossamer.voyant.dao;

import com.gossamer.voyant.entities.User;
import com.gossamer.voyant.entities.UserKeywords;
import jakarta.persistence.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Table(name = "user_keywords")
public interface UserKeywordsDao
        extends CrudRepository<UserKeywords, Long> {
    List<UserKeywords> findUserKeywordsByKeywordId(Long keywordId);
    List<UserKeywords> findUserKeywordsByKeywordIdAndUserIdIsNot(Long keywordId,Long userId);

    List<UserKeywords> findUserKeywordsByUserId(Long userId);

}