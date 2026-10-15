import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

void main() throws Exception {
    String jsonPayload = """
        {
          "model": "llama3.2:1b",
          "prompt": "Give me one essential Linux security tip in one sentence.",
          "stream": false
        }
        """;
     HttpClient client = HttpClient.newHttpClient();
     HttpRequest request = HttpRequest.newBuilder()
         .uri(URI.create("http://127.0.0.1:11434/api/generate"))
         .header("Content-Type", "application/json")
         .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
         .build();

      IO.println("Querying local Llama 3.2 model...");
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      IO.println("\nRaw API Response:\n" + response.body());
}
