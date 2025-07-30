package com.booleanuk.cohorts.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "delivery_log_lines")
public class DeliveryLogLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "log_id", nullable = false)
    @JsonIgnoreProperties("delivery_log_lines")
    private DeliveryLog log;

    public DeliveryLogLine(String content, DeliveryLog log) {
        this.content = content;
        this.log = log;
    }
}
