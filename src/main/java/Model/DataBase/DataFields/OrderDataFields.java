package Model.DataBase.DataFields;;

import lombok.Getter;

@Getter
public enum OrderDataFields implements DataFieldImp {
    ID("id",0),
    TYPE("type",1),
    CLIENT_ID("client_id",2),
    CLIENT_CAR_ID("client_car_id",3),
    CLIENT_PHONE_NUMBER("client_phone_number",4),
    DESCRIPTION("description",5),
    STATUS("status",6),
    CREATED_AT("created_at",7);

    private final String value;

    private final int index;

    OrderDataFields(String value, int index) {
        this.value = value;
        this.index = index;
    }

    @Override
    public String toString() {
        return value;
    }
}