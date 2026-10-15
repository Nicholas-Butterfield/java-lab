import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;

public class HashCheck {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java HashCheck <filename>");
            return;
        }
        byte[] fileBytes = Files.readAllBytes(Path.of(args[0]));
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(fileBytes);
        System.out.println("SHA-256: " + HexFormat.of().formatHex(hash));
     }
}
