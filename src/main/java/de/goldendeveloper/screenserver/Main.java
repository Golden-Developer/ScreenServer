package de.goldendeveloper.screenserver;


import java.io.IOException;

public class Main {

    private static Config config;

    public static void main(String[] args) throws IOException {
        config = new Config();
        new Reader();
    }

    public static Config getConfig() {
        return config;
    }
}
