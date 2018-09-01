package com.inspur.hebeiline.entity.eventbean;

public class MyEvent {

    public static final int OKK = 1;
    public static final int NOO = -1;
    public static final int ME = 0;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MyEvent(int type) {
        this.type = type;
    }

}
