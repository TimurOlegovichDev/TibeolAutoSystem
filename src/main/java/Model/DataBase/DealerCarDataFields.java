package Model.DataBase;

import lombok.Getter;

@Getter
public enum DealerCarDataFields implements DataFieldImp {
    ID("id",0),
    BRAND("brand",1),
    MODEL("model",2),
    COLOR("color",3),
    YEAR("year",4),
    PRICE("price",5),
    MILE_AGE("mile_age",6),
    DESCRIPTION("description",7),
    BOOKED("booked",8);

    private final String value;

    private final int index;

    DealerCarDataFields(String value, int index) {
        this.value = value;
        this.index = index;
    }

    @Override
    public String toString() {
        return value;
    }
}
