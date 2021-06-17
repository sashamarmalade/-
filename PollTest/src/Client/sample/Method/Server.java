package Client.sample.Method;

import java.io.*;
import java.net.Socket;

public class Server implements Runnable {

    private boolean ThreadStop = false;
    private InputStream in = null;
    private OutputStream out = null;
    private final Thread t;
    private final Socket SocketUser;
    private Object Data;

    public Server(Socket user) {
        Data = null;
        this.SocketUser = user;
        t = new Thread(this, "Server");
        if (Connection()) t.start();
    }

    private boolean Connection() {
        try {
            in = SocketUser.getInputStream();
            out = SocketUser.getOutputStream();
            System.out.println("Connection on...");
            return true;
        } catch (Exception e) {
            System.out.println("Error to connection...");
            return false;
        }
    }

    public void OutData(Object Data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(Data);
            oos.flush();

            out.write(bos.toByteArray());
            oos.close();
            bos.close();

        } catch (Exception e) {
            System.out.println("The message was not sent to the server...");
            System.out.println("Error: " + e.getMessage());
        }

    }

    public Object getData() {
        return Data;
    }

    public void ClearData() {
        Data = null;
    }

    @Override
    public void run() {
        while (!ThreadStop) {

            try {
                byte[] bts = new byte[in.available()];
                in.read(bts);
                ByteArrayInputStream bis = new ByteArrayInputStream(bts);
                ObjectInputStream ois = new ObjectInputStream(bis);
                Object NewData = ois.readObject();
                if (NewData != null) Data = NewData;
            } catch (Exception e) {
                if (e.getMessage() != null)
                    System.out.println(e.getMessage());
            }

            try {
                t.sleep(1000);
            } catch (Exception ignored) {
            }
        }
    }

    public void Stop() {
        ThreadStop = true;
    }

}
