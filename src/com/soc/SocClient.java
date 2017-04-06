/**
 * Created by Gebruiker on 06/02/2017.
 */
package com.soc;

import java.io.*;
import java.net.Socket;

public class SocClient {
    public static void main (String[] args) throws Exception {
        String ip="localhost";
        int port= 2000;
        Socket s = new Socket(ip,port);
        String str ="BuTian German";

        OutputStreamWriter os= new OutputStreamWriter(s.getOutputStream());
        PrintWriter out =new PrintWriter(os);
        out.print(str);
        os.flush();
        os.close();//tcp/ip should be close

        BufferedReader br= new BufferedReader(new InputStreamReader(s.getInputStream()));
        String str2 =br.readLine();
        System.out.println("Data from server: "+str2.toLowerCase());
    }

}
