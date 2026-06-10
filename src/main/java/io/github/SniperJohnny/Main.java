package io.github.SniperJohnny;

import io.github.SniperJohnny.window.Ha_free_voucher;
import io.github.SniperJohnny.window.Teacher_window;
import io.github.SniperJohnny.window.Window;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Hello and welcome!");

        new Window();
        Teacher_window.register();
        Ha_free_voucher.register();


    }

}