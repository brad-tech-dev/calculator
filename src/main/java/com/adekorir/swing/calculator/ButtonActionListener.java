package com.adekorir.swing.calculator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An action listener for the calculator buttons.
 * <p>
 * This class implements the {@link ActionListener} interface
 * and provides a concrete implementation of a button action.
 * <p>
 * It parses the button title to determine if it is a number
 * button or a command button.
 * <p>
 * Using that information, it then passes the value to the
 * {@link CalculatorListener calculatorListener} for either
 * command purposes or listening purposes.
 *
 * @author Adams 'Sadam' Korir
 * @see java.awt.event.ActionListener
 */
public class ButtonActionListener implements ActionListener {

    private String title;
    private CalculatorListener calculatorListener;

    public ButtonActionListener(String title, CalculatorListener listener) {
        super();

        this.title = title;
        this.calculatorListener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int num = Integer.parseInt(title);
            this.calculatorListener.listen(num);
        } catch (NumberFormatException ex) {
            this.calculatorListener.command(title);
        }
    }

}
