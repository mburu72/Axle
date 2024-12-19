package com.tumi.haul.service.safcom;

import com.tumi.haul.model.primitives.Amount;
import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.transactions.Transaction;
import com.tumi.haul.model.transactions.TransactionRequest;
import com.tumi.haul.repository.TransactionRepository;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class IntaSend {
    private static final Logger log = LoggerFactory.getLogger(IntaSend.class);
private final TransactionRepository transactionRepository;

    public IntaSend(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public String initiatePayment(TransactionRequest transactionRequest) throws IOException {
        Transaction transaction = new Transaction();
        transaction.setAmount(new Amount(new BigDecimal(transactionRequest.amount)));
        transaction.setJobId(transactionRequest.getJobId());
        transaction.setPhoneNumber(new PhoneNumber(transactionRequest.phoneNumber));
        String amount = transaction.getAmount().toString();
        String phoneNumber = transaction.getPhoneNumber().getInternationalFormat().substring(1);
        String api_key = "ISSecretKey_test_ac7bd171-0b07-463f-8317-6d025de7f016";
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"amount\":\"" + amount + "\",\"phone_number\":\"" + phoneNumber + "\"}");
        Request request = new Request.Builder()
                .url("https://sandbox.intasend.com/api/v1/payment/mpesa-stk-push/")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("Authorization", "Bearer " + api_key)
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                transactionRepository.save(transaction);
                return response.message();
            } else {

                String errorBody = response.body() != null ? response.body().string() : "No response body";
                String errorMessage = parseErrorResponse(errorBody);
                throw new PaymentInitiationException("API Error: " + errorMessage + " Response: " + errorBody);
            }
        } catch (IOException e) {
            throw new PaymentInitiationException("Network or server error occurred while initiating payment");
        }
    }

    private String parseErrorResponse(String errorBody) {

            try {
                JSONObject errorJson = new JSONObject(errorBody);

                if (errorJson.has("errors")) {
                    JSONArray errors = errorJson.getJSONArray("errors");

                    if (errors.length() > 0) {
                        JSONObject firstError = errors.getJSONObject(0);
                        String code = firstError.optString("code", "unknown_error");
                        String detail = firstError.optString("detail", "No details provided");
                        return String.format("Code: %s, Detail: %s", code, detail);
                    }
                }
            } catch (Exception e) {

                return "Failed to parse error response: " + errorBody;
            }

            return "Unknown API error occurred";

    }

    public static class PaymentInitiationException extends IOException {
        public PaymentInitiationException(String message) {
            super(message);
        }
    }
}
