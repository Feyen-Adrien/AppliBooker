package Handler;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;

public class HandlerRoot implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("--- Requete recue ---");

        // Lecture de la requete
        String requestPath = exchange.getRequestURI().getPath();
        System.out.println("requestPath = " + requestPath);

        String requestMethod = exchange.getRequestMethod();
        System.out.println("requestMethod = " + requestMethod);

        Headers requestHeaders = exchange.getRequestHeaders();
        System.out.println("Header : " );
        for (String key : requestHeaders.keySet())
            System.out.println(key + ": " + requestHeaders.get(key));

        InputStream is = exchange.getRequestBody();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String ligne;
        System.out.println("Request Body :");
        while ((ligne = br.readLine()) != null)
            System.out.println(ligne);

        // Ecriture de la reponse
        String reponse = "ceci est le corps de la reponse";
        exchange.getResponseHeaders().set("Content-Type","text/plain");
        exchange.sendResponseHeaders(200,reponse.length());
        OutputStream os = exchange.getResponseBody();
        os.write(reponse.getBytes());
        os.close();

        System.out.println("--- Reponse envoyee ---\n");
    }
}
