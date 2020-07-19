package de.heinerion.invoice.view.swing.home.receiver;

import javax.swing.*;

public class CompanyCreateDialog {
    private final JButton button;

    public CompanyCreateDialog() {
        button= new JButton("+");
        button.addActionListener(a -> System.out.println("new Company"));
    }

    public JButton getButton() {
        return button;
    }
}
