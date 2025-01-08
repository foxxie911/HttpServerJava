import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) {
    System.out.println("Logs from your program will appear here!");
    try {
      // Initializes ServerSocket
      ServerSocket serverSocket = new ServerSocket(4221);
      
      // Can reuse the address even if the port is busy
      serverSocket.setReuseAddress(true);
      
      // Waits for connection from client
      Socket clientConnection = serverSocket.accept();
      System.out.println("accepted new connection");

      var inputStream = clientConnection.getInputStream();
      String inputBufferString = new BufferedReader(new InputStreamReader(inputStream)).readLine();
      String[] url = inputBufferString.split(" ");

      var output = clientConnection.getOutputStream();
      if(url[1].equals("/")){
        output.write("HTTP/1.1 200 OK\r\n\r\n".getBytes(StandardCharsets.UTF_8));
      }
      output.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());


    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
