package ProtocoleBSPPS;

import ServeurTCP.Requete;

import java.security.PublicKey;

public class RequeteLOGIN implements Requete {
    private String userName;
    private String firstName;
    private byte[] password = null;
    private byte[] secretKey = null;
    private PublicKey publicKey = null;

    public RequeteLOGIN(String userName, String firstName) {
        setUserName(userName);
        setFirstName(firstName);
    }

    public RequeteLOGIN(String userName, String firstName, byte[] password, byte[] secretKey, PublicKey publicKey) {
        setUserName(userName);
        setFirstName(firstName);
        setPassword(password);
        setSecretKey(secretKey);
        setPublicKey(publicKey);
    }
    public String getUserName() {
        return userName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public byte[] getPassword() {
        return password;
    }
    public void setPassword(byte[] password) {
        this.password = password;
    }
    public byte[] getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(byte[] secretKey) {
        this.secretKey = secretKey;
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
