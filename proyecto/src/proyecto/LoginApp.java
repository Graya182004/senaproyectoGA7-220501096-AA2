/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

// Clase principal de la aplicación de login y gestión de usuarios
public class LoginApp {
    // URL de conexión a la base de datos
    private static final String DB_URL = "jdbc:mysql://localhost:3306/proyecto";
    // Usuario y contraseña de la base de datos
    private static final String USER = "root";
    private static final String PASS = "";

    public static void main(String[] args) {
        // Crear y configurar la ventana de login
        JFrame frame = new JFrame("Login");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Crear panel y añadir componentes de login
        JPanel panel = new JPanel();
        frame.add(panel);
        placeLoginComponents(panel, frame);

        // Hacer visible la ventana de login
        frame.setVisible(true);
    }

    // Método para colocar los componentes del login en el panel
    private static void placeLoginComponents(JPanel panel, JFrame frame) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Correo:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        JButton registerButton = new JButton("Registro");
        registerButton.setBounds(180, 80, 85, 25);
        panel.add(registerButton);

        // Acción para el botón de login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String correo = userText.getText();
                String contraseña = new String(passwordText.getPassword());

                if (authenticateUser(correo, contraseña)) {
                    JOptionPane.showMessageDialog(panel, "Login exitoso!");
                    frame.dispose();
                    showUserManagementWindow();
                } else {
                    JOptionPane.showMessageDialog(panel, "Correo o contraseña incorrectos.");
                }
            }
        });

        // Acción para el botón de registro
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame registerFrame = new JFrame("Registro");
                registerFrame.setSize(300, 250);
                registerFrame.setLocationRelativeTo(null);

                JPanel registerPanel = new JPanel();
                registerFrame.add(registerPanel);
                placeRegisterComponents(registerPanel, registerFrame);

                registerFrame.setVisible(true);
            }
        });
    }

    // Método para colocar los componentes del registro en el panel
    private static void placeRegisterComponents(JPanel panel, JFrame frame) {
        panel.setLayout(null);

        JLabel nameLabel = new JLabel("Nombre:");
        nameLabel.setBounds(10, 20, 80, 25);
        panel.add(nameLabel);

        JTextField nameText = new JTextField(20);
        nameText.setBounds(100, 20, 165, 25);
        panel.add(nameText);

        JLabel emailLabel = new JLabel("Correo:");
        emailLabel.setBounds(10, 50, 80, 25);
        panel.add(emailLabel);

        JTextField emailText = new JTextField(20);
        emailText.setBounds(100, 50, 165, 25);
        panel.add(emailText);

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setBounds(10, 80, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 80, 165, 25);
        panel.add(passwordText);

        JButton registerButton = new JButton("Registrar");
        registerButton.setBounds(100, 120, 100, 25);
        panel.add(registerButton);

        // Acción para el botón de registrar
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nameText.getText();
                String correo = emailText.getText();
                String contraseña = new String(passwordText.getPassword());

                if (registerUser(nombre, correo, contraseña)) {
                    JOptionPane.showMessageDialog(panel, "Registro exitoso!");
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(panel, "Error al registrar. Inténtalo nuevamente.");
                }
            }
        });
    }

    // Método para autenticar el usuario
    private static boolean authenticateUser(String correo, String contraseña) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT * FROM usuarios WHERE correo = ? AND contraseña = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, correo);
            pstmt.setString(2, contraseña);
            rs = pstmt.executeQuery();

            return rs.next(); // Devuelve true si hay una coincidencia

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para registrar un nuevo usuario
    private static boolean registerUser(String nombre, String correo, String contraseña) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO usuarios (nombre, correo, contraseña) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, correo);
            pstmt.setString(3, contraseña);
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0; // Devuelve true si se insertó un nuevo usuario

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para mostrar la ventana de gestión de usuarios
    private static void showUserManagementWindow() {
        JFrame managementFrame = new JFrame("Gestión de Usuarios");
        managementFrame.setSize(600, 400);
        managementFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managementFrame.setLocationRelativeTo(null);

        JPanel managementPanel = new JPanel();
        managementPanel.setLayout(null);
        managementFrame.add(managementPanel);

        String[] columnNames = {"ID", "Nombre", "Correo"};
        DefaultTableModel model = new DefaultTableModel(new Object[0][0], columnNames);
        JTable userTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBounds(20, 20, 550, 200);
        managementPanel.add(scrollPane);

        loadUsers(model);

        JButton createButton = new JButton("Crear");
        createButton.setBounds(20, 240, 80, 25);
        managementPanel.add(createButton);

        JButton editButton = new JButton("Editar");
        editButton.setBounds(120, 240, 80, 25);
        managementPanel.add(editButton);

        JButton deleteButton = new JButton("Eliminar");
        deleteButton.setBounds(220, 240, 80, 25);
        managementPanel.add(deleteButton);

        // Acción para el botón de crear usuario
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserForm(null, model);
            }
        });

        // Acción para el botón de editar usuario
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    String id = model.getValueAt(selectedRow, 0).toString();
                    String nombre = model.getValueAt(selectedRow, 1).toString();
                    String correo = model.getValueAt(selectedRow, 2).toString();
                    showUserForm(new String[]{id, nombre, correo}, model);
                } else {
                    JOptionPane.showMessageDialog(managementPanel, "Selecciona un usuario para editar.");
                }
            }
        });

        // Acción para el botón de eliminar usuario
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    String id = model.getValueAt(selectedRow, 0).toString();
                    deleteUser(id, model);
                } else {
                    JOptionPane.showMessageDialog(managementPanel, "Selecciona un usuario para eliminar.");
                }
            }
        });

        // Hacer visible la ventana de gestión de usuarios
        managementFrame.setVisible(true);
    }

    // Método para cargar los usuarios en la tabla
    private static void loadUsers(DefaultTableModel model) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT id, nombre, correo FROM usuarios";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("nombre"), rs.getString("correo")});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para mostrar el formulario de creación/edición de usuario
    private static void showUserForm(String[] userData, DefaultTableModel model) {
        JFrame userFrame = new JFrame(userData == null ? "Crear Usuario" : "Editar Usuario");
        userFrame.setSize(300, 200);
        userFrame.setLocationRelativeTo(null);

        JPanel userPanel = new JPanel();
        userPanel.setLayout(null);
        userFrame.add(userPanel);

        JLabel nameLabel = new JLabel("Nombre:");
        nameLabel.setBounds(10, 20, 80, 25);
        userPanel.add(nameLabel);

        JTextField nameText = new JTextField(20);
        nameText.setBounds(100, 20, 165, 25);
        userPanel.add(nameText);

        JLabel emailLabel = new JLabel("Correo:");
        emailLabel.setBounds(10, 50, 80, 25);
        userPanel.add(emailLabel);

        JTextField emailText = new JTextField(20);
        emailText.setBounds(100, 50, 165, 25);
        userPanel.add(emailText);

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setBounds(10, 80, 80, 25);
        userPanel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 80, 165, 25);
        userPanel.add(passwordText);

        JButton saveButton = new JButton("Guardar");
        saveButton.setBounds(100, 120, 100, 25);
        userPanel.add(saveButton);

        // Si se están editando datos de usuario, cargar los datos en los campos
        if (userData != null) {
            nameText.setText(userData[1]);
            emailText.setText(userData[2]);
            passwordText.setEnabled(false); // No permitir cambiar la contraseña en la edición
        }

        // Acción para el botón de guardar usuario
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nameText.getText();
                String correo = emailText.getText();
                String contraseña = new String(passwordText.getPassword());

                if (userData == null) { // Crear usuario
                    if (registerUser(nombre, correo, contraseña)) {
                        model.setRowCount(0); // Limpiar la tabla
                        loadUsers(model);
                        userFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(userPanel, "Error al crear usuario.");
                    }
                } else { // Editar usuario
                    String id = userData[0];
                    if (updateUser(id, nombre, correo)) {
                        model.setRowCount(0); // Limpiar la tabla
                        loadUsers(model);
                        userFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(userPanel, "Error al editar usuario.");
                    }
                }
            }
        });

        // Hacer visible el formulario de usuario
        userFrame.setVisible(true);
    }

    // Método para actualizar un usuario existente
    private static boolean updateUser(String id, String nombre, String correo) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "UPDATE usuarios SET nombre = ?, correo = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, correo);
            pstmt.setString(3, id);
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0; // Devuelve true si se actualizó el usuario

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para eliminar un usuario
    private static void deleteUser(String id, DefaultTableModel model) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "DELETE FROM usuarios WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                model.setRowCount(0); // Limpiar la tabla
                loadUsers(model);
                JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar usuario.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
