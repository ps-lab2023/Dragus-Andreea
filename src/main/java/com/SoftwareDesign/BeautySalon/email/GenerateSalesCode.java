package com.SoftwareDesign.BeautySalon.email;

import com.SoftwareDesign.BeautySalon.dto.BeautyServiceDTO;
import com.SoftwareDesign.BeautySalon.model.Client;
import com.codingerror.model.AddressDetails;
import com.codingerror.model.HeaderDetails;
import com.codingerror.model.Product;
import com.codingerror.model.ProductTableHeader;
import com.codingerror.service.CodingErrorPdfInvoiceCreator;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GenerateSalesCode {
    private static int countSales = 0;
    private int salesId;
    public GenerateSalesCode(Client client) throws FileNotFoundException {
        countSales++;
        this.salesId = countSales;

        String pdfName = "Sales_number_" + countSales+ ".pdf";

        CodingErrorPdfInvoiceCreator cepdf = new CodingErrorPdfInvoiceCreator(pdfName);
        cepdf.createDocument();

        HeaderDetails header = new HeaderDetails();
        header.setInvoiceNo("")
                .setInvoiceNoText("")
                .setInvoiceDate("")
                .setInvoiceDateText("")
                .setInvoiceTitle("Glam Haven Loyalty Program")
                .build();
        cepdf.createHeader(header);

        AddressDetails addressDetails = new AddressDetails();
        addressDetails.setBillingCompany("")
                .setBillingInfoText("Use this code on your next appointment for a 20% discount:")
                .setBillingCompanyText(client.getSalesCode())
                .setBillingNameText("")
                .setBillingName("")
                .setBillingAddressText("")
                .setBillingAddress("")
                .setShippingInfoText("")
                .setShippingName("")
                .setShippingNameText("")
                .setShippingAddressText("")
                .setShippingAddress("")
                .setBillingEmailText("")
                .setBillingEmail("");

        cepdf.createAddress(addressDetails);




        List<String> TncList=new ArrayList<>();

        String imagePath="src/main/resources/logo.png";
        cepdf.createTnc(TncList,false,imagePath);

        System.out.println("pdf genrated");
    }

    public static int getCountSales() {
        return countSales;
    }

    public int getSalesId() {
        return this.salesId;
    }
}
