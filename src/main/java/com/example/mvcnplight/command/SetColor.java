package com.example.mvcnplight.command;

public class SetColor implements Command {
    private String stopMessage;
    private String cell;
    private String collor;
    private String operator;

    public SetColor(String cell, String collor,String operator) {
        this.cell = cell;
        this.collor = collor;
        this.operator = operator;
        this.stopMessage = "H "+cell+" OK";
    }

    @Override
    public void setStopMessage(String id) {

    }

    @Override
    public boolean review(String message) {
        if(message.contains(stopMessage)) return true;
        return false;
    }

    @Override
    public String getCommand() {
        return "H " + cell+" "+operator+" "+collor+";";
    }
}
