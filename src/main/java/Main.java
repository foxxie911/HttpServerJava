import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
  public static void main(String[] args){
    int SERVER_PORT = 4221;
    System.out.println("Listening to port " + SERVER_PORT + ".....");
    System.out.println("Logs will appear below");
    try {
      // Initializes ServerSocket
      ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

      // Can reuse the address even if the port is busy
      serverSocket.setReuseAddress(true);

      // Waits for connection from client
      Socket clientConnection = serverSocket.accept();
      System.out.println("User Connected.....");

      var inputStream = clientConnection.getInputStream();
      var inputBuffer = new BufferedReader(new InputStreamReader(inputStream));

      StringBuilder stringBuilder = new StringBuilder();
      String line;
      while((line = inputBuffer.readLine()) != null){
        stringBuilder.append(line).append("\n");
        if(!inputBuffer.ready()) break;
      }
      String inputBufferString = stringBuilder.toString();


      String[] url = inputBufferString.split("([ \n])");
      var output = clientConnection.getOutputStream();
      if (url[1].equals("/")) {
        output.write("HTTP/1.1 200 OK\r\n\r\n".getBytes(StandardCharsets.UTF_8));
      }
      if (url[1].startsWith("/echo/")){
        String str = url[1].split("/")[2] + "\n";
        output.write(("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: "
                + str.length() + "\r\n\r\n" + str).getBytes(StandardCharsets.UTF_8));
      }
      if (url[1].equals("/user-agent")) {
        StringBuilder body = new StringBuilder(url[6]);
        body.append("\n");
        output.write(("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: "
                + body.length() + "\r\n\r\n" + body).getBytes(StandardCharsets.UTF_8));
      } else {
        output.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes(StandardCharsets.UTF_8));
      }
      output.close();
      serverSocket.close();

    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
