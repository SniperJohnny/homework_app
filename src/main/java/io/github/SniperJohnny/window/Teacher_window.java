package io.github.SniperJohnny.window;

import javax.swing.*;
import java.awt.*;

public class Teacher_window extends JFrame {

    public Teacher_window() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Teacher Window");
        this.setSize(800, 600);
        this.setLayout(null);

        // Define shared dimensions so everything matches perfectly
        int fieldWidth = 250;
        int fieldHeight = 400; // Total window width / 2 = 400
        int componentWidth = 200;
        int componentHeight = 40;

        int centeredX = fieldHeight - (componentWidth/2);

        JTextField hi = new JTextField("Hi, Welcome");
        hi.setBounds(centeredX, 150, componentWidth, componentHeight);
        hi.addActionListener(e -> {
            this.dispose();
        });
        this.add(hi);

        this.setVisible(true);
    }
}
