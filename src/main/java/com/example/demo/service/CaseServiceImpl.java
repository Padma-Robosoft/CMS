package com.example.demo.service;

import com.example.demo.dto.APIResponseDTO;
import com.example.demo.dto.CaseRequestDTO;
import com.example.demo.dto.CaseResponseDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Case;
import com.example.demo.model.enums.Priority;
import com.example.demo.model.enums.Status;
import com.example.demo.repository.CaseRepository;
import com.example.demo.util.APIResponseUtil;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class  CaseServiceImpl implements CaseService{
    @Autowired
    private CaseRepository caseRepository;
    private static final Logger logger = LogManager.getLogger(CaseServiceImpl.class);

    @Value("${log.case.getById}")
    private String logGetCaseById;

    @Value("${log.case.getByCustomerId}")
    private String logGetCasesByCustomerId;

    @Value("${response.caseById}")
    private String caseByIdMessage;

    @Value("${response.casesByCustomer}")
    private String casesByCustomerMessage;

    @Value("${error.case.not-found}")
    private String caseNotFoundMessage;

    @Value("${error.case.no-cases-found-for-customer}")
    private String noCasesFoundForCustomerMessage;

    @Value("${log.case.create}")
    private String logCreateCase;

    @Value("${log.case.update}")
    private String logUpdateCase;

    @Value("${log.case.delete}")
    private String logDeleteCase;

    @Value("${response.case.created}")
    private String caseCreatedMessage;

    @Value("${response.case.updated}")
    private String caseUpdatedMessage;

    @Value("${response.case.deleted}")
    private String caseDeletedMessage;


    public ResponseEntity<APIResponseDTO<CaseResponseDTO>> getCaseById(Long caseId) {
        logger.info(String.format(logGetCaseById, caseId));
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> {
                    logger.error(String.format(caseNotFoundMessage, caseId));
                    return new ResourceNotFoundException(String.format(caseNotFoundMessage, caseId));
                });
        CaseResponseDTO caseResponseDTO = new CaseResponseDTO(
                caseEntity.getCaseId(),
                caseEntity.getTitle(),
                caseEntity.getDescription(),
                caseEntity.getStatus(),
                caseEntity.getPriority(),
                caseEntity.getAssignedTo(),
                caseEntity.getCustomerId()
        );
        return ResponseEntity.ok(APIResponseUtil.createSuccessResponse(caseResponseDTO, caseByIdMessage));
    }

    public ResponseEntity<APIResponseDTO<List<CaseResponseDTO>>> getCasesByCustomerId(Long customerId) {
        logger.info(String.format(logGetCasesByCustomerId, customerId));
        List<Case> cases = caseRepository.findByCustomerId(customerId);
        if (cases.isEmpty()) {
            logger.error(String.format(noCasesFoundForCustomerMessage, customerId));
            throw new ResourceNotFoundException(String.format(noCasesFoundForCustomerMessage, customerId));
        }
        List<CaseResponseDTO> caseResponseDTOs = cases.stream()
                .map(caseEntity -> new CaseResponseDTO(
                        caseEntity.getCaseId(),
                        caseEntity.getTitle(),
                        caseEntity.getDescription(),
                        caseEntity.getStatus(),
                        caseEntity.getPriority(),
                        caseEntity.getAssignedTo(),
                        caseEntity.getCustomerId()
                ))
                .collect(Collectors.toList());

        logger.info(APIResponseUtil.createSuccessResponse(caseResponseDTOs, casesByCustomerMessage));
        return ResponseEntity.ok(APIResponseUtil.createSuccessResponse(caseResponseDTOs, casesByCustomerMessage));
    }

    public ResponseEntity<APIResponseDTO<CaseResponseDTO>> createCase(CaseRequestDTO caseRequestDTO) {
        logger.info(String.format(logCreateCase, caseRequestDTO.getTitle()));

        Case caseEntity = new Case();
        caseEntity.setTitle(caseRequestDTO.getTitle());
        caseEntity.setDescription(caseRequestDTO.getDescription());
        caseEntity.setStatus(caseRequestDTO.getStatus() != null ? caseRequestDTO.getStatus() : Status.NEW);
        caseEntity.setPriority(caseRequestDTO.getPriority() != null ? caseRequestDTO.getPriority() : Priority.MEDIUM);
        caseEntity.setAssignedTo(caseRequestDTO.getAssignedTo() != null ? caseRequestDTO.getAssignedTo() : "Unassigned");
        caseEntity.setCustomerId(caseRequestDTO.getCustomerId());

        // Save the case entity
        Case savedCase = caseRepository.save(caseEntity);
        CaseResponseDTO caseResponseDTO = new CaseResponseDTO(
                savedCase.getCaseId(),
                savedCase.getTitle(),
                savedCase.getDescription(),
                savedCase.getStatus(),
                savedCase.getPriority(),
                savedCase.getAssignedTo(),
                savedCase.getCustomerId()
        );

        return ResponseEntity.status(201).body(APIResponseUtil.createSuccessResponse(caseResponseDTO, "Case created successfully"));
    }

    public ResponseEntity<APIResponseDTO<CaseResponseDTO>> updateCase(Long id, CaseRequestDTO caseRequestDTO) {
        logger.info(String.format(logUpdateCase, id));
        Case caseEntity = caseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(String.format(caseNotFoundMessage, id));
                    return new ResourceNotFoundException(String.format(caseNotFoundMessage, id));
                });
        caseEntity.setTitle(caseRequestDTO.getTitle());
        caseEntity.setDescription(caseRequestDTO.getDescription());
        caseEntity.setStatus(caseRequestDTO.getStatus() != null ? caseRequestDTO.getStatus() : caseEntity.getStatus());
        caseEntity.setPriority(caseRequestDTO.getPriority() != null ? caseRequestDTO.getPriority() : caseEntity.getPriority());
        caseEntity.setAssignedTo(caseRequestDTO.getAssignedTo() != null ? caseRequestDTO.getAssignedTo() : caseEntity.getAssignedTo());
        caseEntity.setCustomerId(caseRequestDTO.getCustomerId());

        Case updatedCase = caseRepository.save(caseEntity);
        CaseResponseDTO caseResponseDTO = new CaseResponseDTO(
                updatedCase.getCaseId(),
                updatedCase.getTitle(),
                updatedCase.getDescription(),
                updatedCase.getStatus(),
                updatedCase.getPriority(),
                updatedCase.getAssignedTo(),
                updatedCase.getCustomerId()
        );
        return ResponseEntity.ok(APIResponseUtil.createSuccessResponse(caseResponseDTO, caseUpdatedMessage));
    }

    public ResponseEntity<APIResponseDTO<String>> deleteCase(Long id) {
        logger.info(String.format(logDeleteCase, id));
        Case caseEntity = caseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(String.format(caseNotFoundMessage, id));
                    return new ResourceNotFoundException(String.format(caseNotFoundMessage, id));
                });
        caseRepository.delete(caseEntity);
        return ResponseEntity.ok(APIResponseUtil.createSuccessResponse(caseDeletedMessage, caseDeletedMessage));
    }
}

