package com.booleanuk.cohorts.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@Data
@Entity
@Table(name = "delivery_logs")
public class DeliveryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @Column
    boolean edited = false;

    @Column
    String title;

    @Column
    String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("delivery_logs")
    private User author;

    @ManyToOne
    @JoinColumn(name = "cohort_id", nullable = false)
    @JsonIgnoreProperties("delivery_logs")
    private Cohort cohort;


}
