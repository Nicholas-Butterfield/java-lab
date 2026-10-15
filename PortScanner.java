import java.net.InetSocketAddress;
import java.net.Socket;

void main() {
     String target = "192.168.0.1"; // My Gateway IP
     int[] commonPorts = {21, 22, 23, 53, 80, 443, 445, 3389, 8080, 11434};

     IO.println("Scanning " + target + "...");

     for (int port : commonPorts) {
         try (Socket socket = new Socket()) {
             // Attempt connection with a 200ms timeout
             socket.connect(new InetSocketAddress(target, port), 200);
             IO.println("[OPEN] Port " + port);
         } catch (Exception _) {
             // Java 25 allows '_' for unused exception variables!
         }
      }
      IO.println("Scan complete.");
}
