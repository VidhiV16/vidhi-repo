package com.example.webhook;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class WebhookApp implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringApplication.run(WebhookApp.class, args);
    }

    @Override
    public void run(String... args) {
        registerAndSubmit();
    }

    private void registerAndSubmit() {
        try {
            // Step 1: Register webhook
            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            Map<String, String> request = new HashMap<>();
            request.put("name", "Your Full Name");   // 🔹 Replace with your details
            request.put("regNo", "REG12347");        // 🔹 Replace with your regNo
            request.put("email", "you@example.com"); // 🔹 Replace with your email

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            String webhookUrl = (String) response.getBody().get("webhook");
            String accessToken = (String) response.getBody().get("accessToken");

            // Step 2: Solve SQL question (for Question 2 in your case)
            String finalQuery = solveSqlProblem("47"); // last two digits of regNo

            // Step 3: Submit solution
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            Map<String, String> body = new HashMap<>();
            body.put("finalQuery", finalQuery);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            restTemplate.postForEntity(webhookUrl, entity, String.class);

            System.out.println("✅ Query submitted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String solveSqlProblem(String lastTwoDigits) {
        // Your final SQL query for Question 2
        return "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, " +
               "COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT " +
               "FROM EMPLOYEE e1 " +
               "JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID " +
               "LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT " +
               "AND e2.DOB > e1.DOB " +
               "GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME " +
               "ORDER BY e1.EMP_ID DESC;";
    }
}
