package Final;

import javafx.application.Platform;
//import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;


import static java.lang.System.in;
import static javafx.application.Platform.isFxApplicationThread;

/**
 * Created by Герман on 24.03.2017.
 */
public class ServerImplementation {

    static ArrayList<User> users = new ArrayList<User>();
    static ArrayList<Offer> market = new ArrayList<Offer>();
    static int offerCounter = 3;

    static final String REQUEST_COMMANDS[] =
            {       "LOG",          //user login request     [0]
                    "EXT",          //terminate connection   [1]
                    "SEL",          //sell request           [2]
                    "DEP",          //make deposit           [3]
                    "GOF",          //get offers list        [4]
                    "GBA",          //get balance            [5]
                    "GSH",          //get shares             [6]
                    "LOT",          //log out                [7]
                    "BUY",          //buy shares             [8]
                    "REG"           //register new user      [9]
            };

    static final String STATUS_CODES[] =
            {       "100",          //OK                     [0]
                    "200",          //FAILED                 [1]
            };

    public static void main(String[] args) throws Throwable, IOException {

        users.add(new User("FootballSociety", "qwerty", 0, 0, 0, 0));
        users.add(new User("TennisSociety", "qwerty", 0, 0,0,0));
        users.add(new User("ChessSociety", "qwerty", 0, 0,0,0));
        Offer societyOffer0 = new Offer(0, 5000, 5, "FootballSociety", users.get(0));
        Offer societyOffer1 = new Offer(1, 5000, 5, "TennisSociety", users.get(1));
        Offer societyOffer2 = new Offer(2, 5000, 5, "ChessSociety", users.get(2));
        market.add(societyOffer0);
        market.add(societyOffer1);
        market.add(societyOffer2);

        ServerSocket ss = new ServerSocket(9999);
        while (true) {
            Socket s = ss.accept();
            System.err.println("Client accepted");
            new Thread(new SocketProcessor(s)).start();
        }
    }

    private static class SocketProcessor implements Runnable {
        private Socket s;
        private DataOutputStream sendToClient = null;
        private BufferedReader inFromClient = null;
        private boolean isConnected = true;
        private User user = null;

        private SocketProcessor(Socket s) throws Throwable {
            this.s = s;
            this.sendToClient = new DataOutputStream(s.getOutputStream());
            this.inFromClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
        }

