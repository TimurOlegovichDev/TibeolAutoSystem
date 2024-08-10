package Model.DataBase;

import lombok.Getter;

@Getter
public enum ClientCarDataFields implements DataFieldImp {

    ID("id",0),
    BRAND("brand",1),
    MODEL("model",2);

    private final String value;

    private final int index;

    ClientCarDataFields(String value, int index) {
        this.value = value;
        this.index = index;
    }

    @Override
    public String toString() {
        return value;
    }
}
