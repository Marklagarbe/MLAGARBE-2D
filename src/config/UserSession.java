package config;

public class UserSession {
    private static UserSession instance;
    private int userId;
    private String username;
    private String email;
    private String role;
    private String status;
    private String profilePicture; // ← BAGO

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // ── Updated setUserData — may profilePicture na ──────────────────────────
    public void setUserData(int userId, String username, String email, 
                             String role, String status) {
        this.userId   = userId;
        this.username = username;
        this.email    = email;
        this.role     = role;
        this.status   = status;
    }

    public void setUserData(int userId, String username, String email,
                             String role, String status, String profilePicture) {
        this.userId         = userId;
        this.username       = username;
        this.email          = email;
        this.role           = role;
        this.status         = status;
        this.profilePicture = profilePicture;
    }

    public void setProfilePicture(String path) {
        this.profilePicture = path;
    }

    public void clearSession() {
        this.userId         = 0;
        this.username       = null;
        this.email          = null;
        this.role           = null;
        this.status         = null;
        this.profilePicture = null;
    }

    public int    getUserId()        { return userId; }
    public String getUsername()      { return username; }
    public String getEmail()         { return email; }
    public String getRole()          { return role; }
    public String getStatus()        { return status; }
    public String getProfilePicture(){ return profilePicture; }
}