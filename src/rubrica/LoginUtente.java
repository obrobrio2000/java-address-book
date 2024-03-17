package rubrica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUtente extends JFrame {
    private JTextField username;
    private JPasswordField password;

    public LoginUtente() {
        super("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel(new GridLayout(2, 2));
        add(loginPanel, BorderLayout.CENTER);

        loginPanel.add(new JLabel("Utente"));
        username = new JTextField();
        loginPanel.add(username);

        loginPanel.add(new JLabel("Password"));
        password = new JPasswordField();
        loginPanel.add(password);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(toolbar, BorderLayout.SOUTH);

        JButton login = new JButton("Login");
        toolbar.add(login);
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = username.getText();
                String pass = new String(password.getPassword());
                if (checkLogin(user, pass)) {
                    dispose();
                    new Rubrica().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(LoginUtente.this, "Login errato", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public boolean checkLogin(String username, String password) {
        return username.equals(System.getProperty("user.name")) && password.equals("admin");
    }
}
