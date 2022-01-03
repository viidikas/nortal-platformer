package com.nortal.platformer;

import javax.swing.*;
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
    HashMap<Integer, Integer> unlockedPlatforms = new HashMap<>();
    private Platform activePlatform;
    private int currentPoints = 0;

    public Game(String gameFile) {

        this.gameFile = gameFile;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {

        Game game = new Game("platforms.csv");
        game.run();
    }

    public void run() throws IOException, URISyntaxException {
        platforms = readPlatforms();
        currentPoints = points;


        // TODO: Implement your mighty algorithm and jump to oblivion.
        activePlatform = platforms.get(0);
        unlockedPlatforms.put(activePlatform.getIndex(), activePlatform.getCost());

        while (!isLatestPlatform()) {
            Platform nextplatform = nextplaform(activePlatform);
            if (canIMoveToNextPlatform(nextplatform)) {
                moveToNextPlatform();
            } else {
                moveToPreviousPlatform();
            }
        }
    }

    private boolean canIMoveToNextPlatform(Platform platform) {
//        Platform nextplatform = platforms.get(activePlatform.getIndex() + 1);
        if (unlockedPlatforms.containsKey(platform.getIndex())) {
            return true;
        } else return !unlockedPlatforms.containsKey(platform.getIndex()) && platform.getCost() <= currentPoints;
    }


    private void moveToNextPlatform() {

        if (!isNextPlatformLocked()) {
            activePlatform = platforms.get(activePlatform.getIndex() + 1);
            currentPoints = currentPoints - activePlatform.getCost();
            unlockedPlatforms.put(activePlatform.getIndex(), activePlatform.getCost());
        } else {
            activePlatform = platforms.get(activePlatform.getIndex() + 1);
            currentPoints = currentPoints + activePlatform.getCost();
        }

    }

    private boolean isLatestPlatform() {
        int platformsize = platforms.size();
        return activePlatform.getIndex() + 1 >= platformsize;
    }

    private boolean isNextPlatformLocked() {
        return unlockedPlatforms.containsKey(activePlatform.getIndex() + 1);
    }

    private void moveToPreviousPlatform() {
        if (activePlatform.getIndex() > 0) {
            activePlatform = platforms.get(activePlatform.getIndex() - 1);
            currentPoints = currentPoints + activePlatform.getCost();
        }

    }

    private Platform nextplaform(Platform activePlatform) {
       return platforms.get(activePlatform.getIndex() + 1);
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
