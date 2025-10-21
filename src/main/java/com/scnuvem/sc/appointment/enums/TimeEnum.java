package com.scnuvem.sc.appointment.enums;

public enum TimeEnum {
    H08_00("08:00"),
    H08_30("08:30"),
    H09_00("09:00"),
    H09_30("09:30"),
    H10_00("10:00"),
    H14_00("14:00"),
    H14_30("14:30"),
    H15_00("15:00"),
    H15_30("15:30"),
    H16_00("16:00");

    private final String time;

    TimeEnum(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
