package com.roshana.consultant.management.controller;

import com.roshana.consultant.management.entity.Consultant;
import com.roshana.consultant.management.entity.ConsultantStatus;
import com.roshana.consultant.management.service.ConsultantExcelExporter;
import com.roshana.consultant.management.service.ConsultantPdfExporter;
import com.roshana.consultant.management.service.ConsultantService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ConsultantController {

    private final ConsultantService consultantService;
    private final ConsultantExcelExporter excelExporter;
    private final ConsultantPdfExporter pdfExporter;

    public ConsultantController(
            ConsultantService consultantService,
            ConsultantExcelExporter excelExporter,
            ConsultantPdfExporter pdfExporter
    ) {
        this.consultantService = consultantService;
        this.excelExporter = excelExporter;
        this.pdfExporter = pdfExporter;
    }

    @GetMapping({"/", "/dashboard"})
    public String showDashboard(Model model) {

        List<Consultant> consultants =
                consultantService.getAllConsultants();

        double averageExperience = consultants.stream()
                .mapToInt(Consultant::getExperience)
                .average()
                .orElse(0.0);

        model.addAttribute(
                "averageExperience",
                averageExperience
        );

        model.addAttribute(
                "totalConsultants",
                consultantService.getTotalConsultants()
        );

        model.addAttribute(
                "activeConsultants",
                consultantService.getCountByStatus(
                        ConsultantStatus.ACTIVE
                )
        );

        model.addAttribute(
                "inactiveConsultants",
                consultantService.getCountByStatus(
                        ConsultantStatus.INACTIVE
                )
        );

        model.addAttribute(
                "availableConsultants",
                consultantService.getCountByStatus(
                        ConsultantStatus.AVAILABLE
                )
        );

        model.addAttribute(
                "onProjectConsultants",
                consultantService.getCountByStatus(
                        ConsultantStatus.ON_PROJECT
                )
        );

        model.addAttribute(
                "recentConsultants",
                consultants.stream().limit(5).toList()
        );

        model.addAttribute(
                "newConsultantsThisMonth",
                consultantService.getNewConsultantsThisMonth()
        );

        List<Object[]> technologyCounts =
                consultantService.getTechnologyCounts();

        model.addAttribute(
                "technologyLabels",
                technologyCounts.stream()
                        .map(row -> (String) row[0])
                        .toList()
        );

        model.addAttribute(
                "technologyValues",
                technologyCounts.stream()
                        .map(row ->
                                ((Number) row[1]).longValue()
                        )
                        .toList()
        );

        return "dashboard";
    }

    @GetMapping("/consultants")
    public String showConsultants(
            @RequestParam(required = false)
            String keyword,

            @RequestParam(required = false)
            ConsultantStatus status,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String sortDirection,

            Model model
    ) {
        Page<Consultant> consultantPage =
                consultantService.searchAndFilterConsultants(
                        keyword,
                        status,
                        page,
                        size,
                        sortBy,
                        sortDirection
                );

        model.addAttribute(
                "consultants",
                consultantPage.getContent()
        );

        model.addAttribute(
                "currentPage",
                consultantPage.getNumber()
        );

        model.addAttribute(
                "totalPages",
                consultantPage.getTotalPages()
        );

        model.addAttribute(
                "totalItems",
                consultantPage.getTotalElements()
        );

        model.addAttribute(
                "pageSize",
                consultantPage.getSize()
        );

        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedStatus", status);

        model.addAttribute(
                "statuses",
                ConsultantStatus.values()
        );

        model.addAttribute("sortBy", sortBy);

        model.addAttribute(
                "sortDirection",
                sortDirection
        );

        return "consultants";
    }

    @GetMapping("/consultants/export/excel")
    public void exportConsultantsToExcel(
            HttpServletResponse response
    ) throws IOException {

        String fileName =
                "consultants-" + LocalDate.now() + ".xlsx";

        response.setContentType(
                "application/vnd.openxmlformats-officedocument." +
                        "spreadsheetml.sheet"
        );

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"" + fileName + "\""
        );

        excelExporter.export(
                consultantService.getAllConsultants(),
                response.getOutputStream()
        );

        response.flushBuffer();
    }

    @GetMapping("/consultants/export/pdf")
    public void exportConsultantsToPdf(
            HttpServletResponse response
    ) throws IOException {

        String fileName =
                "consultants-" + LocalDate.now() + ".pdf";

        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"" + fileName + "\""
        );

        pdfExporter.export(
                consultantService.getAllConsultants(),
                response.getOutputStream()
        );

        response.flushBuffer();
    }

    @GetMapping("/consultants/add")
    public String showAddForm(Model model) {

        model.addAttribute(
                "consultant",
                new Consultant()
        );

        model.addAttribute(
                "statuses",
                ConsultantStatus.values()
        );

        return "consultant-form";
    }

    @GetMapping("/consultants/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute(
                "consultant",
                consultantService.getConsultantById(id)
        );

        model.addAttribute(
                "statuses",
                ConsultantStatus.values()
        );

        return "consultant-form";
    }

    @PostMapping("/consultants/save")
    public String saveConsultant(
            @Valid Consultant consultant,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "statuses",
                    ConsultantStatus.values()
            );

            return "consultant-form";
        }

        consultantService.saveConsultant(consultant);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Consultant saved successfully."
        );

        return "redirect:/consultants";
    }

    @PostMapping("/consultants/delete/{id}")
    public String deleteConsultant(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        consultantService.deleteConsultant(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Consultant deleted successfully."
        );

        return "redirect:/consultants";
    }
}