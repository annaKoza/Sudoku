package com.example.sudoku.com.example.sudoku.model;

import java.util.HashMap;

class CoordinatesList {

    private HashMap<String, int[]> coordinates;

    CoordinatesList() {
        coordinates = new HashMap<>();
    }

    void addCoordinate(final Position position) {
        String value = String.format("%d%d", position.getRow(), position.getColumn());
        coordinates.putIfAbsent(value, new int[]{position.getRow(), position.getColumn()});
    }

    boolean removeCoordinate(final Position position) {
        String value = String.format("%d%d", position.getRow(), position.getColumn());
        if (!coordinates.containsKey(value))
            return false;
        coordinates.remove(value);
        return true;
    }

    boolean contains(final Position position) {
        return coordinates.containsKey(String.format("%d%d", position.getRow(), position.getColumn()));
    }
}
