package com.cristian.etl;

public class TransformationService {

    public String clean(String input) {
        if (input == null) return "";
        return input.trim();
    }

    public String toUppercase(String input) {
        if (input == null) return "";
        return input.toUpperCase();
    }
}
