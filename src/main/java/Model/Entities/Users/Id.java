package Model.Entities.Users;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class Id {

    public static int getUniqueId(Map<?, ?> data){
        int id;
        while(data.containsKey((id = Math.abs(Objects.hash(Math.random()))))){}
        return id;
    }

    public static int getUniqueId(List<?> data){
        int id;
        while(data.contains((id = Math.abs(Objects.hash(Math.random()))))){}
        return id;
    }
}
