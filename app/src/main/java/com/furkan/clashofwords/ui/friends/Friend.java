package com.furkan.clashofwords.ui.friends;

public class Friend {
    private String username;
    private int profileImageResId;
    private boolean isOnline;

    public Friend(String username, int profileImageResId, boolean isOnline) {
        this.username = username;
        this.profileImageResId = profileImageResId;
        this.isOnline = isOnline;
    }

    public String getUsername() {
        return username;
    }

    public int getProfileImageResId() {
        return profileImageResId;
    }

    public boolean isOnline() {
        return isOnline;
    }
}
