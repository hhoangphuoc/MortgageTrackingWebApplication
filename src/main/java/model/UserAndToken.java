package model;

public class UserAndToken {
    User user;
    String token;

    UserAndToken(){}
    public UserAndToken(User user, String token){
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }
//    public void setUser(User user) {
//        this.user = user;
//    }

    public String getToken() {
        return token;
    }
//    public void setToken(String token) {
//        this.token = token;
//    }
    
}
