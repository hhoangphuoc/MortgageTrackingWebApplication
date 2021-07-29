package model;


import java.util.Arrays;

import util.Hasher;
import util.ObjectUtil;

/**
 * The model class for the user.
 */
public class User {
    private int id;
    private String email;
    private byte[] password_salt;
    private byte[] password_hash;
    private String name;
    private String surname;
    private boolean is_staff;
    private int profile_img;

    private Hasher hasher = new Hasher();

    /**
     * Empty constructor necessary for
     * json library.
     */
    public User() {
    }

    public int getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(int profile_img) {
        this.profile_img = profile_img;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){return true;}
        if (!(obj instanceof User)){return false;}
        var other = (User)obj;
        return other.getId() == this.getId()
            && other.getEmail().equals(this.getEmail())
            && Arrays.equals(other.getPassword_salt(), this.getPassword_salt())
            && Arrays.equals(other.getPassword_hash(), this.getPassword_hash())
            && other.getName().equals(this.getName())
            && other.getSurname().equals(this.getSurname())
            && other.getIs_staff() == this.getIs_staff()
            && other.getProfile_img() == this.getProfile_img();
    }

    /**
     * Constructor for user.
     * @param id the id of the user.
     * @param email the email of the user.
     * @param password the password of the user.
     * @param name the name of the user.
     * @param surname the surname of the user.
     */
    public User(
        String email, String password,
        String name, String surname, boolean is_staff
    ) {
        this.id = -1;
        this.email = email;
        this.password_salt = hasher.generateSalt();
        this.setPassword(password);
        this.name = name;
        this.surname = surname;
        this.is_staff = is_staff;
    }
    public User(
        int id, String email,
        byte[] password_hash, byte[] password_salt,
        String name, String surname, boolean is_staff, int profile_img
    ) {
        this.id = id;
        this.email = email;
        this.password_hash = password_hash;
        this.password_salt = password_salt;
        this.name = name;
        this.surname = surname;
        this.is_staff = is_staff;
        this.profile_img = profile_img;
    }
    public boolean checkPassword(String password){
        return Arrays.equals(
            this.hashPassword(password),
            this.password_hash
        );
    }
    private byte[] hashPassword(String password){
        if (this.password_salt == null){
            this.password_salt = Hasher.getInstance().generateSalt();
        }
        return Hasher.getInstance().hash(password, this.password_salt);
    }
//#region getters and setters
    /**
     * getter for id.
     * @return id of the user.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for id.
     * @param id the new id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getter for email.
     * @return the email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email.
     * @param email the new email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Setter for password.
     * @param password the new password.
     */
    public void setPassword(String password) {
        this.password_hash = this.hashPassword(password);
    }
    public byte[] getPassword_hash(){
        return this.password_hash;
    }
    public byte[] getPassword_salt(){
        return this.password_salt;
    }

    /**
     * getter for name.
     * @return the name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * setter for name.
     * @param name the new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter for surname.
     * @return the surname of the user.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * setter for surname.
     * @param surname new surname.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public boolean getIs_staff() {
        return is_staff;
    }

    public void setIs_staff(boolean is_staff) {
        this.is_staff = is_staff;
    }

//#endregion getters and setters
    /**
     * returns a (json) representation of a user object.
     * @return a string representation of user.
     */
    @Override
    public String toString() {
        return ObjectUtil.objectToString(this);
    }
}
