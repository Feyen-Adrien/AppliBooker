package ProtocoleBSPPS;

import ServeurTCP.Reponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class ReponseUPDATE_CADDY_PAYED implements Reponse {
    private boolean valide;
    private byte[] hmac;

    public ReponseUPDATE_CADDY_PAYED(boolean valide, SecretKey secretKey) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        this.valide = valide;
        Security.addProvider(new BouncyCastleProvider());
        Mac hm = Mac.getInstance("HMAC-MD5","BC");
        hm.init(secretKey);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeBoolean(isValide());
        hm.update(baos.toByteArray());
        setHmac(hm.doFinal());
    }
    public ReponseUPDATE_CADDY_PAYED() {
        this.valide = false;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }
    public void setHmac(byte[] hmac) {
        this.hmac = hmac;
    }

    public boolean isValide() {
        return valide;
    }
    public byte[] getHmac() {
        return hmac;
    }
}
