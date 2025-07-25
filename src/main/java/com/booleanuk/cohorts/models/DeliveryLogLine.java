package com.booleanuk.cohorts.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "delivery_log_line")
public class DeliveryLogLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
