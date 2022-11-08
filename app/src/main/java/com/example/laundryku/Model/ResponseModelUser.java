package com.example.laundryku.Model;

import java.util.List;

public class ResponseModelUser {
    private int kodeUser;
    private String pesanUser;
    private List<DataModelUser> dataUser;

    public int getKodeUser() {
        return kodeUser;
    }

    public void setKodeUser(int kodeUser) {
        this.kodeUser = kodeUser;
    }

    public String getPesanUser() {
        return pesanUser;
    }

    public void setPesanUser(String pesanUser) {
        this.pesanUser = pesanUser;
    }

    public List<DataModelUser> getDataUser() {
        return dataUser;
    }

    public void setDataUser(List<DataModelUser> dataUser) {
        this.dataUser = dataUser;
    }
}
