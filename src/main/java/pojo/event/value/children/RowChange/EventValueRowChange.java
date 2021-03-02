package pojo.event.value.children.RowChange;

import pojo.Message;
import enums.EventValueType;
import pojo.event.value.EventValueBase;

import java.util.List;

public class EventValueRowChange extends EventValueBase {

    private String rcType;
    private List<EventColumn> o_col;
    private List<EventColumn> col;

    public EventValueRowChange(Message message) {
        super(EventValueType.ROW_CHANGE, message);
    }

    public String getRcType() {
        return rcType;
    }

    public void setRcType(String rcType) {
        this.rcType = rcType;
    }

    public List<EventColumn> getO_col() {
        return o_col;
    }

    public void setO_col(List<EventColumn> o_col) {
        this.o_col = o_col;
    }

    public List<EventColumn> getCol() {
        return col;
    }

    public void setCol(List<EventColumn> col) {
        this.col = col;
    }
}
