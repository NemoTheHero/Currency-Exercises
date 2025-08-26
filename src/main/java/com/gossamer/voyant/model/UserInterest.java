package com.gossamer.voyant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserInterest {
    Long userId;
    Long keywordId;
    String name;
    Long score;
}
