package rubrica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditorPersona extends JDialog {
    private JTextField nome;
    private JTextField cognome;
    private JTextField indirizzo;
    private JTextField telefono;
    private JTextField eta;
    private boolean ok;

    public EditorPersona(JFrame parent, Persona p) {
        super(parent, "Editor Persona", true);
        setSize(300, 200);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel dataPanel = new JPanel(new GridLayout(6, 2));
        add(dataPanel, BorderLayout.CENTER);

        dataPanel.add(new JLabel("Nome"));
        nome = new JTextField(p.getNome());
        dataPanel.add(nome);

        dataPanel.add(new JLabel("Cognome"));
        cognome = new JTextField(p.getCognome());
        dataPanel.add(cognome);

        dataPanel.add(new JLabel("Indirizzo"));
        indirizzo = new JTextField(p.getIndirizzo());
        dataPanel.add(indirizzo);

        dataPanel.add(new JLabel("Telefono"));
        telefono = new JTextField(p.getTelefono());
        dataPanel.add(telefono);

        dataPanel.add(new JLabel("Età"));
        eta = new JTextField(String.valueOf(p.getEta()));
        dataPanel.add(eta);

        JToolBar toolbar = new JToolBar();
        add(toolbar, BorderLayout.SOUTH);

        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton salva = new JButton("Salva");
        toolbar.add(salva);
        salva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nome.getText().isEmpty() || cognome.getText().isEmpty() || indirizzo.getText().isEmpty()
                        || telefono.getText().isEmpty() || eta.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(EditorPersona.this,
                            "Per salvare è necessario prima compilare tutti i campi.", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    p.setNome(nome.getText());
                    p.setCognome(cognome.getText());
                    p.setIndirizzo(indirizzo.getText());
                    p.setTelefono(telefono.getText());
                    p.setEta(Integer.parseInt(eta.getText()));
                    ok = true;
                    dispose();
                }
            }
        });

        toolbar.addSeparator();

        JButton annulla = new JButton("Annulla");
        toolbar.add(annulla);
        annulla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok = false;
                dispose();
            }
        });
    }

    public boolean isOk() {
        return ok;
    }
}
