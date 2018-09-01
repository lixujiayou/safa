package com.inspur.hebeiline.entity;

/**
 * Created by lixu on 2018/6/22.
 */

public class LXMineEntity {

    /**
     *  personalMonthLength  : 20
     *  personalTotalLength  : 80
     *  totalDoneLenght  : 100
     *  totalUndoLenght  : 100
     */

    private String personalMonthLength;
    private String personalTotalLength;
    private String totalDoneLenght;
    private String totalUndoLenght;

    public String getPersonalMonthLength() {
        return personalMonthLength;
    }

    public void setPersonalMonthLength(String personalMonthLength) {
        this.personalMonthLength = personalMonthLength;
    }

    public String getPersonalTotalLength() {
        return personalTotalLength;
    }

    public void setPersonalTotalLength(String personalTotalLength) {
        this.personalTotalLength = personalTotalLength;
    }

    public String getTotalDoneLenght() {
        return totalDoneLenght;
    }

    public void setTotalDoneLenght(String totalDoneLenght) {
        this.totalDoneLenght = totalDoneLenght;
    }

    public String getTotalUndoLenght() {
        return totalUndoLenght;
    }

    public void setTotalUndoLenght(String totalUndoLenght) {
        this.totalUndoLenght = totalUndoLenght;
    }
}
