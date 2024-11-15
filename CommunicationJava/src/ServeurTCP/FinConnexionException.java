package ServeurTCP;

public class FinConnexionException extends Exception{
    private Reponse reponse;

    public FinConnexionException(Reponse reponse) {
        super("Fin de Conexxion décidé par le protocole");
        this.reponse = reponse;
    }

    public Reponse getReponse() {
        return reponse;
    }
}
