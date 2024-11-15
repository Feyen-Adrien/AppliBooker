import ProtocoleBSPP.BSPP;
import ServeurTCP.Logger;
import ServeurTCP.Protocole;
import ServeurTCP.ThreadServeurDemande;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServeurBSPP implements Logger {
    public ServeurBSPP() throws IOException {
        // Lis les données du fichier properties
        String filePath = "app.properties";
        String PORT_PAYEMENT;
        String dataBaseName;
        String userName;
        String passWord;
        Properties prop = new Properties();

        try(FileInputStream inputStream = new FileInputStream(filePath)) {
            prop.load(inputStream);
            PORT_PAYEMENT = prop.getProperty("PORT_PAYEMENT");
            dataBaseName = prop.getProperty("dataBaseName");
            userName = prop.getProperty("userName");
            passWord = prop.getProperty("passWord");

            System.out.println(PORT_PAYEMENT);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Protocole protocole = new BSPP(this);

        ThreadServeurDemande Server = new ThreadServeurDemande(Integer.parseInt(PORT_PAYEMENT), protocole,this);
        Server.start();
    }
    public static void main(String[] args) throws IOException {
        new ServeurBSPP();
    }


    @Override
    public void Trace(String msg) {
        System.out.println(msg);
    }
}
