import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

void main(String[] args) throws Exception {
     // Allows passing an IP via command line, defaults to the gateway
     String target = args.length > 0 ? args[0] : "192.168.0.1";
     int[] commonPorts = {21, 22, 23, 53, 80, 443, 445, 3389, 8080, 11434};
     List<Integer> openPorts = new ArrayList<>();

     IO.println("Scanning " + target + " for open ports...");

     for (int port : commonPorts) {
         try (Socket socket = new Socket()) {
             socket.connect(new InetSocketAddress(target, port), 200);
             openPorts.add(port);
             IO.println("[OPEN] Port " + port);
         } catch (Exception _) {}
     }

     if (openPorts.isEmpty()) {
        IO.println("No open ports found. Exiting analysis.");
        return;
     }

     IO.println("\nRequesting analysis from local Llama 3.2...");

     // Format a natural language prompt including the list of open ports
     String prompt = "I ran a port scan on a local network device (" + target + ") and found these ports open: "
                    + openPorts + ". In two sentences, tell me what kind of device this likely is and if these open ports present a security risk.";

     // Inject the prompt into the JSON payload expected by the Ollama API
     String jsonPayload = """
         {
           "model": "llama3.2:1b",
           "prompt": "%s",
           "stream": false
         }
         """.formatted(prompt);

      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create("http://127.0.0.1:11434/api/generate"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      // Extract the AI's generated response string from the raw JSON output
      String body = response.body();
      int startIndex = body.indexOf("\"response\":\"") + 12;
      int endIndex = body.indexOf("\"done\"");

      IO.println("\n--- AI Security Analysis ---");
      if (startIndex > 11 && endIndex > startIndex) {
          String aiText = body.substring(startIndex, endIndex)
                              .replace("\\n", "\n")
                              .replace("\\\"", "\"");
          IO.println(aiText);
      } else {
          IO.println(body);
      }
}
