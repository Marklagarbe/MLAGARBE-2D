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
import java.io.File;
import javax.swing.*;

public class ProfilePage extends javax.swing.JFrame {

    private Feed feedParent;

    public ProfilePage() {
        initComponents();
        setLocationRelativeTo(null);
    }

    public ProfilePage(Feed parent) {
        this.feedParent = parent;
        initComponents();
        setLocationRelativeTo(null);
        setTitle("S1M Sports Cars - My Profile");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jButton1.setText(UserSession.getInstance().getUsername());
        ensureProfilePictureColumn(); // siguraduhing may column sa DB
        loadProfilePicture();
        loadMyPosts();
    }

    // ── Mag-add ng profile_picture column kung wala pa ──────────────────────
    private void ensureProfilePictureColumn() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            try {
                conn.createStatement().execute(
                    "ALTER TABLE users ADD COLUMN profile_picture TEXT");
            } catch (SQLException ignored) {
                // Column existing na — okay lang
            }
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    // ── Load profile picture mula sa DB ─────────────────────────────────────
    private void loadProfilePicture() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pst = conn.prepareStatement(
                "SELECT profile_picture FROM users WHERE id = ?");
            pst.setInt(1, UserSession.getInstance().getUserId());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String picPath = rs.getString("profile_picture");
                if (picPath != null && !picPath.isEmpty()) {
                    UserSession.getInstance().setProfilePicture(picPath);
                    setAvatarImage(picPath);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading profile pic: " + e.getMessage());
        }
    }

    // ── I-set ang avatar image sa label ─────────────────────────────────────
    private void setAvatarImage(String path) {
        try {
            Image img = new ImageIcon(path).getImage()
                .getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            jLabel17.setIcon(new ImageIcon(img));
            jLabel17.setText("");
        } catch (Exception ignored) {}
    }

    // ── Browse at i-save ang profile picture ─────────────────────────────────
    private void uploadProfilePicture() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Pumili ng Profile Picture");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Image Files", "jpg", "jpeg", "png", "gif"));

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            String imagePath  = selectedFile.getAbsolutePath();

            // I-save sa database
            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement pst = conn.prepareStatement(
                    "UPDATE users SET profile_picture = ? WHERE id = ?");
                pst.setString(1, imagePath);
                pst.setInt(2, UserSession.getInstance().getUserId());
                pst.executeUpdate();

                // I-update ang session
                UserSession.getInstance().setProfilePicture(imagePath);

                // Ipakita agad
                setAvatarImage(imagePath);

                JOptionPane.showMessageDialog(this, 
                    "Na-update na ang iyong profile picture!");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    // ── Load posts ng current user ───────────────────────────────────────────
    public void loadMyPosts() {
        JPanel postsPanel = new JPanel();
        postsPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 8));
        postsPanel.setBackground(new Color(51, 51, 51));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql =
                "SELECT p.*, " +
                "(SELECT COUNT(*) FROM likes    WHERE post_id = p.id) as like_count, " +
                "(SELECT COUNT(*) FROM comments WHERE post_id = p.id) as comment_count " +
                "FROM posts p WHERE p.user_id = ? ORDER BY p.created_at DESC";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, UserSession.getInstance().getUserId());
            ResultSet rs = pst.executeQuery();

            boolean hasPosts = false;
            while (rs.next()) {
                hasPosts = true;
                postsPanel.add(buildPostCard(
                    rs.getInt("id"),
                    rs.getString("car_brand"),
                    rs.getString("car_model"),
                    rs.getString("description"),
                    rs.getString("image_path"),
                    rs.getString("created_at"),
                    rs.getInt("like_count"),
                    rs.getInt("comment_count")
                ));
            }

            if (!hasPosts) {
                JLabel empty = new JLabel("No Post!Plss Click '+ To Create Post'.");
                empty.setForeground(new Color(160, 160, 160));
                empty.setFont(new Font("Tahoma", Font.ITALIC, 13));
                empty.setHorizontalAlignment(JLabel.CENTER);
                postsPanel.add(empty);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        jScrollPane1.setViewportView(postsPanel);
        jScrollPane1.revalidate();
        jScrollPane1.repaint();
    }

    // ── Build post card ──────────────────────────────────────────────────────
    private JPanel buildPostCard(int postId, String carBrand, String carModel,
                                  String description, String imagePath,
                                  String createdAt, int likeCount, int commentCount) {

        JPanel card = new JPanel(null);
        card.setBackground(new Color(60, 60, 60));
        card.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        card.setPreferredSize(new java.awt.Dimension(1090, 160));

        // Red accent
        JPanel accent = new JPanel();
        accent.setBackground(new Color(200, 50, 50));
        accent.setBounds(0, 0, 5, 160);
        card.add(accent);

        JLabel lCar = new JLabel("🏎️  " + carBrand + " " + carModel);
        lCar.setForeground(new Color(255, 210, 0));
        lCar.setFont(new Font("Tahoma", Font.BOLD, 14));
        lCar.setBounds(18, 12, 600, 22);
        card.add(lCar);

        JLabel lDate = new JLabel("🕐  " + createdAt);
        lDate.setForeground(new Color(150, 150, 150));
        lDate.setFont(new Font("Tahoma", Font.PLAIN, 10));
        lDate.setBounds(18, 36, 400, 16);
        card.add(lDate);

        JLabel lDesc = new JLabel(
            "<html><div style='width:550px'>" + description + "</div></html>");
        lDesc.setForeground(Color.WHITE);
        lDesc.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lDesc.setBounds(18, 56, 580, 60);
        card.add(lDesc);

        JLabel lStats = new JLabel("❤️  " + likeCount + "  Likes     💬  " + commentCount + "  Comments");
        lStats.setForeground(new Color(180, 180, 180));
        lStats.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lStats.setBounds(18, 128, 350, 20);
        card.add(lStats);

        // Thumbnail
        JLabel lThumb = new JLabel("No Image", JLabel.CENTER);
        lThumb.setBackground(new Color(40, 40, 40));
        lThumb.setOpaque(true);
        lThumb.setForeground(new Color(100, 100, 100));
        lThumb.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));
        lThumb.setBounds(870, 10, 180, 120);
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image img = new ImageIcon(imagePath).getImage()
                    .getScaledInstance(180, 120, Image.SCALE_SMOOTH);
                lThumb.setIcon(new ImageIcon(img));
                lThumb.setText("");
            } catch (Exception ignored) {}
        }
        card.add(lThumb);

        // Edit button
        JButton bEdit = new JButton("✏  Edit");
        bEdit.setBounds(870, 135, 85, 20);
        bEdit.setBackground(new Color(40, 110, 40));
        bEdit.setForeground(Color.WHITE);
        bEdit.setFont(new Font("Tahoma", Font.BOLD, 11));
        bEdit.setFocusPainted(false);
        bEdit.setBorderPainted(false);
        bEdit.addActionListener(e ->
            new CreatePost(ProfilePage.this, postId, carBrand, carModel, description, imagePath)
                .setVisible(true));
        card.add(bEdit);

        // Delete button
        JButton bDel = new JButton("Delete");
        bDel.setBounds(965, 135, 85, 20);
        bDel.setBackground(new Color(140, 40, 40));
        bDel.setForeground(Color.WHITE);
        bDel.setFont(new Font("Tahoma", Font.BOLD, 11));
        bDel.setFocusPainted(false);
        bDel.setBorderPainted(false);
        bDel.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(this,
                "Are you sure do you want to delete?",
                "Delete Post", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION) deletePost(postId);
        });
        card.add(bDel);

        return card;
    }

    private void deletePost(int postId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.createStatement().execute("DELETE FROM likes    WHERE post_id = " + postId);
            conn.createStatement().execute("DELETE FROM comments WHERE post_id = " + postId);
            conn.createStatement().execute("DELETE FROM posts    WHERE id      = " + postId);
            JOptionPane.showMessageDialog(this, "Post Deleted!");
            loadMyPosts();
            if (feedParent != null) feedParent.loadPosts();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(null);

        jLabel17.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/2.png"))); // NOI18N
        jPanel1.add(jLabel17);
        jLabel17.setBounds(10, 10, 100, 80);

        jButton1.setText("Profile");
        jPanel1.add(jButton1);
        jButton1.setBounds(10, 100, 100, 23);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("MY POST:");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(450, 80, 220, 40);
        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 130, 1110, 550);

        jButton2.setText("Return...");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(1020, 690, 100, 23);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       this.dispose();
       Feed fd = new Feed();
       fd.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
   public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ProfilePage().setVisible(true));
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
