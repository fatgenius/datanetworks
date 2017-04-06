package com.soc;

import java.io.*;
import java.net.Socket;

/**
 * Created by Gebruiker on 13/02/2017.
 */
public class Client {
    public static void main(String[] args) throws IOException {
       // Socket socket = null;
       // String host = "localhost";


        Socket socket = new Socket("localhost", 4444);

        File file = new File("C:\\Users\\Gebruiker\\Desktop\\Hello.html");
        // Get the size of the file
        long length = file.length();
        byte[] bytes = new byte[16 * 1024];
        InputStream in = new FileInputStream(file);
        OutputStream out = socket.getOutputStream();

        int count;
        while ((count = in.read(bytes)) > 0) {
            out.write(bytes, 0, count);
        }

        out.close();
        in.close();
        socket.close();
    }
}