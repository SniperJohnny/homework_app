package io.github.SniperJohnny.window;

import javax.swing.*;

public class Ha_free_voucher extends JFrame {
    Ha_free_voucher() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Teacher Window");
        this.setSize(800, 600);

        int fieldWidth = 250;
        int fieldHeight = 400; // Total window width / 2 = 400
        int componentWidth = 200;
        int componentHeight = 40;

        int centeredX = fieldHeight - (componentWidth/2);

        ImageIcon image = new ImageIcon(getClass().getResource("/ha_voucher.jpg"));
        JLabel label = new JLabel(image);
        label.setBounds(centeredX, 200, componentWidth, componentHeight);

        this.add(label);


        this.setVisible(true);
    }
    public static void register() {

    }
}
