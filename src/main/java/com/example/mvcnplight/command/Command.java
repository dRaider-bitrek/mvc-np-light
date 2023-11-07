package com.example.mvcnplight.command;

public interface Command {
    public void setStopMessage(String id);
    public boolean review(String message);
    public String getCommand();
}
