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
import java.sql.SQLException;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
/**
 *
 * @author LAGARBE
 */
public class CreatePost extends javax.swing.JFrame {

    private Feed feedParent;
    private ProfilePage profileParent;
    private String selectedImagePath = "";
    private boolean isEditMode = false;
    private int editPostId = -1;

    // ── Mga values na ise-set bago mag-initComponents ────────────────────────
    private String initTitleText  = "Create New Post";
    private String initBtnText    = "SAVE POST";
    private String initCarBrand   = "";
    private String initCarModel   = "";
    private String initDesc       = "";
    private String initImagePath  = "";

    // ── CREATE mode ──────────────────────────────────────────────────────────
    public CreatePost(Feed parent) {
        this.feedParent  = parent;
        this.isEditMode  = false;
        this.initTitleText = "Create New Post";
        this.initBtnText   = "SAVE POST";
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Create New Post");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    // ── EDIT mode ────────────────────────────────────────────────────────────
    public CreatePost(ProfilePage profileParent, int postId, String carBrand,
                      String carModel, String description, String imagePath) {
        this.profileParent    = profileParent;
        this.isEditMode       = true;
        this.editPostId       = postId;
        this.selectedImagePath = imagePath != null ? imagePath : "";
        this.initTitleText    = "Edit Post";
        this.initBtnText      = "UPDATE POST";
        this.initCarBrand     = carBrand  != null ? carBrand  : "";
        this.initCarModel     = carModel  != null ? carModel  : "";
        this.initDesc         = description != null ? description : "";
        this.initImagePath    = imagePath != null ? imagePath : "";
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Edit Post");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    // ── No-arg ───────────────────────────────────────────────────────────────
    public CreatePost() {
        this.isEditMode = false;
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        lblPreview = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtCarModel = new javax.swing.JTextField();
        txtCarBrand = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        browseImage = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        saveOrUpdatePost = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(null);

        jButton1.setText("CREATE POST");
        jPanel1.add(jButton1);
        jButton1.setBounds(970, 10, 150, 40);

        lblPreview.setBackground(new java.awt.Color(255, 255, 255));
        lblPreview.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblPreview.setForeground(new java.awt.Color(255, 255, 255));
        lblPreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPreview.setText("NO IMAGE");
        jPanel1.add(lblPreview);
        lblPreview.setBounds(170, 390, 350, 120);

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Car Brand:");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(140, 130, 80, 30);

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Description:  ");
        jPanel1.add(jLabel16);
        jLabel16.setBounds(220, 240, 250, 30);
        jPanel1.add(txtCarModel);
        txtCarModel.setBounds(220, 180, 250, 30);
        jPanel1.add(txtCarBrand);
        txtCarBrand.setBounds(220, 130, 250, 30);

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText(" Car Model:");
        jPanel1.add(jLabel18);
        jLabel18.setBounds(140, 180, 80, 30);

        txtDescription.setColumns(20);
        txtDescription.setRows(5);
        jScrollPane1.setViewportView(txtDescription);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(170, 270, 350, 90);

        jLabel19.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Create New Post");
        jPanel1.add(jLabel19);
        jLabel19.setBounds(190, 20, 280, 50);

        jLabel20.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Create New Post");
        jPanel1.add(jLabel20);
        jLabel20.setBounds(190, 20, 280, 50);

        browseImage.setText(" Browse Image");
        browseImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseImageActionPerformed(evt);
            }
        });
        jPanel1.add(browseImage);
        browseImage.setBounds(280, 520, 120, 23);

        btnCancel.setText("CANCEL");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jPanel1.add(btnCancel);
        btnCancel.setBounds(350, 610, 140, 30);

        saveOrUpdatePost.setText("SAVE POST");
        saveOrUpdatePost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveOrUpdatePostActionPerformed(evt);
            }
        });
        jPanel1.add(saveOrUpdatePost);
        saveOrUpdatePost.setBounds(190, 610, 130, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void browseImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseImageActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Image Files", "jpg", "jpeg", "png", "gif"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedImagePath = fc.getSelectedFile().getAbsolutePath();
            java.awt.Image img = new ImageIcon(selectedImagePath).getImage()
                .getScaledInstance(350, 160, java.awt.Image.SCALE_SMOOTH);
            lblPreview.setIcon(new ImageIcon(img));
            lblPreview.setText("");
        }
    }//GEN-LAST:event_browseImageActionPerformed

    private void saveOrUpdatePostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveOrUpdatePostActionPerformed
      String carBrand    = txtCarBrand.getText().trim();
    String carModel    = txtCarModel.getText().trim();
    String description = txtDescription.getText().trim();

    // ── VALIDATION ──────────────────────────────────────────────────────────
    if (carBrand.isEmpty()) {
    JOptionPane.showMessageDialog(this,
        "⚠️ Please enter the Car Brand!", "Missing Field", JOptionPane.WARNING_MESSAGE);
    txtCarBrand.requestFocus();
    return;
}
if (carModel.isEmpty()) {
    JOptionPane.showMessageDialog(this,
        "⚠️ Please enter the Car Year/Model!", "Missing Field", JOptionPane.WARNING_MESSAGE);
    txtCarModel.requestFocus();
    return;
}
if (!carModel.matches("\\d+")) {
    JOptionPane.showMessageDialog(this,
        "⚠️ Car Model must be numbers only! (e.g. 2025)", "Invalid Input", JOptionPane.WARNING_MESSAGE);
    txtCarModel.setText("");
    txtCarModel.requestFocus();
    return;
}
if (description.isEmpty()) {
    JOptionPane.showMessageDialog(this,
        "⚠️ Please enter a Description!", "Missing Field", JOptionPane.WARNING_MESSAGE);
    txtDescription.requestFocus();
    return;
}
if (description.length() < 5) {
    JOptionPane.showMessageDialog(this,
        "⚠️ Description is too short!", "Invalid Input", JOptionPane.WARNING_MESSAGE);
    txtDescription.requestFocus();
    return;
}
if (selectedImagePath.isEmpty()) {
    JOptionPane.showMessageDialog(this,
        "⚠️ Please select an image!", "Missing Image", JOptionPane.WARNING_MESSAGE);
    return;
}
    }//GEN-LAST:event_saveOrUpdatePostActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new CreatePost().setVisible(true));
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
    private javax.swing.JButton browseImage;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPreview;
    private javax.swing.JButton saveOrUpdatePost;
    private javax.swing.JTextField txtCarBrand;
    private javax.swing.JTextField txtCarModel;
    private javax.swing.JTextArea txtDescription;
    // End of variables declaration//GEN-END:variables

    private static class titleLabel {

        private static void setText(String create_New_Post) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public titleLabel() {
        }
    }

    private static class previewLabel {

        private static void setIcon(ImageIcon imageIcon) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        private static void setText(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public previewLabel() {
        }
    }

  
    
}