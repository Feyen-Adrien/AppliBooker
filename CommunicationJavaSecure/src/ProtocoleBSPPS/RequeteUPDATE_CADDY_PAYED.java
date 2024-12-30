package ProtocoleBSPPS;

import Crypto.MyCrypto;
import ServeurTCP.Requete;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.*;

public class RequeteUPDATE_CADDY_PAYED implements Requete {
    private byte[] signature;
    private byte[] data;
    private int emplKey;

    public RequeteUPDATE_CADDY_PAYED(int idCaddy, String numVISA, String nomVISA, SecretKey secretKey, PrivateKey privateKey, int emplKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException, SignatureException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Security.addProvider(new BouncyCastleProvider());
        setEmplKey(emplKey);
        // signature
        Signature s = Signature.getInstance("SHA1withRSA","BC");
        s.initSign(privateKey);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idCaddy);
        dos.writeUTF(numVISA);
        dos.writeUTF(nomVISA);
        s.update(baos.toByteArray());
        setSignature(s.sign());
        // cryptage symmétrique
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        DataOutputStream dos2 = new DataOutputStream(baos2);
        dos2.writeInt(idCaddy);
        dos2.writeUTF(numVISA);
        dos2.writeUTF(nomVISA);
        setData(MyCrypto.CryptSym3DES(secretKey,baos2.toByteArray()));
    }

    public byte[] getSignature() {
        return signature;
    }
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public int getEmplKey() {
        return emplKey;
    }
    public void setEmplKey(int emplKey) {
        this.emplKey = emplKey;
    }

}
