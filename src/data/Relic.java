package data;

import main.util.NameAccessible;

import java.sql.SQLException;
import java.util.Objects;

/**
 * 文物
 */
public class Relic implements NameAccessible {
    private String rid;
    private String rname;
    private int tid;
    private int cid;

    public String getImagePath() {
        return imagePath;
    }

    private boolean display;
    private boolean broken;
    private String unity;
    private int num;
    private String imagePath;

    private boolean isAll = false;

    @Override
    public boolean isAll() {
        return isAll;
    }

    public void setAll(boolean all) {
        isAll = all;
    }

    public Relic() {   isAll = true;
    }

    /**
     * 构造函数
     *
     * @param rid
     * @param rname
     * @param tid
     * @param cid
     * @param display
     * @param broken
     * @param unity
     * @param num
     */
    public Relic(String rid, String rname, int tid, int cid, boolean display, boolean broken, String unity, int num, String path) {
        this.rid = rid;
        this.rname = rname;
        this.tid = tid;
        this.cid = cid;
        this.display = display;
        this.broken = broken;
        this.unity = unity;
        this.num = num;
        this.imagePath = path;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public int getTid() {
        return tid;
    }

    public String getTidName() throws SQLException {
        return RelicTypeSQL.queryTID(tid).getName();
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getCid() {
        return cid;
    }

    public String getCidName() throws SQLException {
        return DynastySQL.queryCID(cid).getName();
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public boolean isDisplay() {
        return display;
    }

    public String getDisplay() {
        return display ? "是" : "否";
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public boolean isBroken() {
        return broken;
    }

    public String getBroken() {
        return broken ? "是" : "否";
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Relic{" +
                "rid='" + rid + '\'' +
                ", rname='" + rname + '\'' +
                ", tid=" + tid +
                ", cid=" + cid +
                ", display=" + display +
                ", broken=" + broken +
                ", unity='" + unity + '\'' +
                ", num=" + num +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relic relic = (Relic) o;
        return Objects.equals(rid, relic.rid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rid);
    }

    @Override
    public String getName() {
        return this.rname;
    }
}
