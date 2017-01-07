package com.freeta.daily.fitness.detox_diet_programs_list;

/**
 * Created by tonya on 10/1/16.
 */
public enum DetoxDietLaunchMode {
    STANDARD(0), LIKED(1), SEARCH(2), NEW(3);
    private int mode;

    DetoxDietLaunchMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

}
