package io.github.SniperJohnny.window;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Window extends JFrame {

    private JTextField enter_name_field;
    private JPasswordField enter_password_field;

    public Window() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Homework reward system");
        this.setSize(800, 600);
        this.setLayout(null);
        // Define shared dimensions so everything matches perfectly
        int fieldWidth = 250;
        int fieldHeight = 400; // Total window width / 2 = 400
        int componentWidth = 200;
        int componentHeight = 40;

        int centeredX = fieldHeight - (componentWidth/2);

        //JTextField for user input
        JLabel login_label = new JLabel("Login für Hausaufgabenbelohnung");
        login_label.setBounds(centeredX, 80, componentWidth, componentHeight);
        enter_name_field = new JTextField();
        enter_name_field.setBounds(centeredX, 150, componentWidth, componentHeight);
        addPlaceholder(enter_name_field, "Enter Name");

        enter_password_field = new JPasswordField();
        enter_password_field.setBounds(centeredX, 220, componentWidth, componentHeight);
        addPasswordPlaceholder(enter_password_field, "Enter Password");

        //our_Button
        JButton button = new JButton("Login");
        button.setBounds(centeredX,300, componentWidth, componentHeight);

        this.add(button);
        this.add(login_label);
        this.add(enter_name_field);
        this.add(enter_password_field);

        button.addActionListener(e -> handleLogin());
        enter_name_field.addActionListener(e -> handleLogin());
        enter_password_field.addActionListener(e -> handleLogin());

        this.setVisible(true);
    }

    private void closeWindow(String username, int points, String role) {
        this.dispose();
        new Teacher_window(username, points, role);
    }

    private void handleLogin() {
        String username = enter_name_field.getText();
        String password = new String(enter_password_field.getPassword());

        if (username.equals("Enter Name")) username = "";
        if (password.equals("Enter Password")) password = "";

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Login Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String hashedInputPassword = hashSHA256(password);

        String url = "";
        String dbUser = "";
        String dbPass = "";

        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
            url = prop.getProperty("db.url");
            dbUser = prop.getProperty("db.user");
            dbPass = prop.getProperty("db.password");

            System.out.println("--- FILE FOUND! ---");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not find file in: " + System.getProperty("user.dir"), "Configuration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "SELECT id, username, role , points FROM user WHERE LOWER(username) = LOWER(?) AND password_hash = ?";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, hashedInputPassword);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    System.out.println("Login successful! Role: " + role);
                    int points = resultSet.getInt("points");

                    closeWindow(username, points, role);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    enter_password_field.setText("");
                    enter_password_field.requestFocus();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String hashSHA256(String data) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // --- Helper Method for Normal Text Field Placeholder ---
    private void addPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    // --- Helper Method for Password Field Placeholder ---
    private void addPasswordPlaceholder(JPasswordField field, String placeholder) {
        field.setText(placeholder);
        field.setEchoChar((char) 0); // Show text normally while it's a placeholder
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                String currentText = new String(field.getPassword());
                if (currentText.equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('*'); // Turn on password hiding
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String currentText = new String(field.getPassword());
                if (currentText.isEmpty()) {
                    field.setText(placeholder);
                    field.setEchoChar((char) 0); // Turn off password hiding for placeholder
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }
    public static void register() {

    }
}