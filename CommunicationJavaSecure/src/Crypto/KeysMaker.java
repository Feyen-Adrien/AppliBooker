package Crypto;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.cert.Certificate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;


public class KeysMaker {
    String NomKeystore;

    public KeysMaker()
    {
        Security.addProvider(new BouncyCastleProvider());
        Provider prov[] = Security.getProviders();
        for (int i=0; i<prov.length; i++)
            System.out.println(prov[i].getName() + "/" + prov[i].getVersion());
    }
    // crée des fichiers sérialisés avec les clés publiques t privées
    public KeysMaker(String nomKeystore) throws Exception {

        Security.addProvider(new BouncyCastleProvider());// permet de crypter avec Bouncy Castle
        setNomKeystore(nomKeystore);

        //permet de générer des paires de clés
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA","BC");
        keyPairGenerator.initialize(2048);// un clé RSA de 2048 bits
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Enregistrer les clés dans des fichiers .ser (sérialisation binaire)
        savePrivateKey(privateKey, "CommunicationJavaSecure/Keystore/privateKey"+ nomKeystore+ ".ser");
        savePublicKey(publicKey, "CommunicationJavaSecure/Keystore/publicKey"+ nomKeystore+ ".ser");

        System.out.println("Keystore privateKey"+ nomKeystore+ ".ser created");
        System.out.println("Keystore publicKey"+ nomKeystore+ ".ser created");
    }
    // SETTERS AND GETTERS
    public String getNomKeystore() {
        return NomKeystore;
    }
    public void setNomKeystore(String nomKeystore) {
        NomKeystore = nomKeystore;
    }

    // Fonctions utiles
    // Méthode pour enregistrer la clé privée dans un fichier .ser
    private void savePrivateKey(PrivateKey privateKey, String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(privateKey); // Sérialiser la clé privée dans le fichier
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Méthode pour enregistrer la clé publique dans un fichier .ser
    private void savePublicKey(PublicKey publicKey, String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(publicKey); // Sérialiser la clé publique dans le fichier
        }
    }
}