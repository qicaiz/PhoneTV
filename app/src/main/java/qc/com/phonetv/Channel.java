package qc.com.phonetv;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/7/15.
 */

public class Channel extends BmobObject implements Serializable{
    private String name;
    private String sourceAddress;
    private String posterAddress;
    private String currentProgram;
    private String nextProgram;
    private String type;

    public Channel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuourceAddress() {
        return sourceAddress;
    }

    public void setSuourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getPosterAddress() {
        return posterAddress;
    }

    public void setPosterAddress(String posterAddress) {
        this.posterAddress = posterAddress;
    }

    public String getCurrentProgram() {
        return currentProgram;
    }

    public void setCurrentProgram(String currentProgram) {
        this.currentProgram = currentProgram;
    }

    public String getNextProgram() {
        return nextProgram;
    }

    public void setNextProgram(String nextProgram) {
        this.nextProgram = nextProgram;
    }
}
