package com.example.laundryku.Model;

import java.util.List;

public class ResponseModelLogin {
    private int kodeLogin;
    private String pesanLogin;
    private List<DataModelLogin> dataLogin;

    public int getKodeLogin() {
        return kodeLogin;
    }

    public void setKodeLogin(int kodeLogin) {
        this.kodeLogin = kodeLogin;
    }

    public String getPesanLogin() {
        return pesanLogin;
    }

    public void setPesanLogin(String pesanLogin) {
        this.pesanLogin = pesanLogin;
    }

    public List<DataModelLogin> getDataLogin(int i) {
        return dataLogin;
    }

    public void setDataLogin(List<DataModelLogin> dataLogin) {
        this.dataLogin = dataLogin;
    }
}
