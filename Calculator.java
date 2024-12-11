import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame implements ActionListener {
    private JTextField display;
    private double num1, num2, result;
    private String operator = "";  // Initialize operator as an empty string
    private boolean startNewNumber = true; // Flag to check when to start new number input

    public Calculator() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(300, 400);

        // Create the display area (input bar) and set its font and size
        display = new JTextField();
        display.setEditable(false);  // Prevent direct input
        display.setFont(new Font("Arial", Font.PLAIN, 30)); // Increase font size
        display.setPreferredSize(new Dimension(300, 70)); // Adjust the height and width
        add(display, BorderLayout.NORTH);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4)); // 5x4 grid for calculator buttons (including Del and C)

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C", "Del"
        };

        // Add buttons to the panel and set action listeners
        for (String button : buttons) {
            JButton b = new JButton(button);
            b.addActionListener(this);
            buttonPanel.add(b);
        }

        // Add the button panel to the frame
        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ((Character.isDigit(command.charAt(0)) || command.equals("."))) {
            // If it's a digit or dot, append it to the current number
            if (startNewNumber) {
                display.setText(command); // Start a new number
                startNewNumber = false;
            } else {
                display.setText(display.getText() + command); // Append to the current number
            }
        } else if (command.equals("=")) {
            // When "=" is pressed, calculate the result and display it
            if (!display.getText().isEmpty()) {
                num2 = Double.parseDouble(display.getText());
                calculate(); // Perform the calculation
                startNewNumber = true; // After calculation, prepare for a new number
            }
        } else if (command.equals("C")) {
            // Clear everything
            display.setText("");
            num1 = num2 = result = 0;
            operator = "";
            startNewNumber = true;
        } else if (command.equals("Del")) {
            // Delete the last input character
            String currentText = display.getText();
            if (currentText.length() > 0) {
                display.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else { // It's an operator (+, -, *, /)
            // Only set operator if there is a valid number entered
            if (!operator.isEmpty()) { // If an operator was already set, perform the previous calculation
                num2 = Double.parseDouble(display.getText());
                calculate();
            }
            if (!display.getText().isEmpty()) {
                num1 = Double.parseDouble(display.getText()); // Store the first number
            }
            operator = command; // Set the new operator
            display.setText(""); // Clear display to start typing num2
            startNewNumber = true; // Set flag to start new number after operator
        }
    }

    private void calculate() {
        // Perform the calculation based on the operator
        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    display.setText("Error");
                    return;
                }
                break;
            default:
                return;
        }

        // Display the result only
        display.setText(String.valueOf(result));
        operator = ""; // Clear the operator after calculation
        num1 = result; // Keep the result as the first number for the next operation
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
