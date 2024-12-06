package com.example.demo.service;

import com.example.demo.dto.APIResponseDTO;
import com.example.demo.dto.CaseResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CaseService {
    ResponseEntity<APIResponseDTO<CaseResponseDTO>> getCaseById(Long caseId);
    ResponseEntity<APIResponseDTO<List<CaseResponseDTO>>> getCasesByCustomerId(Long customerId);

}
