package com.coderdot.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Diag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private int age;
    private String observation;

    @Column(name = "deleted")
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Customer createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private Customer updatedBy;

    @ManyToOne
    @JoinColumn(name = "deleted_by")
    private Customer deletedBy;

}