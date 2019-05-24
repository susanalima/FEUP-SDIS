import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import protocol.Chunk;
import protocol.subprotocol.communication.tcp.Server;
import server.ClientSocket;

public class ClientBackupTest {

  public static void main(String[] args) throws IOException {
    String host = args[0];
    int port = Integer.parseInt(args[1]);

    ClientSocket test = new ClientSocket(host, port);

    ScheduledThreadPoolExecutor executor =
        (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(5);

    int replicationDegree = 3;
    List<String> sockets = new ArrayList<>();

    File file = new File("./FILES/orpheu.txt");
    int length = (int) (file.length() / 2);
    FileInputStream stream = new FileInputStream(file);
    byte[] bytes = new byte[length];
    int read = stream.read(bytes, 0, length);
    System.out.println("READ " + read + " bytes");
    System.out.println("FileSize " + length + " bytes");
    String msgToSend = "Hey, this is a message. Bigger than before";
    // length = msgToSend.length();
    // Chunk chunk = new Chunk(0, msgToSend.getBytes());
    Chunk chunk = new Chunk(0, bytes);

    for (int i = 0; i < replicationDegree; i++) {

      Server server = new Server(chunk);
      sockets.add(server.getConnectionSettings());
      executor.schedule(server::sendChunk, 2, TimeUnit.SECONDS);
    }

    StringBuilder sB = new StringBuilder();
    sB.append("BACKUP 1as98d21hiwdhwadh19832rhwqi " + replicationDegree + " " + length + " #");
    for (String socket : sockets) {
      sB.append(socket);
      sB.append(" ");
    }

    String msg = sB.toString();
    test.write(msg);
    System.out.println("WROTE " + msg);

    String rMSG = test.read();

    System.out.println("RECEIVED " + rMSG);

    // executor.shutdown();
  }
}