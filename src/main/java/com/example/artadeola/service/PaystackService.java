

package com.example.artadeola.service;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
@Service
public class PaystackService {
    @Value("${PAYSTACK_SECRET_KEY:}")
    private String secretKey;



    @PostConstruct
    public void checkPaystackKey() {
        System.out.println("Paystack key loaded: "
                + (secretKey != null && !secretKey.isBlank()));

        System.out.println("Paystack key prefix: "
                + (secretKey == null ? "NULL"
                : secretKey.substring(0, Math.min(7, secretKey.length()))));

        System.out.println("Paystack key length: "
                + (secretKey == null ? 0 : secretKey.trim().length()));
    }

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> initializePayment(
            String email, long amountInKobo) {

        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException(
                    "Paystack secret key is not configured.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Customer email is required.");
        }

        if (amountInKobo <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero.");
        }

        String url =
                "https://api.paystack.co/transaction/initialize";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey.trim());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email.trim());
        requestBody.put("amount", amountInKobo);
        requestBody.put("currency", "NGN");

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(requestBody, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(
                    url, request, Map.class);

            if (response == null ||
                    !Boolean.TRUE.equals(response.get("status"))) {
                throw new IllegalStateException(
                        "Paystack did not initialize the payment.");
            }

            return response;

        } catch (RestClientResponseException e) {
            System.err.println("Paystack HTTP error: "
                    + e.getStatusCode().value());
            System.err.println("Paystack response: "
                    + e.getResponseBodyAsString());

            throw new IllegalStateException(
                    "Paystack rejected the request. HTTP status: "
                            + e.getStatusCode().value(), e);
        }
    }

    public Map<String, Object> verifyPayment(String reference) {

        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException(
                    "Paystack secret key is not configured.");
        }

        if (reference == null || reference.isBlank()) {
            throw new IllegalArgumentException(
                    "Payment reference is required.");
        }

        String url =
                "https://api.paystack.co/transaction/verify/" + reference;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey.trim());

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            Map<String, Object> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    Map.class
            ).getBody();

            if (response == null) {
                throw new IllegalStateException(
                        "Paystack returned an empty verification response.");
            }

            return response;

        } catch (RestClientResponseException e) {
            System.err.println("Paystack verification HTTP error: "
                    + e.getStatusCode().value());
            System.err.println("Paystack response: "
                    + e.getResponseBodyAsString());

            throw new IllegalStateException(
                    "Paystack verification failed. HTTP status: "
                            + e.getStatusCode().value(), e);
        }
    }
}
