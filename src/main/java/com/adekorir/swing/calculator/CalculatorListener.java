package com.adekorir.swing.calculator;

/**
 * @author Admin
 */
public interface CalculatorListener {
    String
            MEMORY_CLEAR = "MC",
            MEMORY_ADD = "M+",
            MEMORY_MINUS = "M-",
            MEMORY_RECALL = "MR",
            CLEAR = "C",
            DIVIDE = "/",
            TIMES = "X",
            ADD = "+",
            MINUS = "-",
            POSITIVE_NEGATIVE = "+/-",
            EQUAL = "=",
            POINT = ".";

    void listen(int num);

    void command(String command);
}
