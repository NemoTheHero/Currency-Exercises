package com.gossamer.voyant.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder

public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;
    @Column(nullable = false, name = "username")
    String userName;
}
