package com.SoftwareDesign.BeautySalon.email;

import com.SoftwareDesign.BeautySalon.dto.AppointmentDTO;
import com.SoftwareDesign.BeautySalon.dto.BeautyServiceDTO;
import com.codingerror.model.AddressDetails;
import com.codingerror.model.HeaderDetails;
import com.codingerror.model.Product;
import com.codingerror.model.ProductTableHeader;
import com.codingerror.service.CodingErrorPdfInvoiceCreator;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GenerateInvoice {
    private static int countInvoice = 0;
    private int invoiceId;
    private boolean saleApplied = false;
    public GenerateInvoice(AppointmentDTO appointmentDTO) throws FileNotFoundException {
        countInvoice++;
        this.invoiceId = countInvoice;

        String pdfName = "Bill_number_" + countInvoice + ".pdf";

        CodingErrorPdfInvoiceCreator cepdf = new CodingErrorPdfInvoiceCreator(pdfName);
        cepdf.createDocument();

        HeaderDetails header = new HeaderDetails();
        header.setInvoiceNo(countInvoice + "")
                .setInvoiceDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .setInvoiceTitle("Glam Haven Bill")
                .build();
        cepdf.createHeader(header);

        AddressDetails addressDetails = new AddressDetails();
        addressDetails.setBillingCompany("Glam Haven")
                .setBillingNameText("Employee name")
                .setBillingName(appointmentDTO.getEmployeeDTO().getName())
                .setBillingAddressText("Beauty Field")
                .setBillingAddress(appointmentDTO.getEmployeeDTO().getEmployeeType().toString())
                .setShippingInfoText("Client Information")
                .setShippingName(appointmentDTO.getClientDTO().getName())
                .setShippingAddressText("Email")
                .setShippingAddress(appointmentDTO.getClientDTO().getUserName())
                .setBillingEmailText("Date&Time")
                .setBillingEmail(appointmentDTO.getDateTime().toString());

        cepdf.createAddress(addressDetails);

        ProductTableHeader productTableHeader = new ProductTableHeader();
        cepdf.createTableHeader(productTableHeader);

        List<Product> beautyServices = new ArrayList<>();

        for(BeautyServiceDTO beautyService: appointmentDTO.getBeautyServicesDTO()) {
            beautyServices.add(new Product(beautyService.getName(), 1, beautyService.getPrice().floatValue()));
        }
        System.out.println(beautyServices);
        beautyServices = cepdf.modifyProductList(beautyServices);
        cepdf.createProduct(beautyServices);


        List<String> TncList=new ArrayList<>();

        if(BigDecimal.valueOf(cepdf.getTotalSum(beautyServices)).subtract(appointmentDTO.getTotalPrice()).compareTo(BigDecimal.valueOf(1.5d)) > 0) {
            TncList.add("IMPORTANT: THIS APPOINTMENT HAS A SALE APPLIED, THE FINAL PRICE OF THE APPOINTMENT IS: " + appointmentDTO.getTotalPrice());
            this.saleApplied = true;
        }

        TncList.add("1. Late Arrivals: If you arrive late for your appointment, we will do our best to accommodate you, but your appointment may be shortened or rescheduled.");
        TncList.add("2.Age Requirement: Children under the age of 18 must be accompanied by an adult at all times while in the salon.");

        String imagePath="src/main/resources/logo.png";
        cepdf.createTnc(TncList,false,imagePath);

        System.out.println("pdf genrated");
    }

    public static int getCountInvoice() {
        return countInvoice;
    }

    public int getInvoiceId() {
        return this.invoiceId;
    }

    public boolean getSaleApplied() {
        return this.saleApplied;
    }
}
