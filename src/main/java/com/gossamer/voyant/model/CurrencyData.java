package com.gossamer.voyant.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CurrencyData {
    List<List<String>> currencyData;
}
