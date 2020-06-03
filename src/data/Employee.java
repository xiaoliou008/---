package data;

import main.util.NameAccessible;

import java.util.Objects;

public class Employee implements NameAccessible {
    private String pid;
    private String pname;
    private String psword;
    private String job;

    public Employee(){}

    public Employee(String pid, String pname, String psword, String job) {
        this.pid = pid;
        this.pname = pname;
        this.psword = psword;
        this.job = job;
    }

    @Override
    public String getName() {
        return pname;
    }

    @Override
    public boolean isAll() {
        return false;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPsword() {
        return psword;
    }

    public void setPsword(String psword) {
        this.psword = psword;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(pid, employee.pid) &&
                Objects.equals(pname, employee.pname) &&
                Objects.equals(psword, employee.psword) &&
                Objects.equals(job, employee.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, pname, psword, job);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "pid='" + pid + '\'' +
                ", pname='" + pname + '\'' +
                ", psword='" + psword + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}
