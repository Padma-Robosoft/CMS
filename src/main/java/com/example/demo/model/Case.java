package com.example.demo.model;


import com.example.demo.model.enums.Priority;
import com.example.demo.model.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cases")
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long caseId;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM;

    private String assignedTo = "Unassigned";

    @Column(nullable = false)
    private Long customerId;
}
