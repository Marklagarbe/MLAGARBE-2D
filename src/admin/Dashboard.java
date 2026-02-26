/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import config.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import mainpage.loginform;
import user.CreatePost;

public class Dashboard extends javax.swing.JFrame {

    public Dashboard() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("S1M Sports Cars - Admin Dashboard");
        styleComponents();
        loadStats();
        loadRecentPosts();
    }

    private void loadStats() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            ResultSet rs = conn.createStatement()
                .executeQuery("SELECT COUNT(*) as cnt FROM tbl_user");
            if (rs.next()) jLabel20.setText("👥  " + rs.getInt("cnt"));

            rs = conn.createStatement()
                .executeQuery("SELECT COUNT(*) as cnt FROM posts");
            if (rs.next()) jLabel16.setText("📝  " + rs.getInt("cnt"));

            rs = conn.createStatement()
                .executeQuery("SELECT COUNT(*) as cnt FROM likes");
            if (rs.next()) jLabel17.setText("❤️  " + rs.getInt("cnt"));

            rs = conn.createStatement()
                .executeQuery("SELECT COUNT(*) as cnt FROM comments");
            if (rs.next()) jLabel18.setText("💬  " + rs.getInt("cnt"));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void loadRecentPosts() {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql =
            "SELECT p.id, p.username, p.car_brand || ' ' || p.car_model AS car, " +
            "(SELECT COUNT(*) FROM likes WHERE post_id = p.id) as like_count " +
            "FROM posts p ORDER BY p.created_at DESC LIMIT 10";

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        model.setColumnIdentifiers(new String[]{"ID", "USERNAME", "CAR", "LIKES", "DELETE"});

        ResultSet rs = conn.createStatement().executeQuery(sql);
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("car"),
                "❤️  " + rs.getInt("like_count"),
                "🗑️ DELETE"
            });
        }
        if (model.getRowCount() == 0)
            model.addRow(new Object[]{"—", "No posts yet", "—", "—", "—"});

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }

    // DELETE click listener
    // Remove old listeners first
    for (java.awt.event.MouseListener ml : jTable1.getMouseListeners()) {
        if (ml.getClass().isAnonymousClass()) jTable1.removeMouseListener(ml);
    }
    jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent e) {
            int row = jTable1.getSelectedRow();
            int col = jTable1.getSelectedColumn();
            if (col == 4 && row >= 0) {
                Object idObj = jTable1.getValueAt(row, 0);
                if (idObj == null || idObj.toString().equals("—")) return;
                int id = Integer.parseInt(idObj.toString());
                int confirm = JOptionPane.showConfirmDialog(null,
                    "Delete post ID " + id + "? This will also delete its likes and comments.",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deletePost(id);
                }
            }
        }

      });
    
    
}
    
    private void deletePost(int postId) {
    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.createStatement().execute("DELETE FROM likes WHERE post_id = " + postId);
        conn.createStatement().execute("DELETE FROM comments WHERE post_id = " + postId);
        conn.createStatement().execute("DELETE FROM posts WHERE id = " + postId);
        JOptionPane.showMessageDialog(null, "Post deleted successfully!");
        loadStats();
        loadRecentPosts();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}

    private void styleComponents() {
        // Stat labels
        jLabel20.setFont(new Font("Tahoma", Font.BOLD, 22));
        jLabel20.setForeground(new Color(50, 200, 50));
        jLabel20.setHorizontalAlignment(SwingConstants.CENTER);

        jLabel16.setFont(new Font("Tahoma", Font.BOLD, 22));
        jLabel16.setForeground(new Color(50, 100, 255));
        jLabel16.setHorizontalAlignment(SwingConstants.CENTER);

        jLabel17.setFont(new Font("Tahoma", Font.BOLD, 22));
        jLabel17.setForeground(new Color(220, 50, 50));
        jLabel17.setHorizontalAlignment(SwingConstants.CENTER);

        jLabel18.setFont(new Font("Tahoma", Font.BOLD, 18));
        jLabel18.setForeground(new Color(255, 215, 0));
        jLabel18.setHorizontalAlignment(SwingConstants.CENTER);

        // Sidebar buttons
        styleNavBtn(jButton5, true);
        styleNavBtn(jButton2, false);
        styleNavBtn(jButton3, false);
        styleNavBtn(jButton4, false);

        // Logout
        jButton1.setBackground(new Color(200, 50, 50));
        jButton1.setForeground(Color.WHITE);
        jButton1.setFont(new Font("Tahoma", Font.BOLD, 11));
        jButton1.setFocusPainted(false);
        jButton1.setBorderPainted(false);

        // Table
        jTable1.getTableHeader().setBackground(new Color(30, 30, 30));
       jTable1.getTableHeader().setForeground(Color.BLACK);
        jTable1.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
        jTable1.getTableHeader().setPreferredSize(new Dimension(0, 35));
        jTable1.setRowHeight(30);
        jTable1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        jTable1.setGridColor(new Color(80, 80, 80));
        jTable1.setBackground(new Color(60, 60, 60));
        jTable1.setForeground(Color.WHITE);
        jTable1.setSelectionBackground(new Color(200, 50, 50));
        jTable1.setSelectionForeground(Color.WHITE);
        jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(60,60,60) : new Color(50,50,50));
                    c.setForeground(Color.WHITE);
                }
                ((JLabel)c).setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
                return c;
            }
        });
        jScrollPane1.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        jScrollPane1.getViewport().setBackground(new Color(60, 60, 60));

        // Cards
        styleCard(jPanel4,  new Color(30, 60, 30));
        styleCard(jPanel7,  new Color(20, 30, 70));
        styleCard(jPanel11, new Color(70, 20, 20));
        styleCard(jPanel10, new Color(60, 50, 10));

        jPanel3.setBackground(new Color(26, 26, 26));
        jPanel2.setBackground(new Color(30, 30, 30));

        jLabel19.setFont(new Font("Tahoma", Font.BOLD, 14));
        jLabel19.setForeground(new Color(255, 215, 0));
    }

    private void styleNavBtn(JButton btn, boolean active) {
        btn.setBackground(active ? new Color(200, 50, 50) : new Color(26, 26, 26));
        btn.setForeground(active ? Color.WHITE : new Color(170, 170, 170));
        btn.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
    }

    private void styleCard(JPanel panel, Color bg) {
        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setText("LOGOUT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 40, 140, -1));

        jLabel15.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("WELCOME TO MS SPORTS CARS");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(-40, 40, 430, -1));

        jLabel21.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("ADMIN PANEL");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 440, 40));

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 1040, 100);

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton2.setText("USERS");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 160, 30));

        jButton3.setText("POSTS");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 160, 30));

        jButton4.setText("COMMENTS");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 160, 30));

        jButton5.setText("DASHBOARD");
        jPanel3.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 160, 30));

        jPanel1.add(jPanel3);
        jPanel3.setBounds(0, 100, 180, 620);

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setBackground(new java.awt.Color(0, 0, 0));
        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 204, 0));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("USERS = 12");
        jPanel4.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 90, 70));

        jPanel1.add(jPanel4);
        jPanel4.setBounds(250, 140, 140, 110);

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));
        jPanel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel7MouseClicked(evt);
            }
        });
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setBackground(new java.awt.Color(0, 0, 0));
        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 51, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("POSTS = 45");
        jPanel7.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 70));

        jPanel1.add(jPanel7);
        jPanel7.setBounds(420, 140, 140, 110);

        jPanel11.setBackground(new java.awt.Color(204, 204, 204));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setBackground(new java.awt.Color(0, 0, 0));
        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 0, 0));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("LIKES = 230");
        jPanel11.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 90, 60));

        jPanel1.add(jPanel11);
        jPanel11.setBounds(590, 140, 140, 110);

        jPanel10.setBackground(new java.awt.Color(204, 204, 204));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setBackground(new java.awt.Color(0, 0, 0));
        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 0));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("COMMENT = 89");
        jPanel10.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 120, 20));

        jPanel1.add(jPanel10);
        jPanel10.setBounds(760, 140, 140, 110);

        jLabel19.setBackground(new java.awt.Color(0, 0, 0));
        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("RECENT POSTS:  ");
        jPanel1.add(jLabel19);
        jLabel19.setBounds(190, 270, 150, 20);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "USERNAME", "CAR", "LIKES", "DELETE"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(220, 300, 790, 390);

        jLabel14.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("POSTS");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(420, 110, 140, 30);

        jLabel22.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("LIKES");
        jPanel1.add(jLabel22);
        jLabel22.setBounds(590, 110, 140, 30);

        jLabel23.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("COMMENT");
        jPanel1.add(jLabel23);
        jLabel23.setBounds(760, 110, 140, 30);

        jLabel24.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("USERS");
        jPanel1.add(jLabel24);
        jLabel24.setBounds(250, 110, 140, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1036, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 716, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
 int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to logout?",
        "Logout",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );
    
    if (confirm == JOptionPane.YES_OPTION) {
        this.dispose();
        loginform login = new loginform();
        login.setVisible(true);
    }
     
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
      this.dispose();
      users us = new users();
      us.setVisible(true);
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jPanel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel7MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       this.dispose();
        new Comments().setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
     this.dispose();
    new Post().setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
   java.awt.EventQueue.invokeLater(() -> {
        if (config.UserSession.getInstance().getUserId() == 0) {
            JOptionPane.showMessageDialog(null,
                "⚠️ Please log in!",
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
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

   public void setVisible(boolean visible) {
    super.setVisible(visible); // Just call the parent implementation
}
}