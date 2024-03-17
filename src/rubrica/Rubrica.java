package rubrica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Rubrica extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private ArrayList<Persona> persone;
    private Connection connection;

    public Rubrica() {
        super("Rubrica");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        persone = new ArrayList<>();
        loadFromFile();
        connectToDatabase();

        model = new DefaultTableModel(new String[] { "Nome", "Cognome", "Telefono" }, 0);
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JToolBar toolbar = new JToolBar();
        add(toolbar, BorderLayout.SOUTH);

        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton nuovo = new JButton("Nuovo");
        toolbar.add(nuovo);
        nuovo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Persona p = new Persona("", "", "", "", 0);
                EditorPersona dialog = new EditorPersona(Rubrica.this, p);
                dialog.setVisible(true);
                if (dialog.isOk()) {
                    persone.add(p);
                    model.addRow(new Object[] { p.getNome(), p.getCognome(), p.getTelefono() });
                    saveToFile(p);
                    saveToDatabase(p);
                }
            }
        });

        toolbar.addSeparator();

        JButton modifica = new JButton("Modifica");
        toolbar.add(modifica);
        modifica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = table.getSelectedRow();
                if (index >= 0) {
                    Persona p = persone.get(index);
                    Persona pOld = new Persona(p.getNome(), p.getCognome(), p.getIndirizzo(), p.getTelefono(),
                            p.getEta());
                    EditorPersona dialog = new EditorPersona(Rubrica.this, p);
                    dialog.setVisible(true);
                    if (dialog.isOk()) {
                        deleteFile(pOld);
                        deleteFromDatabase(pOld);
                        model.setValueAt(p.getNome(), index, 0);
                        model.setValueAt(p.getCognome(), index, 1);
                        model.setValueAt(p.getTelefono(), index, 2);
                        saveToFile(p);
                        saveToDatabase(p);
                    }
                } else {
                    JOptionPane.showMessageDialog(Rubrica.this,
                            "Per modificare è necessario prima selezionare una persona.", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        toolbar.addSeparator();

        JButton elimina = new JButton("Elimina");
        toolbar.add(elimina);
        elimina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = table.getSelectedRow();
                if (index >= 0) {
                    Persona p = persone.get(index);
                    int confirm = JOptionPane.showConfirmDialog(Rubrica.this,
                            "Eliminare la persona " + p.getNome() + " " + p.getCognome() + "?", "Conferma",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        persone.remove(index);
                        model.removeRow(index);
                        deleteFile(p);
                        deleteFromDatabase(p);
                    }
                } else {
                    JOptionPane.showMessageDialog(Rubrica.this,
                            "Per eliminare è necessario prima selezionare una persona.", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        for (Persona p : persone) {
            model.addRow(new Object[] { p.getNome(), p.getCognome(), p.getTelefono() });
        }
    }

    private void loadFromFile() {
        File folder = new File("informazioni");
        if (folder.exists()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                try {
                    Scanner scanner = new Scanner(file);
                    String line = scanner.nextLine();
                    String[] data = line.split(";");
                    if (data.length == 5) {
                        String nome = data[0];
                        String cognome = data[1];
                        String indirizzo = data[2];
                        String telefono = data[3];
                        int eta = Integer.parseInt(data[4]);
                        Persona p = new Persona(nome, cognome, indirizzo, telefono, eta);
                        persone.add(p);
                    }
                    scanner.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveToFile(Persona p) {
        try {
            File folder = new File("informazioni");
            if (!folder.exists()) {
                folder.mkdir();
            }
            String fileName = p.getNome() + "-" + p.getCognome() + "-" + shortHashCode(p.getTelefono()) + ".txt";
            File file = new File(folder, fileName);
            PrintStream printStream = new PrintStream(new FileOutputStream(file));
            String line = p.getNome() + ";" + p.getCognome() + ";" + p.getIndirizzo() + ";" + p.getTelefono() + ";"
                    + p.getEta();
            printStream.println(line);
            printStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void deleteFile(Persona p) {
        String fileName = p.getNome() + "-" + p.getCognome() + "-" + shortHashCode(p.getTelefono()) + ".txt";
        File file = new File("informazioni", fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    private void connectToDatabase() {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("credenziali_database.properties"));
            String driver = prop.getProperty("db.driver");
            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.username");
            String pass = prop.getProperty("db.password");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToDatabase(Persona p) {
        try {
            String query = "INSERT INTO persone (nome, cognome, indirizzo, telefono, eta, id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, p.getNome());
            statement.setString(2, p.getCognome());
            statement.setString(3, p.getIndirizzo());
            statement.setString(4, p.getTelefono());
            statement.setInt(5, p.getEta());
            statement.setInt(6, shortHashCode(p.getTelefono()));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteFromDatabase(Persona p) {
        try {
            String query = "DELETE FROM persone WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, shortHashCode(p.getTelefono()));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static short shortHashCode(String str) {
        int strHashCode = str.hashCode();
        short shorterHashCode = (short) (strHashCode % Short.MAX_VALUE);
        return shorterHashCode;
    }

    public static void main(String[] args) {
        new LoginUtente().setVisible(true);
    }
}
