package com.icapp.icapp.Helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EuropeanCarPlateValidator {
    private static Map<String, String> countryRegexMap;

    static {
        // Define regex patterns for each country
        countryRegexMap = new HashMap<>();
//        countryRegexMap.put("AL", "^[A-Z]{1,2}[0-9]{1,4}[A-Z]{1,2}$"); // Albania
//        countryRegexMap.put("AD", "^[0-9]{4}[A-Z]{2}$"); // Andorra
//        countryRegexMap.put("AM", "^[0-9]{2}[A-Z]{2}[0-9]{2}$"); // Armenia
//        countryRegexMap.put("AT", "^[A-Z]{1,2}[0-9]{1,4}$"); // Austria
//        countryRegexMap.put("AZ", "^[A-Z]{2}[0-9]{2}[A-Z]{2}$"); // Azerbaijan
//        countryRegexMap.put("BY", "^[0-9]{2}[A-Z]{2}[0-9]{4}$"); // Belarus
//        countryRegexMap.put("BE", "^[0-9]{1,2}-[A-Z]{1,3}-[0-9]{1,3}$"); // Belgium
//        countryRegexMap.put("BA", "^[A-Z]{2}[0-9]{2}[A-Z]{2}$"); // Bosnia and Herzegovina
//        countryRegexMap.put("BG", "^[A-Z]{2}[0-9]{4}[A-Z]{2}$"); // Bulgaria
//        countryRegexMap.put("HR", "^[A-Z]{2}[0-9]{2}-[A-Z]{1,2}$"); // Croatia
//        countryRegexMap.put("CY", "^[A-Z]{2}[0-9]{4}$"); // Cyprus
//        countryRegexMap.put("CZ", "^[0-9]{2}[A-Z]{1,2}[0-9]{2,4}$"); // Czech Republic
//        countryRegexMap.put("DK", "^[0-9]{2}[A-Z]{1,2}[0-9]{1,2}$"); // Denmark
//        countryRegexMap.put("EE", "^[0-9]{2}[A-Z]{1}[0-9]{2,3}$"); // Estonia
//        countryRegexMap.put("FI", "^[A-Z]{1,3}-[0-9]{1,3}$"); // Finland
//        countryRegexMap.put("FR", "^[A-Z0-9]{2}-[0-9]{3}-[A-Z]{2}$"); // France
//        countryRegexMap.put("GE", "^[A-Z]{2}[0-9]{2}[A-Z]{2}$"); // Georgia
//        countryRegexMap.put("DE", "^[A-Z]{1,3}-[0-9]{1,4}$"); // Germany
//        countryRegexMap.put("GR", "^[A-Z]{2}[0-9]{3}[A-Z]{1,2}$"); // Greece
//        countryRegexMap.put("HU", "^[A-Z]{3}-[0-9]{3}$"); // Hungary
//        countryRegexMap.put("IS", "^[A-Z]{2}[0-9]{3}$"); // Iceland
//        countryRegexMap.put("IE", "^[0-9]{2}[A-Z]{1,2}[0-9]{1,4}$"); // Ireland
//        countryRegexMap.put("IT", "^[A-Z]{2}[0-9]{2}[A-Z]{1,2}$"); // Italy
//        countryRegexMap.put("KZ", "^[0-9]{3}[A-Z]{2}[0-9]{2}$"); // Kazakhstan
//        countryRegexMap.put("XK", "^[0-9]{2}[A-Z]{2}[0-9]{2}$"); // Kosovo
//        countryRegexMap.put("LV", "^[A-Z]{2}[0-9]{2}[A-Z]{1}[0-9]{4}$"); // Latvia
//        countryRegexMap.put("LI", "^[A-Z]{2}[0-9]{2}[A-Z]{2}$"); // Liechtenstein
//        countryRegexMap.put("LT", "^[0-9]{2}[A-Z]{1,2}[0-9]{2,4}$"); // Lithuania
//        countryRegexMap.put("LU", "^[A-Z]{1}[0-9]{3}[A-Z]{2}$"); // Luxembourg
//        countryRegexMap.put("MT", "^[A-Z]{3}[0-9]{4}$"); // Malta
//        countryRegexMap.put("MD", "^[A-Z]{2}[0-9]{2}[A-Z]{1,2}$"); // Moldova
//        countryRegexMap.put("MC", "^[0-9]{2}[A-Z]{1,2}[0-9]{2}$"); // Monaco
//        countryRegexMap.put("ME", "^[A-Z]{2}[0-9]{3}[A-Z]{1}$"); // Montenegro
//        countryRegexMap.put("NL", "^[0-9]{2}-[A-Z]{2}-[0-9]{2}$"); // Netherlands
//        countryRegexMap.put("MK", "^[A-Z]{1,2}[0-9]{2}[A-Z]{2}$"); // North Macedonia
//        countryRegexMap.put("NO", "^[A-Z]{2}[0-9]{2}[A-Z]{1,2}$"); // Norway
//        countryRegexMap.put("PL", "^[A-Z]{1,3}-[0-9]{1,4}$"); // Poland
//        countryRegexMap.put("PT", "^[0-9]{2}-[0-9]{2}-[A-Z]{2}$"); // Portugal
//        countryRegexMap.put("RO", "^[A-Z]{1}[0-9]{2}-[A-Z]{3}$"); // Romania
//        countryRegexMap.put("RU", "^[0-9]{1,3}[A-Z]{1,2}[0-9]{2,3}$"); // Russia
//        countryRegexMap.put("SM", "^[A-Z]{2}[0-9]{2}$"); // San Marino
//        countryRegexMap.put("RS", "^[A-Z]{2}[0-9]{3,4}$"); // Serbia
//        countryRegexMap.put("SK", "^[0-9]{2}[A-Z]{2}[0-9]{2}$"); // Slovakia
//        countryRegexMap.put("SI", "^[A-Z]{2}[0-9]{2}[A-Z]{1}[0-9]{1}$"); // Slovenia
//        countryRegexMap.put("ES", "^[0-9]{4}-[A-Z]{3}$"); // Spain
//        countryRegexMap.put("SE", "^[A-Z]{1,3}[0-9]{1,3}$"); // Sweden
//        countryRegexMap.put("CH", "^[A-Z]{2}[0-9]{1,4}$"); // Switzerland
//        countryRegexMap.put("TR", "^[A-Z]{1,2}-[0-9]{2,4}$"); // Turkey
//        countryRegexMap.put("UA", "^[A-Z]{2}[0-9]{5}$"); // Ukraine
//        countryRegexMap.put("GB", "^[A-Z]{2}[0-9]{2}[A-Z]{3}$"); // United Kingdom
//        countryRegexMap.put("VA", "^[0-9]{2}-[A-Z]{2}$"); // Vatican City
        // Note: Add patterns for other countries as needed
    }

    public static boolean isValidEuropeanCarPlate(String carPlate) {
//        for (String regex : countryRegexMap.values()) {
//            // Create a Pattern object
//            Pattern pattern = Pattern.compile(regex);
//
//            // Create a matcher object
//            Matcher matcher = pattern.matcher(carPlate);
//
//            // Return true if the car plate matches the pattern
//            if (matcher.matches()) {
//                return true;
//            }
//        }
//        return false;
//
        // Create a Pattern object
        Pattern pattern = Pattern.compile("^[A-Z0-9- ]{4,10}$");

        // Create a matcher object
        Matcher matcher = pattern.matcher(carPlate);

        // Return true if the car plate matches the pattern
        return matcher.matches();
    }

    public static boolean isValidTicketNumber(String ticketNumber) {
        Pattern pattern = Pattern.compile(".*\\d{4,}.*");
        Matcher matcher = pattern.matcher(ticketNumber);
        return matcher.matches();
    }
}