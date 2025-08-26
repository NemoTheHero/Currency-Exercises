package com.gossamer.voyant.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "user_keywords")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserKeywords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    Long userId;

    @Column
    Long keywordId;

    @Column
    Long score;

}
