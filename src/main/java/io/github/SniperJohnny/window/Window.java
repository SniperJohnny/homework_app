package io.github.SniperJohnny.window;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;


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
        this.add(enter_name_field);
        this.add(enter_password_field);

        button.addActionListener(e -> handleLogin());
        enter_name_field.addActionListener(e -> handleLogin());
        enter_password_field.addActionListener(e -> handleLogin());


        this.setVisible(true);
    }
    private void closeWindow() {
        this.dispose();
        new Teacher_window();
    }
    private void handleLogin() {

            String username = enter_name_field.getText();
            String password = new String(enter_password_field.getPassword());

            // Ignore placeholder values on submission
            if (username.equals("Enter Name")) username = "";
            if (password.equals("Enter Password")) password = "";

            System.out.println("Login clicked for: " + username);
            if (password.equalsIgnoreCase("test")) {
                closeWindow();
                System.out.println("Credentials correct. Transitioning windows...");
                System.out.println("it should be closing");

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
}
