package com.SoftwareDesign.BeautySalon.saleCode;

import java.util.Random;

public class SalesCodeGenerator {
    public String generateSalesCode() {
        Random random = new Random();
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int codeLength = 10;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(alphabet.length());
            sb.append(alphabet.charAt(index));
        }

        return sb.toString();
    }
}
