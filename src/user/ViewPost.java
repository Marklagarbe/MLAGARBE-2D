/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import config.DatabaseConnection;
import config.UserSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.*;


public class ViewPost extends javax.swing.JFrame {

    private Feed feedParent;
    private int postId;

    public ViewPost() {
        initComponents();
        setLocationRelativeTo(null);
    }

    public ViewPost(Feed parent, int postId) {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("View Post");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.feedParent = parent;
        this.postId = postId;
        loadPostDetails();
        loadComments();
    }

    private void loadPostDetails() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM posts WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, postId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                lblUsername.setText("👤  Posted by: " + rs.getString("username"));
                lblCarInfo.setText("🏎️  " + rs.getString("car_brand") + " " + rs.getString("car_model"));
                lblDescription.setText("<html><div style='width:200px'>" + rs.getString("description") + "</div></html>");

                String imagePath = rs.getString("image_path");
                if (imagePath != null && !imagePath.isEmpty()) {
                    try {
                        ImageIcon icon = new ImageIcon(imagePath);
                        Image img = icon.getImage().getScaledInstance(
                            lblImage.getWidth() > 0 ? lblImage.getWidth() : 320,
                            lblImage.getHeight() > 0 ? lblImage.getHeight() : 150,
                            Image.SCALE_SMOOTH);
                        lblImage.setIcon(new ImageIcon(img));
                        lblImage.setText("");
                    } catch (Exception e) { }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading post: " + e.getMessage());
        }
    }

    public void loadComments() {
        JPanel commentsInner = new JPanel();
        commentsInner.setLayout(new java.awt.GridLayout(0, 1, 0, 5));
        commentsInner.setBackground(new Color(45, 45, 45));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM comments WHERE post_id = ? ORDER BY created_at ASC";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, postId);
            ResultSet rs = pst.executeQuery();

            boolean hasComments = false;
            while (rs.next()) {
                hasComments = true;
                JPanel commentCard = new JPanel();
                commentCard.setLayout(null);
                commentCard.setBackground(new Color(55, 55, 55));
                commentCard.setPreferredSize(new java.awt.Dimension(600, 55));

                JLabel lblCommentUser = new JLabel("👤 " + rs.getString("username") + "  •  " + rs.getString("created_at"));
                lblCommentUser.setForeground(new Color(180, 180, 180));
                lblCommentUser.setFont(new Font("Tahoma", Font.BOLD, 10));
                lblCommentUser.setBounds(10, 5, 580, 15);
                commentCard.add(lblCommentUser);

                JLabel lblCommentText = new JLabel("<html><div style='width:580px'>" + rs.getString("comment_text") + "</div></html>");
                lblCommentText.setForeground(Color.WHITE);
                lblCommentText.setFont(new Font("Tahoma", Font.PLAIN, 11));
                lblCommentText.setBounds(10, 22, 580, 28);
                commentCard.add(lblCommentText);

                commentsInner.add(commentCard);
            }

            if (!hasComments) {
                JLabel noComments = new JLabel("No comments yet. Be the first! 💬");
                noComments.setForeground(Color.LIGHT_GRAY);
                noComments.setFont(new Font("Tahoma", Font.ITALIC, 12));
                noComments.setHorizontalAlignment(JLabel.CENTER);
                commentsInner.add(noComments);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading comments: " + e.getMessage());
        }

        commentsPanel.setViewportView(commentsInner);
        commentsPanel.revalidate();
        commentsPanel.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();
        lblCarInfo = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        commentsPanel = new javax.swing.JScrollPane();
        txtComment = new javax.swing.JTextField();
        btnClose = new javax.swing.JButton();
        btnComment = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(null);

        lblImage.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        lblImage.setForeground(new java.awt.Color(255, 255, 255));
        lblImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImage.setText("Post image");
        jPanel1.add(lblImage);
        lblImage.setBounds(250, 30, 320, 150);

        lblCarInfo.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        lblCarInfo.setForeground(new java.awt.Color(255, 255, 255));
        lblCarInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCarInfo.setText("Car brand + model");
        jPanel1.add(lblCarInfo);
        lblCarInfo.setBounds(20, 10, 220, 50);

        lblUsername.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        lblUsername.setForeground(new java.awt.Color(255, 255, 255));
        lblUsername.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUsername.setText("Posted by");
        jPanel1.add(lblUsername);
        lblUsername.setBounds(10, 80, 220, 50);

        lblDescription.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        lblDescription.setForeground(new java.awt.Color(255, 255, 255));
        lblDescription.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDescription.setText("Post description");
        jPanel1.add(lblDescription);
        lblDescription.setBounds(20, 150, 220, 50);
        jPanel1.add(commentsPanel);
        commentsPanel.setBounds(40, 220, 650, 330);
        jPanel1.add(txtComment);
        txtComment.setBounds(180, 580, 440, 50);

        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        jPanel1.add(btnClose);
        btnClose.setBounds(250, 660, 130, 30);

        btnComment.setText("Comment");
        btnComment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCommentActionPerformed(evt);
            }
        });
        jPanel1.add(btnComment);
        btnComment.setBounds(40, 580, 130, 50);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 737, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCommentActionPerformed
        String commentText = txtComment.getText().trim();
        if (commentText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please write a comment first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO comments (post_id, user_id, username, comment_text) VALUES (?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, postId);
            pst.setInt(2, UserSession.getInstance().getUserId());
            pst.setString(3, UserSession.getInstance().getUsername());
            pst.setString(4, commentText);
            pst.executeUpdate();

            txtComment.setText("");
            loadComments();

            // Refresh feed comment count
            if (feedParent != null) feedParent.loadPosts();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_btnCommentActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ViewPost().setVisible(true));
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
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnComment;
    private javax.swing.JScrollPane commentsPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCarInfo;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JTextField txtComment;
    // End of variables declaration//GEN-END:variables
}