        public void run() {

            while (isConnected) {

                String request = null;
                try {
                    request = inFromClient.readLine();
                    sendToClient.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (request != null) {
                    try {
                        writeResponse(request);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }

        }

        private void writeResponse(String request) throws Throwable {
            String[] clientRequest = request.split(" ");
            System.out.println(clientRequest[0]);

            if (clientRequest[0].equals(REQUEST_COMMANDS[0])) {

                sendToClient.flush();
                if(login(clientRequest[1], clientRequest[2])) {
                    sendToClient.writeBytes(STATUS_CODES[0] + "\r\n");
                } else sendToClient.writeBytes(STATUS_CODES[1] + "\r\n");
            }
            else if (clientRequest[0].equals(REQUEST_COMMANDS[1])) {
                sendToClient.writeBytes(STATUS_CODES[0] + "\r\n");
                isConnected = false;
            }
            else if (clientRequest[0].equals(REQUEST_COMMANDS[2])) {
                String societyName = clientRequest[1];
                int n = Integer.parseInt(clientRequest[2]);
                int price =  Integer.parseInt(clientRequest[3]);
                boolean isValid = false;
                if(societyName.equals("FootballSociety")) {
                    if(user.getSharesFootballSociety() >= n) {
                        user.setSharesFootballSociety(user.getSharesFootballSociety() - n);
                        isValid = true;
                    }
                } else if(societyName.equals("ChessSociety")) {
                    if(user.getSharesChessSociety() >= n) {
                        user.setSharesChessSociety(user.getSharesChessSociety() - n);
                        isValid = true;
                    }

                } else if(societyName.equals("TennisSociety")) {
                    if(user.getSharesTennisSociety() >= n) {
                        user.setSharesTennisSociety(user.getSharesTennisSociety() - n);
                        isValid = true;
                    }
                }
                if(isValid) {
                    Offer new_offer = new Offer(offerCounter, n, price, societyName, this.user);
                    market.add(new_offer);
                    offerCounter++;
                    sendToClient.writeBytes(STATUS_CODES[0] + "\r\n");
                } else {
                    sendToClient.writeBytes(STATUS_CODES[1] + "\r\n");
                }
            }
            else if (clientRequest[0].equals(REQUEST_COMMANDS[3])) {
                int deposit_amount = Integer.parseInt(clientRequest[1]);
                if(deposit_amount <= 1000) {
                    sendToClient.writeBytes(STATUS_CODES[0] + "\r\n");
                    this.user.setBalance( deposit_amount + this.user.getBalance() );
                } else {
                    sendToClient.writeBytes(STATUS_CODES[1] + "\r\n");
                }
            }
            else if (clientRequest[0].equals(REQUEST_COMMANDS[4])) {

                sendToClient.writeBytes(STATUS_CODES[0] + "\r\n");
                for (Offer offer : market) {
                    sendToClient.writeBytes(offer.toString() + "\r\n");
                    sendToClient.flush();
                }
                sendToClient.writeBytes("end\r\n");
            }
            else if (clientRequest[0].equals(REQUEST_COMMANDS[5])) {
                sendToClient.writeBytes(STATUS_CODES[0] + " " + this.user.getBalance() + "\r\n");
            }
            else if (clientRequest[0].equals(REQUEST_COMMANDS[6])) {

                sendToClient.writeBytes(STATUS_CODES[0]  + " " + "ChessSocietyShares= " + user.getSharesChessSociety()
                                                            + " " + "FootballSocietyShares= " + user.getSharesFootballSociety()
                                                            + " " + "TennisSocietyShares= " + user.getSharesTennisSociety() + "\r\n");
            }
            else if (clientRequest[0].equals(REQUEST_COMMANDS[7])) {
                sendToClient.writeBytes(STATUS_CODES[0] + "\r\n");
            }
            else if (clientRequest[0].equals(REQUEST_COMMANDS[8])) {
                int id = Integer.parseInt(clientRequest[1]);
                int n =  Integer.parseInt(clientRequest[2]);
                boolean success = false;

                for (Offer offer : market) {
                    if(offer.getId() == id && offer.getnShares() >= n) {
                        if(user.getBalance() >= offer.getPrice()*n) {
                            user.setBalance(user.getBalance() - offer.getPrice()*n);
                            offer.getUser().setBalance(offer.getUser().getBalance() + offer.getPrice()*n );
                            String societyName = offer.getSocietyName();
                            if(societyName.equals("FootballSociety")) {
                                user.setSharesFootballSociety(user.getSharesFootballSociety() + n);
                            } else if(societyName.equals("ChessSociety")) {
                                user.setSharesChessSociety(user.getSharesChessSociety() + n);
                            } else if(societyName.equals("TennisSociety")) {
                                user.setSharesTennisSociety(user.getSharesTennisSociety() + n);
                            }
                            if(offer.getnShares() == n) {
                                market.remove(offer);
                            } else {
                                offer.setnShares(offer.getnShares() - n);
                            }
                            success = true;
                            sendToClient.writeBytes(STATUS_CODES[0] + "\r\n");
                        }
                    }
                }
                if(!success) {
                    sendToClient.writeBytes(STATUS_CODES[1] + "\r\n");
                }
            }
            else if (clientRequest[0].equals(REQUEST_COMMANDS[9])) {
                registerUser(clientRequest[1], clientRequest[2]);
                sendToClient.writeBytes(STATUS_CODES[0] + "\r\n");
            } else {

            }
        }

        private void registerUser(String userName, String password) {
            users.add(new User(userName, password, 0, 0, 0, 0));
        }

        private boolean login(String userName, String password) {
            for (User user : users) {
                if (user.getName().equals(userName) && user.getPassword().equals(password)) {
                    this.user = user;
                    return true;
                }
            }
            return false;
        }
    }
}
