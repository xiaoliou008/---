package data;

import java.sql.SQLException;
import java.util.Objects;

public class Repair {
    private String eid;
    private String rid;
    private String broke;
    private String stime;
    private String result;
    private String restore;
    private String etime;
    private String pid;
    private boolean repaired = false;

    public Repair() {
    }

    public Repair(String eid, String rid, String broke, String stime) {
        this.setEid(eid);
        this.setRid(rid);
        this.setBroke(broke);
        this.setStime(stime);
        this.setResult("未修复");
    }

    public Repair(String eid, String rid, String broke, String stime, String result, String restore, String etime, String pid) {
        this.setRepaired(true);
        this.setEid(eid);
        this.setRid(rid);
        this.setBroke(broke);
        this.setStime(stime);
        this.setResult(result);
        this.setRestore(restore);
        this.setEtime(etime);
        this.setPid(pid);
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getBroke() {
        return broke;
    }

    public void setBroke(String broke) {
        this.broke = broke;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRestore() {
        return restore;
    }

    public void setRestore(String restore) {
        this.restore = restore;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public boolean isRepaired() {
        return repaired;
    }

    public void setRepaired(boolean repaired) {
        this.repaired = repaired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repair repair = (Repair) o;
        return isRepaired() == repair.isRepaired() &&
                Objects.equals(getEid(), repair.getEid()) &&
                Objects.equals(getRid(), repair.getRid()) &&
                Objects.equals(getBroke(), repair.getBroke()) &&
                Objects.equals(getStime(), repair.getStime()) &&
                Objects.equals(getResult(), repair.getResult()) &&
                Objects.equals(getRestore(), repair.getRestore()) &&
                Objects.equals(getEtime(), repair.getEtime()) &&
                Objects.equals(getPid(), repair.getPid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEid(), getRid(), getBroke(), getStime(), getResult(), getRestore(), getEtime(), getPid(), isRepaired());
    }

    @Override
    public String toString() {
        return "Repair{" +
                "eid='" + getEid() + '\'' +
                ", rid='" + getRid() + '\'' +
                ", broke='" + getBroke() + '\'' +
                ", stime='" + getStime() + '\'' +
                ", result='" + getResult() + '\'' +
                ", restore='" + getRestore() + '\'' +
                ", etime='" + getEtime() + '\'' +
                ", pid='" + getPid() + '\'' +
                ", repaired=" + isRepaired() +
                '}';
    }

    public String getRname() throws SQLException {
        return RelicSQL.queryName(getRid());
    }

    public String getPname() throws SQLException{
        if(getPid() == null) return null;
        return EmployeeSQL.queryName(getPid());
    }
}
