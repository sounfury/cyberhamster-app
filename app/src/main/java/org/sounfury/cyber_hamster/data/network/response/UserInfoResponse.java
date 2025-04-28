package org.sounfury.cyber_hamster.data.network.response;

import org.sounfury.cyber_hamster.data.model.User;

import java.util.List;

public class UserInfoResponse {
    private User loginUser;
    private List<String> permissions;
    private List<String> roles;

    public User getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
} 