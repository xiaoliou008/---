package data;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Objects;

public class Display {
    private String rid;
    private Date dtime;
    private String place;

    public Display() {}
    public Display(String rid, Date dtime, String place) {
        this.setRid(rid);
        this.setDtime(dtime);
        this.setPlace(place);
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public Date getDtime() {
        return dtime;
    }

    public void setDtime(Date dtime) {
        this.dtime = dtime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Display display = (Display) o;
        return Objects.equals(getRid(), display.getRid()) &&
                Objects.equals(getDtime(), display.getDtime()) &&
                Objects.equals(getPlace(), display.getPlace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRid(), getDtime(), getPlace());
    }

    @Override
    public String toString() {
        return "Display{" +
                "rid='" + getRid() + '\'' +
                ", dtime=" + getDtime() +
                ", place='" + getPlace() + '\'' +
                '}';
    }

    /**
     * 获取文物名称
     * @return
     */
    public String getRname() throws SQLException {
        return RelicSQL.queryName(getRid());
    }

}
