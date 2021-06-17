package Server.sample;


import DataTransfer.*;
import Server.sample.Method.DataBaseWork;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class User implements Runnable{

    private boolean ThreadStop = false;
    private InputStream in = null;
    private OutputStream out = null;
    private final Thread t;
    private final Socket SocketUser;
    private Object Data;

    public User(Socket user){
        this.SocketUser = user;
        t = new Thread(this, "Client");
        if (Connection()) t.start();
    }

    public boolean isThreadStop() {
        return ThreadStop;
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

    private void OutData(Object Data){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(Data);
            oos.flush();

            out.write(bos.toByteArray());
            oos.close();
            bos.close();
            this.Data = null;
        } catch (Exception e) {
            System.out.println("The message was not sent to the server...");
            System.out.println("Error: " + e.getMessage());
        }
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
                if (e.getMessage() != null) {
                    System.out.println(e.getMessage());
                    break;
                }
            }

            try {
                t.sleep(1000);
            } catch (Exception ignored) {
            }


            if (Data != null) {
                switch (Data.getClass().getSimpleName()){
                    case "LoginData" -> {
                        LoginData LD = (LoginData) Data;
                        OutData(new DataBaseWork().login(LD.getMailLogin(),LD.getPassword()));
                    }
                    case "RegistrationData" ->{
                        RegistrationData RD = (RegistrationData) Data;
                        OutData(new DataBaseWork().add_user(RD.getMail(), RD.getLogin(), RD.getPassword()));
                    }
                    case "AllThemeData" ->{
                        AllThemeData TD = (AllThemeData) Data;
                        OutData(new DataBaseWork().getAllTheme(TD.getUser_id()));
                    }
                    case "ThemeData" ->{
                        ThemeData TD = (ThemeData) Data;
                        OutData(new DataBaseWork().getTheme(TD.getTheme_id()));
                    }
                    case "QuestionData" ->{
                        QuestionData QD = (QuestionData) Data;
                        OutData(new DataBaseWork().getQuestion(QD.getID()));
                    }
                    case "InputData" ->{
                        InputData ID = (InputData) Data;
                        OutData(new DataBaseWork().InputDataDB(ID.getUser_id(),ID.getTheme_id()));
                    }
                    case "InputAnswer" ->{
                        InputAnswer ID = (InputAnswer) Data;
                        OutData(new DataBaseWork().InputAnswerDB(ID.getUser_id(),ID.getTheme_id(),ID.getAnswers()));
                    }
                    case "UserMsg" ->{
                        UserMsg userMsg = (UserMsg) Data;
                        System.out.println("Client Disconnect");
                        if (userMsg.getMsg().equals("Exit")) Stop();

                    }
                }
            }

        }
    }

    private void Stop(){
        ThreadStop = true;
    }

}


public class Main {

    public static Map<String, String> Config = new HashMap<>();



    public static void main(String[] args) {
        ArrayList<User> AllUsers = new ArrayList<>();
        ConfigInitialization();
        try {

            ServerSocket ss = new ServerSocket(Integer.parseInt(Config.get("ServerPort:")));
            Socket s;

            while (true) {
                System.out.println("Waiting connection...");
                s = ss.accept();
                AllUsers.removeIf(User::isThreadStop);
                AllUsers.add(new User(s));
                System.out.println("Number of users: " + AllUsers.size());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

    private static void ConfigInitialization() {
        Map<String,String> StandardConfig = new HashMap<>();
        StandardConfig.put("ServerPort:","1111");
        StandardConfig.put("DataBaseHost:","localhost");
        StandardConfig.put("User:", "user");
        StandardConfig.put("Password:", "");

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("src/Client/sample/Config.txt"));
        } catch (FileNotFoundException e) {
            Config = StandardConfig;
            return;
        }

        String line;

        while (true) {
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                Config = StandardConfig;
                return;
            }


            if (line == null) continue;
            boolean Empty = line.substring(line.lastIndexOf(":") + 1).trim().length() != 0;
            if (!Empty) continue;

            if (line.contains("ServerPort:"))
                    StandardConfig.put("ServerPort:", line.substring(line.lastIndexOf(":") + 1).trim());

            if (line.contains("DataBaseHost:"))
                    StandardConfig.put("DataBaseHost:", line.substring(line.lastIndexOf(":") + 1).trim());

            if (line.contains("User:"))
                    StandardConfig.put("User:", line.substring(line.lastIndexOf(":") + 1).trim());

            if (line.contains("Password:"))
                    StandardConfig.put("Password:", line.substring(line.lastIndexOf(":") + 1).trim());
        }
        Config = StandardConfig;
    }


}


