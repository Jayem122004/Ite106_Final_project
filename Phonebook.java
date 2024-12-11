import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class PhonebookSystem extends JFrame implements ActionListener {
    private JTextField nameField, phoneField;
    private JTextArea displayArea;
    private String filename = "phonebook.txt";
    private Map<String, String> phonebook;

    public PhonebookSystem() {
        setTitle("Phonebook System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 14));
        phoneField = new JTextField(15);
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(phoneLabel);
        inputPanel.add(phoneField);
        add(inputPanel, BorderLayout.NORTH);

        displayArea = new JTextArea(10, 30);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 14));
        displayArea.setEditable(false);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addButton = new JButton("Add");
        styleButton(addButton);
        addButton.addActionListener(this);

        JButton searchButton = new JButton("Search");
        styleButton(searchButton);
        searchButton.addActionListener(this);

        JButton deleteButton = new JButton("Delete");
        styleButton(deleteButton);
        deleteButton.addActionListener(this);

        JButton listButton = new JButton("List");
        styleButton(listButton);
        listButton.addActionListener(this);

        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(listButton);
        add(buttonPanel, BorderLayout.SOUTH);

        phonebook = new HashMap<>();
        loadContacts();
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(72, 133, 237));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 40));
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Add")) {
            addContact();
        } else if (command.equals("Search")) {
            searchContact();
        } else if (command.equals("Delete")) {
            deleteContact();
        } else if (command.equals("List")) {
            listContacts();
        }
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        if (!name.isEmpty() && !phone.isEmpty()) {
            phonebook.put(name, phone);
            saveContacts();
            displayArea.append("Added: " + name + " - " + phone + "\n");
            nameField.setText("");
            phoneField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Name and Phone cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchContact() {
        String name = nameField.getText();
        if (phonebook.containsKey(name)) {
            String phone = phonebook.get(name);
            displayArea.append("Found: " + name + " - " + phone + "\n");
        } else {
            displayArea.append("Contact not found: " + name + "\n");
        }
    }

    private void deleteContact() {
        String name = nameField.getText();
        if (phonebook.containsKey(name)) {
            phonebook.remove(name);
            saveContacts();
            displayArea.append("Deleted: " + name + "\n");
        } else {
            displayArea.append("Contact not found: " + name + "\n");
        }
    }

    private void listContacts() {
        displayArea.setText("");
        if (phonebook.isEmpty()) {
            displayArea.append("No contacts found.\n");
        } else {
            displayArea.append("Contacts List:\n");
            for (Map.Entry<String, String> entry : phonebook.entrySet()) {
                displayArea.append(entry.getKey() + " - " + entry.getValue() + "\n");
            }
        }
    }

    private void loadContacts() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] contact = line.split(",");
                if (contact.length == 2) {
                    phonebook.put(contact[0], contact[1]);
                }
            }
        } catch (IOException e) {
        }
    }

    private void saveContacts() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<String, String> entry : phonebook.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving contacts.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new PhonebookSystem();
    }
}
