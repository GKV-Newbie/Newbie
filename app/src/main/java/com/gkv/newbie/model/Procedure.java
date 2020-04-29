package com.gkv.newbie.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Procedure implements Serializable {

    String _id;
    String name;
    String parent;
    Procedure parentProcedure;
    ArrayList<Procedure> children;
    User owner;
    ArrayList<User> sharedAccess;
    String shareType="public";
    String procedureType="group";
    String process="{}";

    public Procedure() {
        children = new ArrayList<>();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public String getFormattedName() {
        return (getShareType().equals("public")?"":("\uD83D\uDD12"+" "))+name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Procedure getParentProcedure() {
        return parentProcedure;
    }

    public void setParentProcedure(Procedure parentProcedure) {
        this.parentProcedure = parentProcedure;
    }

    public ArrayList<Procedure> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Procedure> children) {
        this.children = children;
    }

    public void addChild(Procedure child){
        this.children.add(child);
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public ArrayList<User> getSharedAccess() {
        return sharedAccess;
    }

    public void setSharedAccess(ArrayList<User> sharedAccess) {
        this.sharedAccess = sharedAccess;
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

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    @Override
    public String toString() {
        return name;
    }
}
