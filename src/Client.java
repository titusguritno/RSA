
import java.io.*;
import java.net.Socket;

public class Client{

    public static void main(String[] args) {
        double totalLatency = 0;
        int messageCount = 0;
        try {
            // Encrypt the message before sending it to the network socket
            Socket client = new Socket("127.0.0.1", 12345);
            System.out.println("Client: " + client);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            RSA rsa = new RSA(); // Initialize RSA
            String encryptedMessage = rsa.encrypt("Encrypt the message before sending it to the network socket", rsa.getPublicKey());
            System.out.println("Encrypted message : " + encryptedMessage);

            double startTime = System.nanoTime(); // Start measuring latency
            out.println(encryptedMessage); // Send the encrypted message to server
            out.flush();

            // Receive response from server
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
//                    System.out.println("test " + reader.readLine());
            String encryptedResponse = reader.readLine(); // Read encrypted response from server
            if (encryptedResponse != null) {
                // Decrypt the response using RSA private key
                String decryptedResponse = rsa.decrypt(encryptedMessage);

                // Display the response
                System.out.println("Server(Encrypted) : " + encryptedResponse + "\n");
                // Display the decrypted response
                System.out.println("Server : " + decryptedResponse + "\n");

                double endTime = System.nanoTime(); // End measuring latency
                double latency = (endTime - startTime) / 1000000; // Convert nano seconds to milliseconds
                totalLatency += latency;
                messageCount++;
                // Display calculate latency
                System.out.println("Startime = " + startTime);
                System.out.println("Endtime = " + endTime);
                System.out.println("Average Latency: " + (totalLatency / (double) messageCount) + " ms\n");
                client.close();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
