package main;

import os.OperatingSystem;

public class Main {
    public static void main(String[] args) {
        OperatingSystem operatingSystem = new OperatingSystem();
        operatingSystem.associate();
        operatingSystem.run();
    }
}
