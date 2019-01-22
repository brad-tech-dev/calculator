package com.adekorir.swing.calculator;

import javax.swing.*;
import javax.swing.plaf.metal.MetalBorders;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Main Application
 */
public class Main extends JFrame implements CalculatorListener {

    private final String ERROR = "E";
    private final JLabel lblOutput;
    private CalculatorMemory mainMemory, cacheMemory;
    private CalcProcessor calcProcessor;

    {
        lblOutput = new JLabel("0", SwingConstants.RIGHT);
        mainMemory = new CalculatorMemory("Main Memory");
        cacheMemory = new CalculatorMemory("Cache Memory");
        calcProcessor = new CalcProcessor();
    }

    public Main() {
        super("Calculator");


        // display label info
        lblOutput.setFont(new Font("Monospaced", 1, 24));
        lblOutput.setBorder(new MetalBorders.TextFieldBorder());


        initGUI();
    }

    public static void main(String[] args) {
        final Main main = new Main();
        try {
            // set the logo
            Image icon = new ImageIcon(Main.class.getResource("brad-logo.png").toURI().toURL()).getImage();
            main.setIconImage(icon);
        } catch (MalformedURLException | URISyntaxException ex) {
            System.err.println("Error loading window icon");
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            main.setVisible(true);
        });

    }

    private void initGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridBagLayout());

        // set up constraints
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.ipadx = 10;
        constraints.ipady = 15;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        this.getContentPane().add(lblOutput, constraints);

        constraints.insets = new Insets(0, 0, 0, 0);

        // set up the rest of the buttons
        final String[][] pad = {
                {MEMORY_CLEAR, MEMORY_ADD, MEMORY_MINUS, MEMORY_RECALL},
                {CLEAR, POSITIVE_NEGATIVE, DIVIDE, TIMES},
                {"7", "8", "9", MINUS},
                {"4", "5", "6", ADD},
                {"1", "2", "3", EQUAL},
                {"0", "", POINT, ""},
        };

        for (int row = 0; row < pad.length; row++) {
            for (int col = 0; col < pad[row].length; col++) {
                final String TITLE = pad[row][col];

                if (!TITLE.isEmpty()) {
                    if (TITLE.equals("0")) {
                        constraints.gridwidth = 2;
                    } else {
                        constraints.gridheight = 1;
                        constraints.gridwidth = 1;
                    }

                    if (TITLE.equals("=")) {
                        constraints.gridheight = 2;
                    }

                    constraints.gridx = col;
                    constraints.gridy = row + 1;

                    JButton button = new JButton(TITLE);
                    button.addActionListener(new ButtonActionListener(TITLE, this));
                    this.getContentPane().add(button, constraints);
                }
            }
        }

        this.pack();
    }

    @Override
    public void listen(int digit) {
        String currentText = lblOutput.getText().trim();
        // replace error with 0
        if (currentText.equals(ERROR)) {
            currentText = "0";
        }
        try {
            int val = Integer.parseInt(currentText);
            // if it is a valid number
            if (val == 0) {
                // just replace the contents with the new number even if it is 0
                lblOutput.setText("" + digit);
            } else {
                // append the digit to the number
                lblOutput.setText("" + val + "" + digit);
            }
        } catch (NumberFormatException ex) {
            System.out.println("Double value: " + currentText);
            // test for doubles
            try {
                double doubleVal = Double.parseDouble(currentText);
                // append the digit to the number
                // fixme: improve this piece of code to avoid examples like { 123.0 -> 123.00 -> 123.04... }
                if (currentText.endsWith(".0") && digit != 0) {
                    // replace the last .0 with the new digit to make a valid number
                    currentText = currentText.substring(0, currentText.length() - 1) + digit;
                    lblOutput.setText(currentText);
                } else {
                    lblOutput.setText("" + doubleVal + "" + digit);
                }
            } catch (NumberFormatException nfe) {
                // else just refuse to append the new num
                lblOutput.setText(currentText);
            }
        }
    }

    @Override
    public void command(String command) {
        String currentText = lblOutput.getText().trim();
        if (currentText.equals(ERROR)) {
            currentText = "0";
        }
        switch (command) {
            case ADD:
            case MINUS:
            case DIVIDE:
            case TIMES:
                addToMemory(currentText, command);
                break;
            case EQUAL:
                // add last value first before calculation
                addToMemory(currentText, ADD);
                calculateMemory(cacheMemory);
                // clear memory after for new calculation
                cacheMemory.clear();
                break;
            case POINT:
                addDot();
                break;
            case MEMORY_MINUS:
            case MEMORY_ADD:
                addToMemory(currentText, command);
                break;
            case MEMORY_RECALL:
                calculateMemory(mainMemory);
                break;
            case MEMORY_CLEAR:
                mainMemory.clear();
            case CLEAR:
            default:
                lblOutput.setText("0");
                break;
        }
    }

    private void addToMemory(String currentText, String op) {
        if (op.equals(MEMORY_ADD) || op.equals(MEMORY_MINUS)) {
            if (op.equals(MEMORY_ADD)) {
                if (!mainMemory.isEmpty()) {
                    mainMemory.add("+");
                }
                mainMemory.add(currentText);
            }

            if (op.equals(MEMORY_MINUS)) {
                mainMemory.add("-");
                mainMemory.add(currentText);
            }
        } else {
            String operation = op.equals(TIMES) ? "*" : op;

            if (cacheMemory.isEmpty()) {
                if (operation.equals(MINUS)) cacheMemory.add(operation);
            } else {
                cacheMemory.add(operation);
            }
            cacheMemory.add(currentText);
        }

        lblOutput.setText("0");
    }

    private void calculateMemory(CalculatorMemory memory) {
        if (memory.size() > 0) {
            final StringBuilder memoryString = new StringBuilder();
            memory.forEach(memoryString::append);

            try {
                String result = calcProcessor.process(memoryString.toString());
                lblOutput.setText(result);
            } catch (ProcessException ex) {
                lblOutput.setText(ERROR);
            }
        } else {
            lblOutput.setText("0");
        }
    }

    private void addDot() {
        String currentText = lblOutput.getText().trim();
        try {
            String dotText = currentText + ".";
            System.out.println(dotText);
            double num = Double.parseDouble(dotText);
            System.out.println(num + "");
            // if it gets to this point, the dot can be accepted
            lblOutput.setText("" + num);
        } catch (NumberFormatException nfe) {
            System.err.println(nfe.getMessage());
            // not a valid number just ignore the new dot
            lblOutput.setText(currentText);
        }
        System.out.println("DONE with DOT");
    }
}
