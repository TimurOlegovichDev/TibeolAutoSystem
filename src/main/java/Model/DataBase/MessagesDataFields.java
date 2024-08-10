package Model.DataBase;

import lombok.Getter;

@Getter
public enum MessagesDataFields implements DataFieldImp {
    RECEIVER_ID("receiver_id",0),
    SENDER_ID("sender_id",1),
    DESCRIPTION("description",2);

    private final String value;

    private final int index;

    MessagesDataFields(String value, int index) {
        this.value = value;
        this.index = index;
    }

    @Override
    public String toString() {
        return value;
    }
}