package com.gossamer.voyant.dao;

import com.gossamer.voyant.entities.UserKeywords;
import com.gossamer.voyant.entities.UserUserScore;
import jakarta.persistence.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Table(name = "user_user_score")
public interface UserUserScoreDao
        extends CrudRepository<UserUserScore, Long> {

    Optional<UserUserScore> findUserUserScoreByUser1IdAndUser2Id(Long lowerUserId, Long higherUserId);

    List<UserUserScore> findUserUserScoreByUser1IdOrUser2IdOrderByScoreDesc(Long userId1, Long userId2);

}
