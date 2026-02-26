/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import config.DatabaseConnection;
import java.sql.*;
import javax.swing.*;
import user.CreatePost;

public class Form extends javax.swing.JFrame {

    private int editId;       // -1 = ADD mode, may value = EDIT mode
    private users parent;     // para ma-refresh ang table pagkatapos

    // ── ADD mode ─────────────────────────────────────────────────────────────
    public Form(int userId, users parent) {
        this.editId = userId;
        this.parent = parent;
        initComponents();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        if (userId == -1) {
            // ADD mode
            setTitle("Add New User");
            jLabel15.setText("ADD USER");
        } else {
            // EDIT mode — i-load ang existing data
            setTitle("Edit User");
            jLabel15.setText("EDIT USER");
            loadUserData(userId);
            // Sa edit mode, password field ay optional
            jLabel10.setText("Password:");
            // Add hint
            jPasswordField1.setToolTipText("Iwanan ng blank kung ayaw baguhin ang password");
        }
    }

    // ── No-arg constructor (para sa NetBeans design view) ────────────────────
    public Form() {
        this.editId = -1;
        this.parent = null;
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    // ── Load existing user data (EDIT mode) ──────────────────────────────────
    private void loadUserData(int userId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pst = conn.prepareStatement(
                "SELECT * FROM tbl_user WHERE us_id = ?");
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                jTextField2.setText(rs.getString("username"));
                jTextField3.setText(rs.getString("email"));
                // Password — blank, optional i-change
                jPasswordField1.setText("");

                // Set Role ComboBox
                String role = rs.getString("role");
                if (role != null) {
                    if (role.equalsIgnoreCase("Admin")) {
                        jComboBox2.setSelectedItem("Admin");
                    } else {
                        jComboBox2.setSelectedItem("User");
                    }
                }

                // Set Status ComboBox
                String status = rs.getString("status");
                if (status != null) {
                    if (status.equalsIgnoreCase("Decline")) {
                        jComboBox1.setSelectedItem("Decline");
                    } else {
                        jComboBox1.setSelectedItem("Approve");
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading user: " + e.getMessage());
        }
    }

    // ── Save (ADD or EDIT) ───────────────────────────────────────────────────
    private void saveUser() {
        String username = jTextField2.getText().trim();
        String email    = jTextField3.getText().trim();
        String pass     = new String(jPasswordField1.getPassword()).trim();
        String role     = jComboBox2.getSelectedItem().toString().trim();
        String status   = jComboBox1.getSelectedItem().toString().trim();

        // Validation
        if (username.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Username at Email ay required!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (editId == -1 && pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Password ay required sa Add User!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (editId == -1) {
                // ── ADD ──────────────────────────────────────────────────────
                PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO tbl_user (username, email, pass, role, status) VALUES (?,?,?,?,?)");
                pst.setString(1, username);
                pst.setString(2, email);
                pst.setString(3, pass);
                pst.setString(4, role);
                pst.setString(5, status);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "User added successfully!");

            } else {
                // ── EDIT ─────────────────────────────────────────────────────
                if (pass.isEmpty()) {
                    // Hindi baguhin ang password
                    PreparedStatement pst = conn.prepareStatement(
                        "UPDATE tbl_user SET username=?, email=?, role=?, status=? WHERE us_id=?");
                    pst.setString(1, username);
                    pst.setString(2, email);
                    pst.setString(3, role);
                    pst.setString(4, status);
                    pst.setInt(5, editId);
                    pst.executeUpdate();
                } else {
                    // Baguhin din ang password
                    PreparedStatement pst = conn.prepareStatement(
                        "UPDATE tbl_user SET username=?, email=?, pass=?, role=?, status=? WHERE us_id=?");
                    pst.setString(1, username);
                    pst.setString(2, email);
                    pst.setString(3, pass);
                    pst.setString(4, role);
                    pst.setString(5, status);
                    pst.setInt(6, editId);
                    pst.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "User updated successfully!");
            }

            // I-refresh ang parent table
            if (parent != null) parent.loadUserData("");
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving: " + e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(null);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("USER FORM");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(150, 70, 120, 50);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Username:");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(0, 160, 110, 30);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Status:");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(350, 220, 80, 20);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Email:  ");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(0, 210, 110, 30);
        jPanel1.add(jTextField2);
        jTextField2.setBounds(110, 160, 210, 30);
        jPanel1.add(jTextField3);
        jTextField3.setBounds(110, 210, 210, 30);
        jPanel1.add(jPasswordField1);
        jPasswordField1.setBounds(110, 260, 210, 30);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText(" Password:");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(0, 260, 110, 30);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Role:");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(350, 160, 80, 30);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Approve", "Decline" }));
        jPanel1.add(jComboBox1);
        jComboBox1.setBounds(430, 220, 70, 20);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "User ", "Admin", " " }));
        jPanel1.add(jComboBox2);
        jComboBox2.setBounds(430, 170, 70, 20);

        jButton1.setForeground(new java.awt.Color(255, 0, 0));
        jButton1.setText("CANCEL");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(300, 350, 120, 23);

        jButton2.setForeground(new java.awt.Color(51, 204, 0));
        jButton2.setText("SAVE CHANGES");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(150, 350, 120, 23);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        saveUser();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
   public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Form().setVisible(true));
  java.awt.EventQueue.invokeLater(() -> {
        if (config.UserSession.getInstance().getUserId() == 0) {
            JOptionPane.showMessageDialog(null,
                "⚠️ Please log in first before creating a post!",
                "Login Required",
                JOptionPane.WARNING_MESSAGE);
            new mainpage.loginform().setVisible(true);
        } else {
            new CreatePost().setVisible(true);
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
