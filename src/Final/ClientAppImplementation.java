package Final;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Герман on 24.03.2017.
 */

public class ClientAppImplementation {

    private static Scanner sc = new Scanner(System.in);
    private static boolean isLoggedIn = false;
    private static DataOutputStream outToServer = null;
    private static BufferedReader inFromServer = null;
    private static String serverResponse = null;

    public static void main( String[] args ) {

        try {
            Socket clientSocket = getClientSocket();
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (IOException e) {
            System.err.println("failed to connect to the server");
            e.printStackTrace();
        }

        while(true) {
            while (!isLoggedIn) {
                try {
                    isLoggedIn = userAuthorization();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            while (isLoggedIn) {

                try {
                    isLoggedIn = stockExchangePlatform();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static private boolean userAuthorization() throws IOException {
        boolean dataIsValid = false;
        System.out.println("\nChose option: ");
        System.out.println("1. Log in");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        int choice = sc.nextInt();

        if (choice == 1) {
            System.out.println("Enter name: ");
            String userName = sc.next();
            System.out.println("Enter password:");
            String password = sc.next();
            outToServer.flush();
            outToServer.writeBytes("LOG " + userName + " " + password  + "\r\n");
            serverResponse = inFromServer.readLine();
            if(serverResponse.equals("100")) {
                System.out.println("logged in successfully");
                return true;
            }
            else
                System.out.println("invalid logging data");

        } else if(choice == 2) {
            while (!dataIsValid) {
                System.out.println("Enter name: ");
                String userName = sc.next();
                System.out.println("Enter password:");
                String password = sc.next();
                System.out.println("Re-enter password:");
                String re_password = sc.next();
                if (password.equals(re_password)) {
                    outToServer.writeBytes("REG" + " " + userName + " " + password + "\r\n");
                    serverResponse = inFromServer.readLine();
                    dataIsValid = true;
                    if(serverResponse.equals("100")) {
                        System.out.println("Registered successfully");
                    }
                } else {
                    System.out.println("Passwords does not match");
                }
            }

        } else if(choice == 3) {
            outToServer.writeBytes("EXT" +"\r\n");
            serverResponse = inFromServer.readLine();
            if(serverResponse.equals("100")) {
                System.out.println("system exit");
                System.exit(0);
            }
        }
        else {
            System.out.println("No such option");
        }
        return false;
    }

    static private boolean stockExchangePlatform() throws IOException{

        System.out.println("\nChose option: ");
        System.out.println("1. Sell shares: ");
        System.out.println("2. See offers list: ");
        System.out.println("3. Buy shares: ");
        System.out.println("4. Make deposit: ");
        System.out.println("5. Check my balance: ");
        System.out.println("6. Check my shares: ");
        System.out.println("7. Log out: ");

        int choice = sc.nextInt();
        if(choice == 1) {
            String  societyName = null;
            System.out.println("Enter society name: ");
            societyName = sc.next();
            System.out.println("Enter number of shares you want to sell: ");
            int n = sc.nextInt();
            System.out.println("Enter price: ");
            int price = sc.nextInt();
            outToServer.writeBytes("SEL " + societyName + " " + n + " " + price + "\r\n");
            serverResponse = inFromServer.readLine();
            if(serverResponse.equals("100")) {
                System.out.println("offer posted");
            } else {
                System.out.println("fail to post offer");
            }

        } else if(choice == 2) {
            outToServer.writeBytes("GOF " + "\r\n");
            serverResponse = inFromServer.readLine();
            if(serverResponse.equals("100")) {
                while (!(serverResponse = inFromServer.readLine()).equals("end")) {
                    System.out.println(serverResponse);

                }
            } else {
                System.out.println("error");
            }
        } else if(choice == 3) {
            System.out.println("Enter share id: ");
            int id = sc.nextInt();
            System.out.println("Enter number of shares you want to buy: ");
            int n = sc.nextInt();
            outToServer.writeBytes("BUY " + id + " " + n + "\r\n");
            serverResponse = inFromServer.readLine();
            if(serverResponse.equals("100")) {
                System.out.println("bought successively");
            } else {
                System.out.println("deal denied");
            }
        } else if(choice == 4) {
            System.out.println("Enter deposit amount [max. 1000 euro]");
            int depositAmount = sc.nextInt();
            outToServer.writeBytes("DEP " + depositAmount + "\r\n");
            serverResponse = inFromServer.readLine();
            if(serverResponse.equals("100")) {
                System.out.println("deposit added successfully");
            } else {
                System.out.println("deposit denied");
            }
        } else if(choice == 5) {
            outToServer.writeBytes("GBA "  + "\r\n");
            serverResponse = inFromServer.readLine();
            String[] response = serverResponse.split(" ");
            if(response[0].equals("100")) {
                System.out.println(response[1]);
            } else {
                System.out.println("error");
            }
        } else if(choice == 6) {
            outToServer.writeBytes("GSH "  + "\r\n");
            serverResponse = inFromServer.readLine();
            String[] response = serverResponse.split(" ");
            if(response[0].equals("100")) {
                System.out.println(response[1] + "\t" + response[2]);
                System.out.println(response[3] + "\t" + response[4]);
                System.out.println(response[5] + "\t" + response[6]);
            } else {
                System.out.println("error");
            }
        } else if(choice == 7) {

            outToServer.writeBytes("LOT" +"\r\n");
            serverResponse = inFromServer.readLine();
            if(serverResponse.equals("100")) {
                System.out.println("logged out successively");
                return false;
            }
            else {
                System.out.println("error");
            }
        } else {
            System.out.println("No such option");
        }
        return  true;
    }

    static private Socket getClientSocket() throws IOException{
        return new Socket("localhost", 9999);
    }
}
