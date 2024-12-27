import ProtocoleBSPPS.BSPPS;
import ServeurTCP.Logger;
import ServeurTCP.Protocole;
import ServeurTCP.ThreadServeurPool;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServeurBSPPS implements Logger {
    public ServeurBSPPS() throws IOException {
        // Lis les données du fichier properties
        String filePath = "app.properties";
        String PORT_PAYEMENT_SECURE;
        String dataBaseName;
        String userName;
        String passWord;
        String NbThreads;
        Properties prop = new Properties();

        try(FileInputStream inputStream = new FileInputStream(filePath)) {
            prop.load(inputStream);
            PORT_PAYEMENT_SECURE = prop.getProperty("PORT_PAYEMENT_SECURE");
            dataBaseName = prop.getProperty("dataBaseName");
            userName = prop.getProperty("userName");
            passWord = prop.getProperty("passWord");
            NbThreads = prop.getProperty("NbThreads");

            System.out.println(PORT_PAYEMENT_SECURE);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Protocole protocole = new BSPPS(this);

        ThreadServeurPool Server = new ThreadServeurPool(Integer.parseInt(PORT_PAYEMENT_SECURE), protocole,Integer.parseInt(NbThreads),this);
        Server.start();
    }
    public static void main(String[] args) throws IOException {
        new ServeurBSPPS();
    }


    @Override
    public void Trace(String msg) {
        System.out.println(msg);
    }
}

