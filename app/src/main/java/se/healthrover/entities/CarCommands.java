package se.healthrover.entities;

public enum CarCommands {

    //A list of predefined commands used to manage the car
    LEFT("left"), RIGHT("right"), FORWARD("forward"), BACK("back"), STOP("stop"), STATUS("status");

    private final String carCommands;

    CarCommands(String carCommands) {
        this.carCommands = carCommands;
    }

    public String getCarCommands() {
        return carCommands;
    }
}
