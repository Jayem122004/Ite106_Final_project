import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainApp extends JFrame implements ActionListener {
    private JButton computeButton, historyButton, exitButton;
    private ArrayList<String> history = new ArrayList<>();  // To store history

    public MainApp() {
        setTitle("Main App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(300, 200);

        // Compute button: Opens the calculator
        computeButton = new JButton("Compute");
        computeButton.addActionListener(this);
        add(computeButton);

        // History button: Displays the history of calculations
        historyButton = new JButton("History");
        historyButton.addActionListener(this);
        add(historyButton);

        // Exit button: Closes the application
        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        add(exitButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Compute")) {
            // Open the calculator
            new Calculator(history);
            this.setVisible(false); // Hide the main app window when the calculator opens
        } else if (command.equals("History")) {
            // Show history in a dialog box
            StringBuilder historyText = new StringBuilder("History:\n");
            for (String h : history) {
                historyText.append(h).append("\n");
            }
            JOptionPane.showMessageDialog(this, historyText.toString(), "Calculation History", JOptionPane.INFORMATION_MESSAGE);
        } else if (command.equals("Exit")) {
            // Exit the application
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new MainApp();
    }
}

class Calculator extends JFrame implements ActionListener {
    private JTextField display;
    private double num1, num2, result;
    private String operator = "";
    private boolean startNewNumber = true;
    private ArrayList<String> history;  // Reference to the history list

    public Calculator(ArrayList<String> history) {
        this.history = history;  // Pass history to the calculator
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(300, 400);

        // Create the display area (input bar)
        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.PLAIN, 30));
        display.setPreferredSize(new Dimension(300, 70));
        add(display, BorderLayout.NORTH);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4));

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
            if (startNewNumber) {
                display.setText(command); 
                startNewNumber = false;
            } else {
                display.setText(display.getText() + command);
            }
        } else if (command.equals("=")) {
            if (!display.getText().isEmpty()) {
                num2 = Double.parseDouble(display.getText());
                calculate();
                startNewNumber = true;
                history.add(display.getText());  // Store the result in history
            }
        } else if (command.equals("C")) {
            display.setText("");
            num1 = num2 = result = 0;
            operator = "";
            startNewNumber = true;
        } else if (command.equals("Del")) {
            String currentText = display.getText();
            if (currentText.length() > 0) {
                display.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else {
            if (!operator.isEmpty()) {
                num2 = Double.parseDouble(display.getText());
                calculate();
            }
            if (!display.getText().isEmpty()) {
                num1 = Double.parseDouble(display.getText());
            }
            operator = command;
            display.setText("");
            startNewNumber = true;
        }
    }

    private void calculate() {
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
        }

        display.setText(String.valueOf(result));
        operator = "";
        num1 = result;
    }
}
