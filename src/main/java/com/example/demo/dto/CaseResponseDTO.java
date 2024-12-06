package com.example.demo.dto;

import com.example.demo.model.enums.Priority;
import com.example.demo.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaseResponseDTO {
    private Long caseId;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private String assignedTo;
    private Long customerId;

}
