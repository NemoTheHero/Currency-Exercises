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
    Long userId;

    @Column
    Long user1Id;

    @Column
    Long user2Id;

    @Column
    Long score;
}
