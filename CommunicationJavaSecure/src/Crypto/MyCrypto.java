package Crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class MyCrypto
{
    // Chiffrement symétrique avec Triple DES (3DES)
    public static byte[] CryptSym3DES(SecretKey cle, byte[] data) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
        // Utilisation de l'algorithme Triple DES (3DES) avec ECB et PKCS5Padding
        Security.addProvider(new BouncyCastleProvider());
        Cipher chiffrementE = Cipher.getInstance("DESede/ECB/PKCS5Padding", "BC");
        chiffrementE.init(Cipher.ENCRYPT_MODE, cle);
        return chiffrementE.doFinal(data);
    }

    // Déchiffrement symétrique avec Triple DES (3DES)
    public static byte[] DecryptSym3DES(SecretKey cle, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
        // Utilisation de l'algorithme Triple DES (3DES) avec ECB et PKCS5Padding
        Security.addProvider(new BouncyCastleProvider());
        Cipher chiffrementD = Cipher.getInstance("DESede/ECB/PKCS5Padding", "BC");
        chiffrementD.init(Cipher.DECRYPT_MODE, cle);
        return chiffrementD.doFinal(data);
    }

    // Chiffrement asymétrique avec RSA
    public static byte[] CryptAsymRSA(PublicKey cle, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Security.addProvider(new BouncyCastleProvider());
        Cipher chiffrementE = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        chiffrementE.init(Cipher.ENCRYPT_MODE, cle);
        return chiffrementE.doFinal(data);
    }

    // Déchiffrement asymétrique avec RSA
    public static byte[] DecryptAsymRSA(PrivateKey cle, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Security.addProvider(new BouncyCastleProvider());
        Cipher chiffrementD = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        chiffrementD.init(Cipher.DECRYPT_MODE, cle);
        return chiffrementD.doFinal(data);
    }

    // Méthode pour générer une clé secrète pour 3DES // permettre de générer une clé de session
    public static SecretKey generate3DESKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator keyGen = KeyGenerator.getInstance("DESede","BC");
        keyGen.init(168);  // La taille de clé 3DES est généralement de 168 bits
        return keyGen.generateKey();
    }

    // Méthode pour convertir un tableau d'octets en clé 3DES à partir d'une chaîne
    public static SecretKey get3DESKeyFromBytes(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes, "DESede");
    }
}
