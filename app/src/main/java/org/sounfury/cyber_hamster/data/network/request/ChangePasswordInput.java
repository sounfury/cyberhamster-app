package org.sounfury.cyber_hamster.data.network.request;

public class ChangePasswordInput {
    private String oldPassword; // 旧密码
    private String newPassword; // 新密码
    
    public ChangePasswordInput(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
    
    public String getOldPassword() {
        return oldPassword;
    }
    
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
