package com.gossamer.voyant.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "user_user_score")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserUserScore {
    @Id
    int userId;

    @Column
    int user1Id;

    @Column
    int user2Id;

    @Column
    int score;
}
