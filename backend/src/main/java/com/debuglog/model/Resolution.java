package com.debuglog.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resolutions")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Resolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String errorHash;

    @Column(nullable = false)
    private String errorType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String pattern;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String solution;

    @Column(nullable = false)
    private boolean confirmed;

    @Column(nullable = false)
    private int usedCount;
}