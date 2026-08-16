package com.roshana.consultant.management.service;

import com.roshana.consultant.management.entity.Consultant;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

@Component
public class ConsultantPdfExporter {

    public void export(
            List<Consultant> consultants,
            OutputStream outputStream
    ) throws IOException {

        Document document = new Document(
                PageSize.A4.rotate(),
                20,
                20,
                25,
                25
        );

        try {
            PdfWriter.getInstance(
                    document,
                    outputStream
            );

            document.open();

            addTitle(document, consultants.size());

            PdfPTable table = createTable();
            addConsultants(table, consultants);

            document.add(table);

        } catch (DocumentException exception) {

            throw new IOException(
                    "Unable to create consultant PDF.",
                    exception
            );

        } finally {

            if (document.isOpen()) {
                document.close();
            }
        }
    }

    private void addTitle(
            Document document,
            int totalConsultants
    ) throws DocumentException {

        Font titleFont = new Font(
                Font.HELVETICA,
                18,
                Font.BOLD,
                new Color(22, 75, 112)
        );

        Paragraph title = new Paragraph(
                "Consultant Management System",
                titleFont
        );

        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(8);

        document.add(title);

        Font informationFont = new Font(
                Font.HELVETICA,
                10,
                Font.NORMAL,
                Color.DARK_GRAY
        );

        Paragraph information = new Paragraph(
                "Consultant Report | Generated: "
                        + LocalDate.now()
                        + " | Total Records: "
                        + totalConsultants,
                informationFont
        );

        information.setAlignment(Element.ALIGN_CENTER);
        information.setSpacingAfter(18);

        document.add(information);
    }

    private PdfPTable createTable()
            throws DocumentException {

        PdfPTable table = new PdfPTable(8);

        table.setWidthPercentage(100);

        table.setWidths(new float[]{
                0.6f,
                1.8f,
                2.2f,
                1.4f,
                1.6f,
                1.0f,
                1.2f,
                1.3f
        });

        table.setHeaderRows(1);

        Font headerFont = new Font(
                Font.HELVETICA,
                9,
                Font.BOLD,
                Color.WHITE
        );

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

        for (String heading : headings) {

            PdfPCell headerCell = new PdfPCell(
                    new Paragraph(
                            heading,
                            headerFont
                    )
            );

            headerCell.setBackgroundColor(
                    new Color(20, 123, 184)
            );

            headerCell.setHorizontalAlignment(
                    Element.ALIGN_CENTER
            );

            headerCell.setVerticalAlignment(
                    Element.ALIGN_MIDDLE
            );

            headerCell.setPadding(7);

            table.addCell(headerCell);
        }

        return table;
    }

    private void addConsultants(
            PdfPTable table,
            List<Consultant> consultants
    ) {
        Font cellFont = new Font(
                Font.HELVETICA,
                8,
                Font.NORMAL,
                Color.DARK_GRAY
        );

        for (Consultant consultant : consultants) {

            addCell(
                    table,
                    String.valueOf(consultant.getId()),
                    cellFont
            );

            addCell(
                    table,
                    safeText(consultant.getName()),
                    cellFont
            );

            addCell(
                    table,
                    safeText(consultant.getEmail()),
                    cellFont
            );

            addCell(
                    table,
                    safeText(consultant.getPhone()),
                    cellFont
            );

            addCell(
                    table,
                    safeText(consultant.getTechnology()),
                    cellFont
            );

            addCell(
                    table,
                    consultant.getExperience() + " years",
                    cellFont
            );

            addCell(
                    table,
                    consultant.getStatus() == null
                            ? ""
                            : consultant.getStatus().name(),
                    cellFont
            );

            addCell(
                    table,
                    consultant.getJoinedDate() == null
                            ? ""
                            : consultant.getJoinedDate().toString(),
                    cellFont
            );
        }
    }

    private void addCell(
            PdfPTable table,
            String value,
            Font font
    ) {
        PdfPCell cell = new PdfPCell(
                new Paragraph(value, font)
        );

        cell.setPadding(6);
        cell.setVerticalAlignment(
                Element.ALIGN_MIDDLE
        );

        table.addCell(cell);
    }

    private String safeText(String value) {
        return value == null ? "" : value;
    }
}