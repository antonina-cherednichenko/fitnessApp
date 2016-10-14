package com.cherednichenko.antonina.detoxdiet.detox_diet_data;

/**
 * Created by tonya on 10/10/16.
 */
public class EventInfo {
    private long time;
    private ProgramInfo program;

    public void setProgram(ProgramInfo program) {
        this.program = program;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ProgramInfo getProgram() {
        return program;
    }

    public long getTime() {
        return time;
    }
}
