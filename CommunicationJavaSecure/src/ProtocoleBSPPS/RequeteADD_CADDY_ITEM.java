package ProtocoleBSPPS;

import Crypto.MyCrypto;
import ServeurTCP.Requete;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class RequeteADD_CADDY_ITEM implements Requete {
    byte[] data;
    int emplSecretKey;

    public RequeteADD_CADDY_ITEM(int idLivre, int quantite, int caddyCreated, int idClient, SecretKey secretKey, int emplSecretKey) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, NoSuchProviderException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idLivre);
        dos.writeInt(quantite);
        dos.writeInt(caddyCreated);
        dos.writeInt(idClient);
        setData(MyCrypto.CryptSym3DES(secretKey,baos.toByteArray()));
        this.emplSecretKey = emplSecretKey;
    }
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public int getEmplSecretKey() {
        return emplSecretKey;
    }
    public void setEmplSecretKey(int emplSecretKey) {
        this.emplSecretKey = emplSecretKey;
    }
}
