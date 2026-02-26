/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import config.DatabaseConnection;
import config.UserSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mainpage.loginform;
import user.CreatePost;


/**
 *
 * @author LAGARBE
 */
public class users extends javax.swing.JFrame {

    private DefaultTableModel model;

    public users() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("S1M Sports Cars - Manage Users");
        styleComponents();
        loadUserData("");
    }

    // ── Style buttons ────────────────────────────────────────────────────────
    private void styleComponents() {
        // ADD - green
        jButton6.setBackground(new java.awt.Color(50, 160, 50));
        jButton6.setForeground(java.awt.Color.WHITE);
        jButton6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jButton6.setFocusPainted(false);
        jButton6.setBorderPainted(false);

        // EDIT - blue
        jButton2.setBackground(new java.awt.Color(50, 100, 200));
        jButton2.setForeground(java.awt.Color.WHITE);
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jButton2.setFocusPainted(false);
        jButton2.setBorderPainted(false);

        // DELETE - red
        jButton3.setBackground(new java.awt.Color(200, 50, 50));
        jButton3.setForeground(java.awt.Color.WHITE);
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jButton3.setFocusPainted(false);
        jButton3.setBorderPainted(false);

        // REFRESH - gray
        jButton5.setBackground(new java.awt.Color(90, 90, 90));
        jButton5.setForeground(java.awt.Color.WHITE);
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jButton5.setFocusPainted(false);
        jButton5.setBorderPainted(false);

        // SEARCH - dark gray
        jButton7.setBackground(new java.awt.Color(70, 70, 70));
        jButton7.setForeground(java.awt.Color.WHITE);
        jButton7.setFont(new java.awt.Font("Tahoma", 1, 11));
        jButton7.setFocusPainted(false);
        jButton7.setBorderPainted(false);

        // Return - gray
        jButton4.setBackground(new java.awt.Color(80, 80, 80));
        jButton4.setForeground(java.awt.Color.WHITE);
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jButton4.setFocusPainted(false);
        jButton4.setBorderPainted(false);

        // Table header styling
        jTable1.getTableHeader().setBackground(new java.awt.Color(30, 30, 30));
        jTable1.getTableHeader().setForeground(new java.awt.Color(255, 215, 0));
        jTable1.getTableHeader().setFont(new java.awt.Font("Tahoma", 1, 12));
        jTable1.setRowHeight(28);
        jTable1.setSelectionBackground(new java.awt.Color(200, 50, 50, 180));
        jTable1.setSelectionForeground(java.awt.Color.WHITE);
        jTable1.setGridColor(new java.awt.Color(200, 200, 200));
    }

    // ── Load users ───────────────────────────────────────────────────────────
    public void loadUserData(String search) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql;
            PreparedStatement pst;

            if (search.isEmpty()) {
                sql = "SELECT us_id, username, email, role, status FROM tbl_user ORDER BY us_id ASC";
                pst = conn.prepareStatement(sql);
            } else {
                sql = "SELECT us_id, username, email, role, status FROM tbl_user " +
                      "WHERE CAST(us_id AS TEXT) LIKE ? OR username LIKE ? ORDER BY us_id ASC";
                pst = conn.prepareStatement(sql);
                pst.setString(1, "%" + search + "%");
                pst.setString(2, "%" + search + "%");
            }

            model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);
            model.setColumnIdentifiers(new String[]{"User ID", "Username", "Email", "Role", "Status"});

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("us_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("status")
                });
            }
            rs.close();
            pst.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    // ── Delete user ──────────────────────────────────────────────────────────
    private void deleteUser() {
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pumili muna ng user sa table!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId      = (int)    model.getValueAt(row, 0);
        String username = (String) model.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete user '" + username + "'?\nAll post will be deleted!",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Delete likes + comments ng posts ng user
            ResultSet rs = conn.prepareStatement(
                "SELECT id FROM posts WHERE user_id = " + userId).executeQuery();
            while (rs.next()) {
                int postId = rs.getInt("id");
                conn.createStatement().execute("DELETE FROM likes    WHERE post_id = " + postId);
                conn.createStatement().execute("DELETE FROM comments WHERE post_id = " + postId);
            }
            conn.createStatement().execute("DELETE FROM posts    WHERE user_id = " + userId);
            conn.createStatement().execute("DELETE FROM tbl_user WHERE us_id   = " + userId);

            JOptionPane.showMessageDialog(this, "User '" + username + "' deleted successfully!");
            loadUserData(jTextField1.getText().trim());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(null);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "User Id", "User Name", "Email", "Role", "Status"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 100, 1000, 580);

        jButton2.setText("EDIT");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(120, 70, 100, 23);

        jButton3.setText("DELETE");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);
        jButton3.setBounds(230, 70, 100, 23);

        jButton4.setText("Return...");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);
        jButton4.setBounds(870, 700, 130, 20);

        jButton6.setText("ADD");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6);
        jButton6.setBounds(10, 70, 100, 23);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField1);
        jTextField1.setBounds(740, 60, 250, 30);

        jButton5.setText("REFRESH");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5);
        jButton5.setBounds(340, 70, 100, 23);

        jButton7.setText("SEARCH");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton7);
        jButton7.setBounds(630, 60, 100, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1016, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       this.dispose();
       Dashboard db = new Dashboard();
       db.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
         Form form = new Form(-1, this);
    form.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int row = jTable1.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Pumili muna ng user sa table!", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    int userId = (int) model.getValueAt(row, 0);
    Form form = new Form(userId, this);
    form.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    
        deleteUser();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
         jTextField1.setText("");
        loadUserData("");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
     loadUserData(jTextField1.getText().trim());
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        loadUserData(jTextField1.getText().trim());
    }//GEN-LAST:event_jTextField1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(users.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new users().setVisible(true));
   java.awt.EventQueue.invokeLater(() -> {
        if (config.UserSession.getInstance().getUserId() == 0) {
            JOptionPane.showMessageDialog(null,
                "⚠️ Please log in first!",
                "Login Required",
                JOptionPane.WARNING_MESSAGE);
            new mainpage.loginform().setVisible(true);
        } else {
            new CreatePost().setVisible(true);
        }
    });
}
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}
