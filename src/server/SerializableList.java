package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SerializableList<T> implements Serializable {
    private List<T> list;

    public SerializableList() {
        list = Collections.synchronizedList(new ArrayList<>());
    }
   
    public List<T> getList() {
        return list;
    }

    public void addMessage(T t){
        list.add(t);
    }


}

