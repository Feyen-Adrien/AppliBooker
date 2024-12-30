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

public class ReponseDELETE_CADDY implements Reponse {
    private boolean valid;
    private byte[] hmac;

    public ReponseDELETE_CADDY(boolean valid, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchProviderException, IOException, InvalidKeyException {

        this.valid = valid;
        Security.addProvider(new BouncyCastleProvider());
        Mac hm = Mac.getInstance("HMAC-MD5","BC");
        hm.init(secretKey);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeBoolean(isValid());
        hm.update(baos.toByteArray());
        setHmac(hm.doFinal());

    }

    public ReponseDELETE_CADDY() {
        this.valid = false;
    }

    public boolean isValid() {
        return valid;
    }
    public byte[] getHmac() {
        return hmac;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
    public void setHmac(byte[] hmac) {
        this.hmac = hmac;
    }
}
