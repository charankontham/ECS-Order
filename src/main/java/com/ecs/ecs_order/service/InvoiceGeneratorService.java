package com.ecs.ecs_order.service;

import com.ecs.ecs_order.dto.InvoiceData;
import com.ecs.ecs_order.dto.ProductDto;
import com.ecs.ecs_order.dto.ProductFinalDto;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceGeneratorService {

    public void generateInvoice(String outputFilePath, InvoiceData invoiceData) throws Exception {
        // Create the PDF Writer and Document
        PdfWriter writer = new PdfWriter(new FileOutputStream(outputFilePath));
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument, PageSize.A4);

        // Add Header Section
        addHeader(document);

        // Add Order Details
        addOrderDetails(document, invoiceData);

        // Add Product Table
        addProductTable(document, invoiceData);

        // Add Summary Section
//        addSummary(document, invoiceData);

        // Add Footer
        addFooter(document);

        document.close();
    }

    private void addHeader(Document document) throws IOException {
        PdfFont boldFont = PdfFontFactory.createFont("Helvetica-Bold");
        Table headerTable = new Table(new float[]{1, 1});
        headerTable.setWidth(UnitValue.createPercentValue(100));
        headerTable.setMarginTop(12f);
        headerTable.setMarginBottom(18f);

        Cell companyName = new Cell()
                .add(new Paragraph("ECS-Shopper")
                        .setFontSize(15)
                        .setFont(boldFont))
                .setBorder(null)
                .setTextAlignment(TextAlignment.LEFT);
        headerTable.addCell(companyName);
        Cell documentName = new Cell()
                .add(new Paragraph("Tax Invoice/Bill of Supply")
                        .setFontSize(14)
                        .setFont(boldFont))
                .setBorder(null)
                .setTextAlignment(TextAlignment.RIGHT);
        headerTable.addCell(documentName);

        document.add(headerTable);
    }

    private void addOrderDetails(Document document, InvoiceData invoiceData) throws IOException {
        PdfFont boldFont = PdfFontFactory.createFont("Helvetica-Bold");

        Table orderDetailsAndAddressTable = new Table(new float[]{1, 1});
        orderDetailsAndAddressTable.setWidth(UnitValue.createPercentValue(100));

        Text orderIdLabel = new Text("Order Id: ").setFont(boldFont).setFontSize(12);
        Text orderIdValue = new Text("# " + invoiceData.getOrderId()).setFontSize(12);
        Text orderDateLabel = new Text("Order Date: ").setFont(boldFont).setFontSize(12);
        Text orderDateValue = new Text(invoiceData.getOrderDate().toString()).setFontSize(12);
        Text invoiceIdLabel = new Text("Invoice Id: ").setFont(boldFont).setFontSize(12);
        Text invoiceIdValue = new Text("12345").setFontSize(12);

        Paragraph orderId = new Paragraph()
                .add(orderIdLabel)
                .add(orderIdValue)
                .setMultipliedLeading(1.5f)
                .setTextAlignment(TextAlignment.LEFT);

        Paragraph invoiceId = new Paragraph()
                .add(invoiceIdLabel)
                .add(invoiceIdValue)
                .setMultipliedLeading(1.5f)
                .setTextAlignment(TextAlignment.LEFT);

        Paragraph orderDate = new Paragraph()
                .add(orderDateLabel)
                .add(orderDateValue)
                .setMultipliedLeading(1.5f)
                .setTextAlignment(TextAlignment.LEFT);

        Cell orderInfoCell = new Cell().setTextAlignment(TextAlignment.LEFT).setBorder(null);
        orderInfoCell.add(orderId);
        orderInfoCell.add(orderDate);
        orderInfoCell.add(invoiceId);

        orderDetailsAndAddressTable.addCell(orderInfoCell);

        Cell billingAddress = new Cell().setTextAlignment(TextAlignment.RIGHT).setBorder(null);
        billingAddress.add(new Paragraph("Billing Address").setFont(boldFont).setFixedLeading(15));
        billingAddress.add(new Paragraph(invoiceData.getBillingAddress().getName()).setFixedLeading(14));
        billingAddress.add(new Paragraph(invoiceData.getBillingAddress().getStreet()).setFixedLeading(14));
        billingAddress.add(
                new Paragraph(invoiceData.getBillingAddress()
                        .getCity() + ", " + invoiceData.getBillingAddress()
                        .getZip() + ", " + invoiceData.getBillingAddress()
                        .getZip())
                        .setFixedLeading(14));
        billingAddress.add(new Paragraph(invoiceData.getBillingAddress().getCountry()).setFixedLeading(14));
        orderDetailsAndAddressTable.addCell(billingAddress);

        Cell emptyCell = new Cell().setTextAlignment(TextAlignment.LEFT).setBorder(null);
        orderDetailsAndAddressTable.addCell(emptyCell);

        Cell shippingAddress = new Cell().setTextAlignment(TextAlignment.RIGHT).setBorder(null);
        shippingAddress.add(new Paragraph("Shipping Address").setFont(boldFont).setFixedLeading(15));
        shippingAddress.add(new Paragraph(invoiceData.getShippingAddress().getName()).setFixedLeading(14));
        shippingAddress.add(new Paragraph(invoiceData.getShippingAddress().getStreet()).setFixedLeading(14));
        shippingAddress.add(
                new Paragraph(invoiceData.getShippingAddress()
                        .getCity() + ", " + invoiceData.getShippingAddress()
                        .getZip() + ", " + invoiceData.getShippingAddress()
                        .getZip())
                        .setFixedLeading(14));
        shippingAddress.add(new Paragraph(invoiceData.getShippingAddress().getCountry()).setFixedLeading(14));
        orderDetailsAndAddressTable.addCell(shippingAddress);
        document.add(orderDetailsAndAddressTable);
    }

    private void addProductTable(Document document, InvoiceData invoiceData) throws IOException {
        PdfFont boldFont = PdfFontFactory.createFont("Helvetica-Bold");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        Table productTable = new Table(new float[]{1, 2, 1, 1, 1, 1});
        productTable.setMarginTop(10f);
        productTable.setWidth(UnitValue.createPercentValue(100));

        productTable.addHeaderCell(new Cell().add(
                new Paragraph("Sl. No").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        productTable.addHeaderCell(new Cell().add(
                new Paragraph("Description").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        productTable.addHeaderCell(new Cell().add(
                new Paragraph("Unit Price").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        productTable.addHeaderCell(new Cell().add(
                new Paragraph("Quantity").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        productTable.addHeaderCell(new Cell().add(
                new Paragraph("Tax Amount").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        productTable.addHeaderCell(new Cell().add(
                new Paragraph("Total Amount").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        int counter = 1;
        for (ProductFinalDto product : invoiceData.getProducts()) {
            productTable.addCell(new Cell().add(new Paragraph(counter + "")));
            productTable.addCell(new Cell().add(new Paragraph(product.getProductName())));
            productTable.addCell(new Cell().add(
                    new Paragraph("$" + decimalFormat.format(product.getProductPrice()))));
            productTable.addCell(new Cell().add(new Paragraph(String.valueOf(product.getProductQuantity()))));
            productTable.addCell(
                    new Cell().add(
                            new Paragraph("$" + decimalFormat.format((product.getProductPrice() * 0.07f) )
                    ))
            );
            productTable.addCell(new Cell().add(
                    new Paragraph("$" + decimalFormat.format(product.getProductPrice() + (product.getProductPrice() * 0.07f))
                    )
            ));
            counter++;
        }

        Cell totalLabelCell = new Cell(1,4);
        Cell totalTaxValueCell = new Cell(1,1);
        Cell totalOrderValueCell = new Cell(1,1);

        totalLabelCell.add(new Paragraph("TOTAL:" ).setFont(boldFont));
        totalTaxValueCell.add(new Paragraph("$"+ decimalFormat.format(invoiceData.getTotalTax())).setFont(boldFont));
        totalOrderValueCell.add(new Paragraph( "$"+
                decimalFormat.format(invoiceData.getTotalOrderValue())).setFont(boldFont)
        );

        productTable.addCell(totalLabelCell);
        productTable.addCell(totalTaxValueCell);
        productTable.addCell(totalOrderValueCell);

        Cell amountInWordsLabelCell = new Cell(1,2).setTextAlignment(TextAlignment.LEFT);
        Cell amountInWordsValueCell = new Cell(1,4).setTextAlignment(TextAlignment.RIGHT);

        amountInWordsLabelCell.add(new Paragraph("Amount in Words:" ).setFont(boldFont));
        amountInWordsValueCell.add(new Paragraph("Bla bla bla").setFont(boldFont));

        productTable.addCell(amountInWordsLabelCell);
        productTable.addCell(amountInWordsValueCell);


        document.add(productTable);
    }

    private void addSummary(Document document, InvoiceData invoiceData) throws IOException {
        PdfFont boldFont = PdfFontFactory.createFont("Helvetica-Bold");
        Paragraph summaryTitle = new Paragraph("Summary")
                .setFontSize(12)
                .setFont(boldFont)
                .setUnderline();
        document.add(summaryTitle);

        Table summaryTable = new Table(new float[]{1, 1});
        summaryTable.setWidth(UnitValue.createPercentValue(100));

        summaryTable.addCell(new Cell().add(new Paragraph("Total Tax").setFont(boldFont)));
        summaryTable.addCell(new Cell().add(new Paragraph("$" + invoiceData.getTotalTax())));
        summaryTable.addCell(new Cell().add(new Paragraph("Total Order Value").setFont(boldFont)));
        summaryTable.addCell(new Cell().add(new Paragraph("$" + invoiceData.getTotalOrderValue())));

        document.add(summaryTable);
    }

    private void addFooter(Document document) {
        Paragraph footer = new Paragraph("Thank you for shopping with us!")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10)
                .setMarginTop(10f)
                .setFontColor(ColorConstants.GRAY);
        document.add(footer);
    }
}
