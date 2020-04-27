package com.gkv.newbie.model;

import java.io.Serializable;

public class Procedure implements Serializable {

    String name;
    Procedure parent;
    User owner;
    String shareType;
    String procedureType;
    Process processObj;
    String process;

    public Procedure() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Procedure getParent() {
        return parent;
    }

    public void setParent(Procedure parent) {
        this.parent = parent;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getProcedureType() {
        return procedureType;
    }

    public void setProcedureType(String procedureType) {
        this.procedureType = procedureType;
    }

    public Process getProcessObj() {
        return processObj;
    }

    public void setProcessObj(Process processObj) {
        this.processObj = processObj;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
