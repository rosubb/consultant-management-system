package com.roshana.consultant.management.repository;

import com.roshana.consultant.management.entity.Consultant;
import com.roshana.consultant.management.entity.ConsultantStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ConsultantRepository
        extends JpaRepository<Consultant, Long> {

    // Counts consultants who joined between two dates
    long countByJoinedDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    // Counts consultants by status
    long countByStatus(ConsultantStatus status);

    // Checks whether an email address already exists
    boolean existsByEmailIgnoreCase(String email);

    // Existing basic search
    List<Consultant>
    findByNameContainingIgnoreCaseOrTechnologyContainingIgnoreCase(
            String name,
            String technology
    );

    // Groups consultants by technology for the dashboard chart
    @Query("""
           SELECT c.technology, COUNT(c)
           FROM Consultant c
           GROUP BY c.technology
           ORDER BY COUNT(c) DESC
           """)
    List<Object[]> countConsultantsByTechnology();

    // Search, status filter, sorting, and pagination
    @Query("""
           SELECT c
           FROM Consultant c
           WHERE (
               :keyword IS NULL
               OR :keyword = ''
               OR LOWER(c.name) LIKE LOWER(
                   CONCAT('%', :keyword, '%')
               )
               OR LOWER(c.technology) LIKE LOWER(
                   CONCAT('%', :keyword, '%')
               )
               OR LOWER(c.email) LIKE LOWER(
                   CONCAT('%', :keyword, '%')
               )
           )
           AND (
               :status IS NULL
               OR c.status = :status
           )
           """)
    Page<Consultant> searchAndFilter(
            @Param("keyword") String keyword,
            @Param("status") ConsultantStatus status,
            Pageable pageable
    );
}