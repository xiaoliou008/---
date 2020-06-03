package data;

import main.util.NameAccessible;

import java.util.Objects;

/**
 * 文物分类
 */
public class RelicType implements NameAccessible {
    private int tid;
    private String tname;
    private String tdesc;

    private boolean isAll = false;

    @Override
    public boolean isAll() {
        return isAll;
    }

    public void setAll(boolean all) {
        isAll = all;
    }

    public RelicType(){isAll = true;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelicType relicType = (RelicType) o;
        return tid == relicType.tid &&
                Objects.equals(tname, relicType.tname) &&
                Objects.equals(tdesc, relicType.tdesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tid, tname, tdesc);
    }

    @Override
    public String toString() {
        return "RelicType{" +
                "tid=" + tid +
                ", tname='" + tname + '\'' +
                ", tdesc='" + tdesc + '\'' +
                '}';
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTdesc() {
        return tdesc;
    }

    public void setTdesc(String tdesc) {
        this.tdesc = tdesc;
    }

    public RelicType(int tid, String tname, String tdesc) {
        this.tid = tid;
        this.tname = tname;
        this.tdesc = tdesc;
    }

    @Override
    public String getName() {
        return tname;
    }
}
