package Crypto;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;

import java.security.*;


public class KeysMaker {
    String NomKeystore;
    PublicKey PublicKey;
    PrivateKey PrivateKey;

    // crée des fichiers sérialisés avec les clés publiques t privées
    public KeysMaker() throws Exception {

        Security.addProvider(new BouncyCastleProvider());// permet de crypter avec Bouncy Castle

        //permet de générer des paires de clés
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA","BC");
        keyPairGenerator.initialize(2048);// un clé RSA de 2048 bits
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        setPublicKey(publicKey);
        setPrivateKey(privateKey);

        System.out.println("privateKey created");
        System.out.println("publicKey created");
    }
    // SETTERS AND GETTERS
    public String getNomKeystore() {
        return NomKeystore;
    }

    public PublicKey getPublicKey() {
        return PublicKey;
    }
    public PrivateKey getPrivateKey() {
        return PrivateKey;
    }
    public void setNomKeystore(String nomKeystore) {
        NomKeystore = nomKeystore;
    }
    public void setPublicKey(PublicKey publicKey) {
        PublicKey = publicKey;
    }
    public void setPrivateKey(PrivateKey privateKey) {
        PrivateKey = privateKey;
    }
}