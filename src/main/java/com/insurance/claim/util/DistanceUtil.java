package com.insurance.claim.util;

public class DistanceUtil {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private DistanceUtil() {
    }

    public static double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        if (Double.isNaN(lat1) || Double.isNaN(lon1) || Double.isNaN(lat2) || Double.isNaN(lon2)) {
            return Double.MAX_VALUE;
        }

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    public static double calculateDistanceMeters(double lat1, double lon1, double lat2, double lon2) {
        return calculateDistanceKm(lat1, lon1, lat2, lon2) * 1000;
    }

    public static boolean isWithinDistanceKm(double lat1, double lon1, double lat2, double lon2, double maxDistanceKm) {
        return calculateDistanceKm(lat1, lon1, lat2, lon2) <= maxDistanceKm;
    }

    public static boolean isWithinDistanceMeters(double lat1, double lon1, double lat2, double lon2, double maxDistanceMeters) {
        return calculateDistanceMeters(lat1, lon1, lat2, lon2) <= maxDistanceMeters;
    }
}
