package com.example.sudoku.com.example.sudoku.model;

import java.util.HashMap;

class CoordinatesList {

    private HashMap<String, int[]> coordinates;

    CoordinatesList() {
        coordinates = new HashMap<>();
    }

    void addCoordinate(int r, int c) {
        String value = String.format("%d%d", r, c);
        coordinates.putIfAbsent(value, new int[]{r, c});
    }

    boolean removeCoordinate(int r, int c) {
        String value = String.format("%d%d", r, c);
        if (!coordinates.containsKey(value))
            return false;
        coordinates.remove(value);
        return true;
    }

    boolean contains(int r, int c) {
        return coordinates.containsKey(String.format("%d%d", r, c));
    }
}
