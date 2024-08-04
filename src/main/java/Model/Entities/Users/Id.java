package Model.Entities.Users;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Класс, служащий для идентификационного номера в пределах базы данных, переданный в параметре методов
 * Он стал незаменимым, так как класс UUID генерирует слишком большой ключ, а ID один из главных способов получения данных из хранилищ
 */

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
