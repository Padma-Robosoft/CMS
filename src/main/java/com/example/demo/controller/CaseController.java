package com.example.demo.controller;

import com.example.demo.dto.APIResponseDTO;
import com.example.demo.dto.CaseRequestDTO;
import com.example.demo.dto.CaseResponseDTO;
import com.example.demo.service.CaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cases")
public class CaseController {

    @Autowired
    private CaseServiceImpl caseService;

    @GetMapping("/{id}")
    public ResponseEntity<APIResponseDTO<CaseResponseDTO>> getCaseById(@PathVariable Long id) {
        return caseService.getCaseById(id);
    }
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<APIResponseDTO<List<CaseResponseDTO>>> getCasesByCustomerId(@PathVariable Long customerId) {
        return caseService.getCasesByCustomerId(customerId);
    }

    @PostMapping("/customer/{customerId}")
    public ResponseEntity<APIResponseDTO<CaseResponseDTO>> createCase(@RequestBody CaseRequestDTO caseRequest) {
        return caseService.createCase(caseRequest);
    }

    @PutMapping("/customer/{id}")
    public ResponseEntity<APIResponseDTO<CaseResponseDTO>> updateCase(@PathVariable Long id, @RequestBody CaseRequestDTO caseRequestDTO) {
        return caseService.updateCase(id, caseRequestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponseDTO<String>> deleteCase(@PathVariable Long id) {
        return caseService.deleteCase(id);
    }
}
