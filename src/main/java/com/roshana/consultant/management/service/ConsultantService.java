package com.roshana.consultant.management.service;

import com.roshana.consultant.management.entity.Consultant;
import com.roshana.consultant.management.entity.ConsultantStatus;
import com.roshana.consultant.management.repository.ConsultantRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ConsultantService {

    private final ConsultantRepository consultantRepository;

    public ConsultantService(
            ConsultantRepository consultantRepository
    ) {
        this.consultantRepository = consultantRepository;
    }

    public List<Consultant> getAllConsultants() {
        return consultantRepository.findAll(
                Sort.by(Sort.Direction.DESC, "id")
        );
    }

    public List<Consultant> searchConsultants(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllConsultants();
        }

        String cleanedKeyword = keyword.trim();

        return consultantRepository
                .findByNameContainingIgnoreCaseOrTechnologyContainingIgnoreCase(
                        cleanedKeyword,
                        cleanedKeyword
                );
    }

    public Page<Consultant> searchAndFilterConsultants(
            String keyword,
            ConsultantStatus status,
            int page,
            int size,
            String sortBy,
            String sortDirection
    ) {
        String cleanedKeyword =
                keyword == null ? "" : keyword.trim();

        int safePage = Math.max(page, 0);

        int safeSize = Math.max(5, Math.min(size, 100));

        String safeSortBy = switch (sortBy) {
            case "name",
                 "email",
                 "technology",
                 "experience",
                 "status",
                 "joinedDate" -> sortBy;

            default -> "id";
        };

        Sort.Direction direction =
                "asc".equalsIgnoreCase(sortDirection)
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                safePage,
                safeSize,
                Sort.by(direction, safeSortBy)
        );

        return consultantRepository.searchAndFilter(
                cleanedKeyword,
                status,
                pageable
        );
    }

    public Consultant getConsultantById(Long id) {
        return consultantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Consultant not found with ID: " + id
                ));
    }

    @Transactional
    public Consultant saveConsultant(Consultant consultant) {
        if (consultant.getStatus() == null) {
            consultant.setStatus(ConsultantStatus.ACTIVE);
        }

        if (consultant.getJoinedDate() == null) {
            consultant.setJoinedDate(LocalDate.now());
        }

        return consultantRepository.save(consultant);
    }

    @Transactional
    public void deleteConsultant(Long id) {
        Consultant consultant = getConsultantById(id);
        consultantRepository.delete(consultant);
    }

    public long getTotalConsultants() {
        return consultantRepository.count();
    }

    public long getCountByStatus(ConsultantStatus status) {
        return consultantRepository.countByStatus(status);
    }

    public long getNewConsultantsThisMonth() {
        LocalDate startOfMonth =
                LocalDate.now().withDayOfMonth(1);

        LocalDate endOfMonth =
                LocalDate.now().withDayOfMonth(
                        LocalDate.now().lengthOfMonth()
                );

        return consultantRepository.countByJoinedDateBetween(
                startOfMonth,
                endOfMonth
        );
    }

    public List<Object[]> getTechnologyCounts() {
        return consultantRepository
                .countConsultantsByTechnology();
    }
}