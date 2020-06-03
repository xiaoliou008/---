package data;

import main.util.NameAccessible;

import java.util.Objects;

public class Dynasty implements NameAccessible {

    private int cid;
    private String cname;
    private String title;
    private int syear;
    private int eyear;

    private boolean isAll = false;

    @Override
    public boolean isAll() {
        return isAll;
    }

    public void setAll(boolean all) {
        isAll = all;
    }

    public Dynasty(){isAll = true;}

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSyear() {
        return syear;
    }

    public void setSyear(int syear) {
        this.syear = syear;
    }

    public int getEyear() {
        return eyear;
    }

    public void setEyear(int eyear) {
        this.eyear = eyear;
    }

    public Dynasty(int cid, String cname, String title, int syear, int eyear) {
        this.cid = cid;
        this.cname = cname;
        this.title = title;
        this.syear = syear;
        this.eyear = eyear;
    }

    @Override
    public String getName() {
        return cname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dynasty dynasty = (Dynasty) o;
        return cid == dynasty.cid &&
                syear == dynasty.syear &&
                eyear == dynasty.eyear &&
                Objects.equals(cname, dynasty.cname) &&
                Objects.equals(title, dynasty.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cid, cname, title, syear, eyear);
    }

    @Override
    public String toString() {
        return "Dynasty{" +
                "cid=" + cid +
                ", cname='" + cname + '\'' +
                ", title='" + title + '\'' +
                ", syear=" + syear +
                ", eyear=" + eyear +
                '}';
    }
}
