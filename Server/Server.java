
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.lang.*;

public class Server {

    // All client names, so we can check for duplicates upon registration.
    private static Set<String> names = new HashSet<>();
    public static DatabaseOperations db = new DatabaseOperations();
     // The set of all the print writers for all the clients, used for broadcast.
    private static Set<PrintWriter> writers = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running...");
        Thread.sleep(40000);
        db.intDB();
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (ServerSocket listener = new ServerSocket(59001)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }

    /**
     * The client handler task.
     */
    private static class Handler implements Runnable {
        private String name;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;
        //private DatabaseOperations db = new DatabaseOperations();

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);

                // Keep requesting a name until we get a unique one.

                while (true) {
                    out.println("SUBMITNAME");
                    name = in.nextLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            break;
                        }
                    }
                }

                out.println("NAMEACCEPTED " + name);
                writers.add(out);
                out.println("Usage: docker attach <img>\nCommands: appoint <date> <task>/ delete <id>/ show / quit");
                // Accept messages from this client and broadcast them.
                while (true) {
                    String input = in.nextLine();
                    if (input.toLowerCase().startsWith("quit")) {
                        return;
                    } else if (input.toLowerCase().startsWith("appoint")) {
                    	String task = "";
                    	int i = 0;
                    	for (String str : input.split(" ")) {
                    		i++;
                    		if (i > 2)
                    			task += str + " ";
                    	}
                    	Records r = new Records(0, name, input.split(" ")[1], task);
                    	Server.db.insertRecords(r);
                    } else if (input.toLowerCase().startsWith("delete")) {
                    	Server.db.deleteRecords(input.split(" ")[1]);
                    }

                    out.println("ID \t date \t\t task");
                    for (Records r : Server.db.selectByName(name)) {
                        out.println("" + r.id + " \t " + r.date + " \t " + r.task);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    writers.remove(out);
                }
                if (name != null) {
                    System.out.println(name + " is leaving");
                    names.remove(name);
                    for (PrintWriter writer : writers) {
                        writer.println("" + name + " has left");
                    }
                }
                try { socket.close(); } catch (IOException e) {}
            }
        }
    }
}