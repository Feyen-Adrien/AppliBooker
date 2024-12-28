package Crypto;


import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;



public class KeystoreMaker{
    String NomKeystore;
    String NomAlias;
    String Mdp;
    KeyStore ks;

    public KeystoreMaker(String NomKeystore,String NomAllias, String Mdp) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        setNomKeystore(NomKeystore);
        setNomAlias(NomAllias);
        setMdp(Mdp);
        // vérifie si le fichier keystore existe déjà pour le client concerné ou le serveur si non on crée
        Path path = Paths.get("D:\\HEPL_3_2024-2025\\Reseaux_Technologie_Internet\\Labo\\AppliBooker\\CommunicationJavaSecure\\Keystore" + NomKeystore + ".bks");
        if(!Files.exists(path))
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");// car utilisation bouncy castle
            keyStore.load(null, null); // crée un keystore vide

            //permet de générer des paires de clés
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);// un clé RSA de 2048 bits

            // génére une paire de clés et son certificzt auto-signé (pas de CA)
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();






        }



    }
    // SETTERS AND GETTERS
    public String getNomKeystore() {
        return NomKeystore;
    }
    public String getNomAlias() {
        return NomAlias;
    }
    public String getMdp() {
        return Mdp;
    }
    public KeyStore getKs() {
        return ks;
    }

    public void setNomKeystore(String nomKeystore) {
        NomKeystore = nomKeystore;
    }
    public void setNomAlias(String nomAlias) {
        NomAlias = nomAlias;
    }
    public void setMdp(String mdp) {
        Mdp = mdp;
    }
    public void setKs(KeyStore ks) {
        this.ks = ks;
    }

    // Autres fonctions utiles
    private X509Certificate generateCertificate(PublicKey publicKey, PrivateKey privateKey) throws CertificateException {
        long currentTime = System.currentTimeMillis();
        Date today = new Date(currentTime);
        Date inOneYear = new Date(currentTime - 365 * 24 * 60 * 60 * 1000L);

        BigInteger serialNumber = new BigInteger(130, new SecureRandom());
        return null;
    }
}