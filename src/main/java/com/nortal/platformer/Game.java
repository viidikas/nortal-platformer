package com.nortal.platformer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {

    private final String gameFile;
    private final Integer points = 500;
    List<Platform> platforms;
    //    List<Platform> unlockedPlatforms = new ArrayList<>();
    HashMap<Integer, Integer> unlockedPlatforms = new HashMap<>();
    private Platform activePlatform;
    private int currentPoints = 0;

    public Game(String gameFile) {

        this.gameFile = gameFile;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {

//      Game game = new Game("C:\\dev\\WinterUniversity_2022\\WinterUniversity_2022\\java-platformer\\src\\main\\resources\\platforms.csv");
        Game game = new Game("platforms.csv");
        game.run();
    }

    public void run() throws IOException, URISyntaxException {
        platforms = readPlatforms();
        currentPoints = points;

        // TODO: Implement your mighty algorithm and jump to oblivion.
        activePlatform = platforms.get(0);
        unlockedPlatforms.put(activePlatform.getIndex(), activePlatform.getCost());
        currentPoints = currentPoints - activePlatform.getCost();

        while (true) {
            if (canIMoveToNextPlatform()) {
                moveToNextPlatform();
                if (isLatestPlatform()) {
                    break;
                }
            } else {
                moveToPreviousPlatform();
            }
        }
    }

    private boolean canIMoveToNextPlatform() {
        return isNextPlatfromUnlocked();
    }

    private void moveToNextPlatform() {

        if (isNextPlatfromUnlocked()) {
            activePlatform = platforms.get(activePlatform.getIndex() + 1);
            currentPoints = currentPoints + activePlatform.getCost();
        } else {
            activePlatform = platforms.get(activePlatform.getIndex() + 1);
            currentPoints = currentPoints - activePlatform.getCost();
        }

        unlockedPlatforms.put(activePlatform.getIndex(), activePlatform.getCost());
        jumpTo(activePlatform);
    }

    private boolean isLatestPlatform() {
        int platformsize = platforms.size();
        return activePlatform.getIndex() + 1 >= platformsize;
    }

    private boolean isNextPlatfromUnlocked() {
        return !unlockedPlatforms.containsKey(activePlatform.getIndex() + 1);
    }

    private void moveToPreviousPlatform() {
        if (activePlatform.getIndex() > 0) {
            activePlatform = platforms.get(activePlatform.getIndex() - 1);
            currentPoints = currentPoints + activePlatform.getCost();
        }

    }

    /**
     * Reads platforms from csv file and returns the as list.
     *
     * @return platforms - Platforms as list
     */
    private List<Platform> readPlatforms() throws IOException, URISyntaxException {
        List<Platform> platforms = new ArrayList<>();

        URL resource = getClass().getClassLoader().getResource(gameFile);
        File csvFile = new File(resource.toURI());
        List<String> lines = Files.readAllLines(csvFile.toPath());

        for (int i = 1; i < lines.size(); i++) {
            String[] lineparts = lines.get(i).split(", ");
            Platform platform = Platform.builder().index(Integer.parseInt(lineparts[0])).cost(Integer.parseInt(lineparts[1])).build();
            platforms.add(platform);
        }
        return platforms;
    }


    /**
     * Invoke this function to jump to next platform.
     *
     * @param platform - Platform that you are going to jump to.
     */
    public void jumpTo(Platform platform) {
        activePlatform = platform;
    }
}
