package model;

import java.util.Optional;

import util.ObjectUtil;

public class Login {
    private String email;
    private String password;
    public Login(){}
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return ObjectUtil.objectToString(this);
    }
}
