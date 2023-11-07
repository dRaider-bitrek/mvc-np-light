package com.example.mvcnplight.command;

public class CustomCommand implements Command{
    private String stopMessage;
    private String command;

    @Override
    public void setStopMessage(String id) {
        this.stopMessage = id;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public boolean review(String message) {
        if(message.contains(stopMessage)) return true;
        return false;
    }

    @Override
    public String getCommand() {
        return command+";";
    }
}
