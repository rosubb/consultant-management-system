package com.roshana.consultant.management.controller;

import com.roshana.consultant.management.entity.ConsultantStatus;
import com.roshana.consultant.management.service.ConsultantService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ReportController {

    private final ConsultantService consultantService;

    public ReportController(
            ConsultantService consultantService
    ) {
        this.consultantService = consultantService;
    }

    @GetMapping("/reports")
    public String showReports(Model model) {

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
                "newConsultantsThisMonth",
                consultantService.getNewConsultantsThisMonth()
        );

        List<Object[]> technologyCounts =
                consultantService.getTechnologyCounts();

        model.addAttribute(
                "technologyCounts",
                technologyCounts
        );

        return "reports";
    }
}