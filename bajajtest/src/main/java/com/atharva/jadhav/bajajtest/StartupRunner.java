package com.atharva.jadhav.bajajtest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            Map<String, String> body = new HashMap<>();
            body.put("name", "Atharva Dhanaji Jadhav");
            body.put("regNo", "ADT24SOCBD028");
            body.put("email", "atharvajadhav591@gmail.com");

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body);

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(url, request, Map.class);

            System.out.println("RESPONSE:");
            System.out.println(response.getBody());

            String webhookUrl = (String) response.getBody().get("webhook");
            String accessToken = (String) response.getBody().get("accessToken");

            String finalQuery = "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT FROM EMPLOYEE e1 JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT AND e2.DOB > e1.DOB GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME ORDER BY e1.EMP_ID DESC;";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> submitBody = new HashMap<>();
            submitBody.put("finalQuery", finalQuery);

            HttpEntity<Map<String, String>> submitRequest =
                    new HttpEntity<>(submitBody, headers);

            ResponseEntity<String> submitResponse =
                    restTemplate.postForEntity(webhookUrl, submitRequest, String.class);

            System.out.println("FINAL RESPONSE:");
            System.out.println(submitResponse.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}