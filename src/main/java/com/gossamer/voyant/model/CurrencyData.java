package com.gossamer.voyant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyData {
    List<List<String>> currencyData;
}
