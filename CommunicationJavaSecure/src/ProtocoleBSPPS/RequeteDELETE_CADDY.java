package ProtocoleBSPPS;

import ServeurTCP.Requete;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.*;

public class RequeteDELETE_CADDY implements Requete {
    private final int idCaddy;
    private byte[] signature;
    private int emplKey;

    public RequeteDELETE_CADDY(int idCaddy, PrivateKey privateKey, int emplKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, IOException, SignatureException {
        Security.addProvider(new BouncyCastleProvider());
        setEmplKey(emplKey);
        this.idCaddy = idCaddy;
        Signature s = Signature.getInstance("SHA1withRSA","BC");
        s.initSign(privateKey);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idCaddy);
        s.update(baos.toByteArray());
        setSignature(s.sign());

    }

    public int getIdCaddy() {
        return idCaddy;
    }
    public byte[] getSignature() {
        return signature;
    }
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
    public int getEmplKey() {
        return emplKey;
    }
    public void setEmplKey(int emplKey) {
        this.emplKey = emplKey;
    }
}
