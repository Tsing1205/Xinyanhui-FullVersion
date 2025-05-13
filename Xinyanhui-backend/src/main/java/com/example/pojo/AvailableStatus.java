package com.example.pojo;

public enum AvailableStatus {
    AVAILABLE, BUSY;

    public static AvailableStatus transShort(Short status) {
        if(status == 1){
            return AVAILABLE;
        }
        else if(status==0){
            return BUSY;
        }
        return null;
    }

    @Override
    public String toString() {
        if(this == AVAILABLE)
            return "AVAILABLE";
        else if(this == BUSY)
            return "BUSY";
        else
            return "UNKNOWN";
    }
}
