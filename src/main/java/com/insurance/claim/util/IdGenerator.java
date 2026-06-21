package com.insurance.claim.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class IdGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private IdGenerator() {
    }

    public static String generateClaimNumber() {
        String datePart = LocalDateTime.now().format(DATE_FORMATTER);
        String randomPart = String.format("%06d", random.nextInt(1000000));
        return "CL" + datePart + randomPart;
    }

    public static String generatePolicyNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = String.format("%08d", random.nextInt(100000000));
        return "POL" + datePart + randomPart;
    }

    public static String generateSurveyNumber() {
        String datePart = LocalDateTime.now().format(DATE_FORMATTER);
        String randomPart = String.format("%04d", random.nextInt(10000));
        return "SV" + datePart + randomPart;
    }

    public static String generateSupplementNumber() {
        String datePart = LocalDateTime.now().format(DATE_FORMATTER);
        String randomPart = String.format("%04d", random.nextInt(10000));
        return "SP" + datePart + randomPart;
    }

    public static String generateConclusionNumber() {
        String datePart = LocalDateTime.now().format(DATE_FORMATTER);
        String randomPart = String.format("%04d", random.nextInt(10000));
        return "CC" + datePart + randomPart;
    }

    public static String generateClueNumber() {
        String datePart = LocalDateTime.now().format(DATE_FORMATTER);
        String randomPart = String.format("%04d", random.nextInt(10000));
        return "BL" + datePart + randomPart;
    }

    public static String generateCallbackId() {
        return "CB" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
