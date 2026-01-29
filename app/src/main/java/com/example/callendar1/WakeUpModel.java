package com.example.callendar1;

public class WakeUpModel {
    private int id;
    private String godzina;
    private String kiedywakeup;
    private boolean enabled;

    public WakeUpModel(int id, String godzina, String kiedywakeup, boolean enabled) {
        this.id = id;
        this.godzina = godzina;
        this.kiedywakeup = kiedywakeup;
        this.enabled = enabled;
    }

    public int getId() { return id; }
    public String getGodzina() { return godzina; }
    public String getkiedywakeup() { return kiedywakeup; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
