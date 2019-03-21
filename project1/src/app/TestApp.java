package app;

import protocol.Channel;

public class TestApp {

    public final static String BACKUP = "BACKUP";
    public final static String RESTORE = "RESTORE";
    public final static String DELETE = "DELETE";
    public final static String RECLAIM = "RECLAIM";
    public final static String STATE = "STATE";

    private static String sub_protocol;
    private static String opnd_1 = "";
    private static String opnd_2 = "";

    private static Channel cmd;

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: app.TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2> ");
            return;
        } else if(args.length == 4) {
            opnd_2 = args[3];
        }
        opnd_1 = args[2];

        String[] ap = args[0].split(":");
        if(ap.length == 1) {
            cmd = new Channel("localhost", ap[0]);
        } else {
            cmd = new Channel(ap[0], ap[1]);
        }

        sub_protocol = args[1];

        //Parameters check
        if(sub_protocol.equals(BACKUP) && args.length != 4) {
            System.out.println("Usage: app.TestApp <peer_ap> " + sub_protocol + "  <filepath> <replication_degree> ");
            return;
        } else if(sub_protocol.equals(RESTORE) && args.length != 3) {
            System.out.println("Usage: app.TestApp <peer_ap> " + sub_protocol + "  <filepath>");
            return;
        } else if(sub_protocol.equals(DELETE) && args.length != 3) {
            System.out.println("Usage: app.TestApp <peer_ap> " + sub_protocol + "  <filepath>");
            return;
        } else if(sub_protocol.equals(RECLAIM) && args.length != 3) {
            System.out.println("Usage: app.TestApp <peer_ap> " + sub_protocol + "  <max_size>");
            return;
        } else if(sub_protocol.equals(STATE) && args.length != 2) {
            System.out.println("Usage: app.TestApp <peer_ap> " + sub_protocol);
            return;
        }

        String message = sub_protocol + " " + opnd_1 + " " + opnd_2;
        cmd.write(message);
        return;
    }
}