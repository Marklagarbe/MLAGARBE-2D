package user;


import config.DatabaseConnection;
import config.UserSession;
import mainpage.loginform;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;   

/**
 *
 * @author USER22
 */
public class Feed extends javax.swing.JFrame {

    /**
     * Creates new form admindash
     */
    public Feed() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("S1M Sports Cars - Feed");
        createTables();
        loadPosts();
    }

    private void createTables() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS posts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "username TEXT," +
                "car_brand TEXT," +
                "car_model TEXT," +
                "description TEXT," +
                "image_path TEXT," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)"
            );
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS comments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "post_id INTEGER," +
                "user_id INTEGER," +
                "username TEXT," +
                "comment_text TEXT," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)"
            );
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS likes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "post_id INTEGER," +
                "user_id INTEGER)"
            );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }
    }

    public void loadPosts() {
        postsPanel.removeAll();
        postsPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT p.*, " +
                        "(SELECT COUNT(*) FROM likes WHERE post_id = p.id) as like_count, " +
                        "(SELECT COUNT(*) FROM comments WHERE post_id = p.id) as comment_count " +
                        "FROM posts p ORDER BY p.created_at DESC";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            boolean hasPosts = false;
            while (rs.next()) {
                hasPosts = true;
                JPanel card = createPostCard(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("car_brand"),
                    rs.getString("car_model"),
                    rs.getString("description"),
                    rs.getString("image_path"),
                    rs.getString("created_at"),
                    rs.getInt("like_count"),
                    rs.getInt("comment_count")
                );
                postsPanel.add(card);
            }

            if (!hasPosts) {
                JLabel noPost = new JLabel("No posts yet! Be the first to post! 🏎️");
                noPost.setForeground(Color.LIGHT_GRAY);
                noPost.setFont(new Font("Tahoma", Font.ITALIC, 14));
                noPost.setHorizontalAlignment(JLabel.CENTER);
                postsPanel.add(noPost);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        postsPanel.revalidate();
        postsPanel.repaint();
    }

    private JPanel createPostCard(int postId, String username, String carBrand,
                                   String carModel, String description, String imagePath,
                                   String createdAt, int likeCount, int commentCount) {

        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(new Color(60, 60, 60));
        card.setPreferredSize(new java.awt.Dimension(950, 220));

        // Username
        JLabel lblUser = new JLabel("👤 " + username + "   •   " + createdAt);
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblUser.setBounds(10, 10, 600, 20);
        card.add(lblUser);

        // Car Brand + Model
        JLabel lblCar = new JLabel("🏎️  " + carBrand + " " + carModel);
        lblCar.setForeground(new Color(255, 200, 0));
        lblCar.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblCar.setBounds(10, 35, 500, 20);
        card.add(lblCar);

        // Description
        JLabel lblDesc = new JLabel(
            "<html><div style='width:580px'>" + description + "</div></html>"
        );
        lblDesc.setForeground(Color.WHITE);
        lblDesc.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblDesc.setBounds(10, 60, 600, 80);
        card.add(lblDesc);

        // Image
        JLabel lblImage = new JLabel();
        lblImage.setBackground(new Color(40, 40, 40));
        lblImage.setOpaque(true);
        lblImage.setHorizontalAlignment(JLabel.CENTER);
        lblImage.setText("No Image");
        lblImage.setForeground(Color.GRAY);
        lblImage.setBounds(720, 10, 200, 150);
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(
                    200, 150, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(img));
                lblImage.setText("");
            } catch (Exception e) { }
        }
        card.add(lblImage);

        // Like button
        JButton btnLike = new JButton("❤️  " + likeCount + " Likes");
        btnLike.setBounds(10, 160, 140, 35);
        btnLike.setBackground(new Color(200, 50, 50));
        btnLike.setForeground(Color.WHITE);
        btnLike.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnLike.addActionListener(e -> likePost(postId));
        card.add(btnLike);

        // Comment button
        JButton btnComment = new JButton("💬  " + commentCount + " Comments");
        btnComment.setBounds(165, 160, 170, 35);
        btnComment.setBackground(new Color(50, 100, 200));
        btnComment.setForeground(Color.WHITE);
        btnComment.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnComment.addActionListener(e -> {
            ViewPost vp = new ViewPost(Feed.this, postId);
            vp.setVisible(true);
        });
        card.add(btnComment);

        return card;
    }

    private void likePost(int postId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String checkSql = "SELECT id FROM likes WHERE post_id=? AND user_id=?";
            PreparedStatement check = conn.prepareStatement(checkSql);
            check.setInt(1, postId);
            check.setInt(2, UserSession.getInstance().getUserId());
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                // Unlike
                PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM likes WHERE post_id=? AND user_id=?");
                pst.setInt(1, postId);
                pst.setInt(2, UserSession.getInstance().getUserId());
                pst.executeUpdate();
            } else {
                // Like
                PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO likes (post_id, user_id) VALUES (?,?)");
                pst.setInt(1, postId);
                pst.setInt(2, UserSession.getInstance().getUserId());
                pst.executeUpdate();
            }
            loadPosts();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        btnCreatePost = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        postsPanel = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(null);

        jLabel17.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/2.png"))); // NOI18N
        jPanel1.add(jLabel17);
        jLabel17.setBounds(10, 10, 100, 80);

        btnLogout.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnLogout.setText("LOGOUT");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        jPanel1.add(btnLogout);
        btnLogout.setBounds(980, 700, 140, 23);

        jLabel19.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("WELCOME TO S1M SPORTS CARS !");
        jPanel1.add(jLabel19);
        jLabel19.setBounds(710, 0, 460, 50);

        btnCreatePost.setText("+ Create Post");
        btnCreatePost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreatePostActionPerformed(evt);
            }
        });
        jPanel1.add(btnCreatePost);
        btnCreatePost.setBounds(920, 70, 160, 40);

        postsPanel.setToolTipText("");
        postsPanel.setMaximumSize(new java.awt.Dimension(40, 40));
        postsPanel.setLayout(null);
        jScrollPane1.setViewportView(postsPanel);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(80, 120, 980, 570);

        jLabel18.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText(" S1M Sports Cars - Feed");
        jPanel1.add(jLabel18);
        jLabel18.setBounds(300, 40, 520, 60);

        jButton1.setText("Your Profile");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(10, 90, 100, 23);

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

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
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

    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnCreatePostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreatePostActionPerformed
        CreatePost cp = new CreatePost(Feed.this);
    cp.setVisible(true);
    
    }//GEN-LAST:event_btnCreatePostActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ProfilePage page = new ProfilePage(Feed.this);
    page.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

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
        java.util.logging.Logger.getLogger(Feed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }

    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            // ← DITO ANG DAGDAG
            if (config.UserSession.getInstance().getUserId() == 0) {
                javax.swing.JOptionPane.showMessageDialog(null,
                    "⚠️ Please log in first!",
                    "Login Required",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                new mainpage.loginform().setVisible(true);
            } else {
                new Feed().setVisible(true);
            }
        }
    });
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreatePost;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel postsPanel;
    // End of variables declaration//GEN-END:variables

   
}
   
