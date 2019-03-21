package protocol;

import protocol.subprotocol.Initiator;
import protocol.subprotocol.Receiver;

import java.util.concurrent.ConcurrentHashMap;

public class Peer {

    private static Float version;
    private static Integer id;

    private static MulticastChannel control;
    private static MulticastChannel backup;
    private static MulticastChannel restore;

    private static Channel cmd;

    private static Initiator protocolIni;
    private static Receiver protocolRec;

    private static DataContainer dataContainer;


    //TODO : arranjar uma maneira melhor para guardar esta informaçao
    // Key = ChunkId
    // Value = Chunk body
    private static ConcurrentHashMap<String, byte[]> restoredChunks = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        if (args.length != 6) {
            System.out.println("Usage: protocol.Peer <protocol_version> <server_id> <service_ap> <MC_ip:MC_port> <MDB_ip:MDB_port> <MDR_ip:MDR_port>");
            return;
        }

        version = Float.parseFloat(args[0]);
        id = Integer.parseInt(args[1]);

        String[] ap = args[2].split(":");
        if(ap.length == 1) {
            cmd = new Channel("localhost", ap[0]);
        } else {
            cmd = new Channel(ap[0], ap[1]);
        }

        //address port
        String[] MC = args[3].split(":");
        control = new MulticastChannel(MC[0], MC[1]);

        String[] MDB = args[4].split(":");
        backup = new MulticastChannel(MDB[0], MDB[1]);

        String[] MDR = args[5].split(":");
        restore = new MulticastChannel(MDR[0], MDR[1]);

        protocolIni = new Initiator();
        protocolRec = new Receiver();

        dataContainer = DataContainer.load();

        Thread hook = new Thread(() -> {
            System.out.println("storing......................");
            dataContainer.store();
        });
        Runtime.getRuntime().addShutdownHook(hook);



        //Threads Start

        Thread threadMC = new Thread(control);
        threadMC.start();

        Thread threadMDB = new Thread(backup);
        threadMDB.start();

        Thread threadMDR = new Thread(restore);
        threadMDR.start();

        Thread threadCMD = new Thread(cmd);
        threadCMD.start();
    }

    public static MulticastChannel getControlChannel() {
        return control;
    }

    public static MulticastChannel getBackupChannel() {
        return backup;
    }

    public static MulticastChannel getRestoreChannel() {
        return restore;
    }


    //onde é que isto é chamado? channel.java -> read
    public static void initiateProtocol(String message) {
        if(!protocolIni.run(message)) {
            System.out.println("Something went wrong...");
        }
    }

    public static void answerProtocol(String message) {
        if(!protocolRec.run(message)) {
            System.out.println("Something went wrong...");
        }
    }

    public static Float getProtocolVersion() {
        return version;
    }

    public static Integer getServerId() {
        return id;
    }

    public static DataContainer getDataContainer() {
        return dataContainer;
    }

    //TODO
    public static ConcurrentHashMap<String, byte[]> getRestoredChunks() {
        return restoredChunks;
    }

    public static void addRestoredChunk(String key, byte[] body)
    {
        if(restoredChunks.get(key) == null)
            restoredChunks.put(key,body);

    }

}