package com.gossamer.voyant.model;

import com.gossamer.voyant.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserScore {
    Long userId;
    String name;
    Long score;
}
