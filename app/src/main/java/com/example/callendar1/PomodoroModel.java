package com.example.callendar1;



public class PomodoroModel {
    private int id;
    private String kiedypraca;
    private String kiedyprzerwa;
    private String statuspraca;
    private String statusprzerwa;

    public PomodoroModel(int id, String kiedypraca, String kiedyprzerwa,String statuspraca,String statusprzerwa) {
        this.id = id;
        this.kiedyprzerwa = kiedyprzerwa;
        this.kiedypraca = kiedypraca;
        this.statuspraca = statuspraca;
        this.statusprzerwa = statusprzerwa;
    }

    public PomodoroModel() {
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "id=" + id +
                ", kiedyprzerwa='" + kiedyprzerwa + '\'' +
                ", kiedypraca=" + kiedypraca + '}';
    }

    public String getkiedyprzerwa() {
        return kiedyprzerwa;
    }

    public void setkiedyprzerwa(String kiedyprzerwa) {
        this.kiedyprzerwa = kiedyprzerwa;
    }

    public String getkiedypraca() {
        return kiedypraca;
    }

    public void setkiedypraca(String kiedypraca) {
        this.kiedypraca = kiedypraca;
    }

    public String statuspraca() {
        return statuspraca;
    }

    public void statuspraca(String statuspraca) {
        this.statuspraca = statuspraca;
    }

    public String statusprzerwa() {
        return statuspraca;
    }

    public void statusprzerwa(String statusprzerwa) {
        this.statusprzerwa = statusprzerwa;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
