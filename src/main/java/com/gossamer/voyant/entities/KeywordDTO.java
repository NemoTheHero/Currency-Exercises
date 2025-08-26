package com.gossamer.voyant.entities;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class KeywordDTO {
    private String keyword;
    private int score;
}
