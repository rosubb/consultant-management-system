package com.roshana.consultant.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;



@Entity
@Table(name = "consultants")
public class Consultant {
    @Column(name = "joined_date", nullable = false)
    private LocalDate joinedDate;
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public ConsultantStatus getStatus() {
        return status;
    }

    public void setStatus(ConsultantStatus status) {
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9+()\\- ]{7,20}$",
            message = "Enter a valid phone number"
    )
    @Column(nullable = false, length = 20)
    private String phone;

    @NotBlank(message = "Technology is required")
    @Size(max = 100, message = "Technology cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String technology;

    @NotNull(message = "Experience is required")
    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 50, message = "Experience cannot exceed 50 years")
    @Column(nullable = false)
    private Integer experience;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ConsultantStatus status = ConsultantStatus.ACTIVE;

    public LocalDate getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(LocalDate joinedDate) {
        this.joinedDate = joinedDate;
    }

    public Consultant() {
    }

    public Consultant(
            String name,
            String email,
            String phone,
            String technology,
            Integer experience,
            ConsultantStatus status
    ) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.technology = technology;
        this.experience = experience;
        this.status = status;
    }
}