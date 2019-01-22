package com.adekorir.swing.calculator;

import java.util.ArrayList;

class CalculatorMemory extends ArrayList<String> {

    private String title;

    CalculatorMemory() {
        this("UNNAMED");
    }

    CalculatorMemory(String title) {
        super();
        this.title = title;
    }

    @Override
    public boolean add(String s) {
        System.out.println("Adding " + s + " to " + title);
        return super.add(s);
    }
}
