package io.github.SniperJohnny.window;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.regex.Pattern;

public class Teacher_window extends JFrame {
    private JTextField points_inserter = new JTextField();

    private JTextField name_chooser = new JTextField();

    public Teacher_window(String username, int points, String role) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLayout(null);

        // Define shared dimensions so everything matches perfectly
        int fieldWidth = 250;
        int fieldHeight = 400; // Total window width / 2 = 400
        int componentWidth = 200;
        int componentHeight = 40;

        int centeredX = fieldHeight - (componentWidth/2);
        JLabel hello_text = new JLabel("Herzlich Willkommen: " + username +".");
        JLabel points_text = new JLabel("Du hast " + points + " Punkte");
        JButton button = new JButton("give Points");
        hello_text.setBounds(centeredX, 80, componentWidth, componentHeight);
        points_text.setBounds(centeredX, 150, componentWidth, componentHeight);
        name_chooser.setBounds(centeredX, 150, componentWidth, componentHeight);
        points_inserter.setBounds(centeredX, 220, componentWidth, componentHeight);
        points_inserter.addActionListener(e -> {
            handle_point_giving();
        });
        name_chooser.addActionListener(e -> {
            handle_point_giving();
        });
        button.setBounds(centeredX, 290, componentWidth, componentHeight);
        JButton ha_free_voucher = new JButton("Hausaufgabenfrei Gutschein für 500");
        ha_free_voucher.setBounds(275, 290, 250, componentHeight);
        ha_free_voucher.addActionListener(e -> {
            this.dispose();
            new Ha_free_voucher();

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

            String query = "UPDATE user SET points = points + ? WHERE LOWER(username) = LOWER(?)";

            try (Connection connection = DriverManager.getConnection(url, dbUser, dbPass);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, -500);
                statement.setString(2, username);

                statement.executeUpdate(); // Führt das Update auf der Datenbank aus!

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Datenbankfehler beim Einlösen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        button.addActionListener( e -> {
            handle_point_giving();
        });

        this.add(hello_text);

        if(role.equals("student")) {
            this.add(points_text);
            this.add(ha_free_voucher);
            this.setTitle("Student Window");
        } else if(role.equals("teacher")) {
            this.add(name_chooser);
            this.add(points_inserter);
            this.add(button);
            this.setTitle("Teacher Window");
        }
        ((AbstractDocument) points_inserter.getDocument()).setDocumentFilter(new DocumentFilter() {
            private final Pattern pattern = Pattern.compile("\\d*");

            @Override
            public void insertString(FilterBypass fb, int offset, String string,
                                     AttributeSet attr) throws BadLocationException {
                if (string != null && pattern.matcher(string).matches()) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
                                AttributeSet attrs) throws BadLocationException {
                if (text != null && pattern.matcher(text).matches()) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        this.setVisible(true);



}
    private void handle_point_giving() {
        int points_to_give;
        String student_name = name_chooser.getText();
        if(points_inserter.getText().isEmpty() || student_name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte Name und Punkte eingeben.", "Fehler", JOptionPane.WARNING_MESSAGE);
            return;
        } else if(!is_number(points_inserter.getText())) {
            return;
        }
        points_to_give = string_to_int_converter(points_inserter.getText());
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

        String query = "UPDATE user SET points = points + ? WHERE LOWER(username) = LOWER(?)";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, points_to_give);
            statement.setString(2, student_name);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Punkte erfolgreich vergeben an " + student_name + "!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                points_inserter.setText("");
                name_chooser.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Schüler '" + student_name + "' wurde nicht gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Datenbankfehler: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private boolean is_number(String str) {
        if (str.isEmpty() || str.isEmpty()) {
            System.out.println("pls insert name or amount of points");
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    private int string_to_int_converter(String str) {
        if(!is_number(str)) {
            throw new IllegalArgumentException("String is not numeric");
        }
        return Integer.parseInt(str);

    }
    public static void register() {

    }
}
