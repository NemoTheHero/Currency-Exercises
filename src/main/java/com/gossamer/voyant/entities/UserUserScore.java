package com.gossamer.voyant.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "user_user_score")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserUserScore {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @Column(name = "user1_id")
    Long user1Id;

    @Column(name = "user2_id")
    Long user2Id;

    @Column
    Long score;
}
