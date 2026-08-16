package com.roshana.consultant.management.service;

import com.roshana.consultant.management.entity.Consultant;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Component
public class ConsultantExcelExporter {

    public void export(
            List<Consultant> consultants,
            OutputStream outputStream
    ) throws IOException {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet =
                    workbook.createSheet("Consultants");

            createHeader(workbook, sheet);

            int rowNumber = 1;

            for (Consultant consultant : consultants) {

                Row row = sheet.createRow(rowNumber++);

                row.createCell(0).setCellValue(
                        consultant.getId()
                );

                row.createCell(1).setCellValue(
                        safeText(consultant.getName())
                );

                row.createCell(2).setCellValue(
                        safeText(consultant.getEmail())
                );

                row.createCell(3).setCellValue(
                        safeText(consultant.getPhone())
                );

                row.createCell(4).setCellValue(
                        safeText(consultant.getTechnology())
                );

                row.createCell(5).setCellValue(
                        consultant.getExperience()
                );

                row.createCell(6).setCellValue(
                        consultant.getStatus() == null
                                ? ""
                                : consultant.getStatus().name()
                );

                row.createCell(7).setCellValue(
                        consultant.getJoinedDate() == null
                                ? ""
                                : consultant.getJoinedDate().toString()
                );
            }

            for (int column = 0; column < 8; column++) {
                sheet.autoSizeColumn(column);
            }

            workbook.write(outputStream);
        }
    }

    private void createHeader(
            Workbook workbook,
            Sheet sheet
    ) {
        Row headerRow = sheet.createRow(0);

        String[] headings = {
                "ID",
                "Name",
                "Email",
                "Phone",
                "Technology",
                "Experience",
                "Status",
                "Joined Date"
        };

        CellStyle headerStyle =
                workbook.createCellStyle();

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(
                IndexedColors.WHITE.getIndex()
        );

        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(
                IndexedColors.DARK_BLUE.getIndex()
        );
        headerStyle.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        for (int column = 0;
             column < headings.length;
             column++) {

            var cell = headerRow.createCell(column);
            cell.setCellValue(headings[column]);
            cell.setCellStyle(headerStyle);
        }
    }

    private String safeText(String value) {
        return value == null ? "" : value;
    }
}