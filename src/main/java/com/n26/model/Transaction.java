package com.n26.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Transaction {
    private double amount;
    private long timestamp;
    private long expiryTimestamp;
}
