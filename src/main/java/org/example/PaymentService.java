package org.example;

import java.util.HashMap;
import java.util.Map;

public class PaymentService {
    private Map<Integer, String> paymentRecords;

    public PaymentService() {
        paymentRecords = new HashMap<>();
    }

    public void savePaymentRecord(int paymentId, String cardNumber) {
        paymentRecords.put(paymentId, cardNumber);
    }
}