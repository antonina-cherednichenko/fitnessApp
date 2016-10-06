package com.cherednichenko.antonina.detoxdiet.detox_diet_programs_list;

/**
 * Created by tonya on 10/1/16.
 */
public enum DetoxDietLaunchMode {
    STANDARD(0), LIKED(1), SEARCH(2);
    private int mode;

    DetoxDietLaunchMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

}
