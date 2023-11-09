package server;
import server.ClientHandler;
import model.ServerMessage;

import java.io.*;

import java.util.List;

public class ChatHistory {
    private SerializableList<ServerMessage> history;
    public ChatHistory() {
        history = new SerializableList<>();
    }

    public void addMessage(ServerMessage message) {
        history.addMessage(message);
        save();

    }

    public List<ServerMessage> getHistory() {
        load();
        return history.getList();
    }

    private void save(){
        try(FileOutputStream output = new FileOutputStream("ChatMessages");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output)){

            objectOutputStream.writeObject(history);
            objectOutputStream.flush();

        }catch(IOException eSave){
            System.out.println("saving failed! " + eSave.getMessage());
        }
    }
    private void load(){
        File file = new File("ChatMessages");
        if(!file.exists()){
            save();
        }
        try( FileInputStream input = new FileInputStream("ChatMessages");
             ObjectInputStream objectInputStream = new ObjectInputStream(input)){

            history = (SerializableList<ServerMessage>) objectInputStream.readObject();

        } catch (Exception eLoad) {
            System.out.println("Read failed " + eLoad.getMessage());
        }
    }
}
