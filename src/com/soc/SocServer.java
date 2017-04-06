package com.soc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Gebruiker on 06/02/2017.
 */
public class SocServer {
    public static void  main (String[] args) throws IOException {
        System.out.println("Server is started ");
        ServerSocket ss =new ServerSocket(2000);
        System.out.println("server is waiting");
        Socket s=ss.accept();
        System.out.println("Clinet connected");

        BufferedReader br= new BufferedReader(new InputStreamReader(s.getInputStream()));
        String str =br.readLine();
        System.out.println("Data is: "+str.toUpperCase());

         String str1 = "Fuck all";
        OutputStreamWriter os= new OutputStreamWriter(s.getOutputStream());
        PrintWriter out =new PrintWriter(os);
        out.print(str1);
        out.flush();
        System.out.println("send data to client");
    }
}
