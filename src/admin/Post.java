/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import config.DatabaseConnection;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import user.CreatePost;

public class Post extends javax.swing.JFrame {

    public Post() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("S1M Sports Cars - Manage Posts");
        loadPosts();
    }

    private void loadPosts() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT p.id, p.username, p.car_brand || ' ' || p.car_model AS car, " +
                         "p.description, " +
                         "(SELECT COUNT(*) FROM likes WHERE post_id = p.id) as like_count, " +
                         "(SELECT COUNT(*) FROM comments WHERE post_id = p.id) as comment_count " +
                         "FROM posts p ORDER BY p.created_at DESC";

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);
            model.setColumnIdentifiers(new String[]{"ID", "USER", "CAR", "LIKES", "COMMENTS", "DELETE"});

            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("car"),
                    "❤️ " + rs.getInt("like_count"),
                    "💬 " + rs.getInt("comment_count"),
                    "🗑️ DELETE"
                });
            }

            if (model.getRowCount() == 0)
                model.addRow(new Object[]{"—", "No posts yet", "—", "—", "—", "—"});

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        // Add click listener for DELETE column
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = jTable1.getSelectedRow();
                int col = jTable1.getSelectedColumn();
                if (col == 5) { // DELETE column
                    int id = (int) jTable1.getValueAt(row, 0);
                    int confirm = JOptionPane.showConfirmDialog(null,
                        "Delete post ID " + id + "?", "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        deletePost(id);
                    }
                }
            }
        });
    }

    private void deletePost(int postId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Delete related likes and comments first
            conn.createStatement().execute("DELETE FROM likes WHERE post_id = " + postId);
            conn.createStatement().execute("DELETE FROM comments WHERE post_id = " + postId);
            conn.createStatement().execute("DELETE FROM posts WHERE id = " + postId);
            JOptionPane.showMessageDialog(this, "Post deleted successfully!");
            loadPosts();
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
        jButton4 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(null);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "USER", "CAR", "LIKES", "COMMENT", "DEL"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 110, 1000, 580);

        jButton4.setText("Return...");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);
        jButton4.setBounds(870, 700, 130, 20);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("MANAGE POST");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(10, 30, 210, 50);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1016, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.dispose();
        Dashboard db = new Dashboard();
        db.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Post.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Post.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Post.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Post.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Post().setVisible(true);
            }
        });
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
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
