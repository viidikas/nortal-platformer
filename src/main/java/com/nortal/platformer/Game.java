package com.nortal.platformer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Game {

    private final String gameFile;
    private final Integer points = 500;
    List<Platform> platforms;
    List<Platform> unlockedPlatforms = new ArrayList<>();
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
        unlockedPlatforms.add(activePlatform);
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


//        Platform nextPlatform = findNextPlatform(activePlatform);
//        jumpTo(nextPlatform);

    }

    private boolean canIMoveToNextPlatform() {
        if (isNextPlatfromUnlocked()) {
            return true;
        }
//        Platform nextPlatform = platforms.get(activePlatform.getIndex() + 1);
//        currentPoints = currentPoints + nextPlatform.getCost();
        return false;
    }

    private void moveToNextPlatform() {

        if (isNextPlatfromUnlocked()) {
            activePlatform = platforms.get(activePlatform.getIndex() + 1);
            currentPoints = currentPoints + activePlatform.getCost();
        }
        else
        {
            activePlatform = platforms.get(activePlatform.getIndex() + 1);
            currentPoints = currentPoints - activePlatform.getCost();
        }

        unlockedPlatforms.add(activePlatform);
        jumpTo(activePlatform);
    }

    private boolean isLatestPlatform() {
        int platformsize = platforms.size();
        return activePlatform.getIndex() + 1 >= platformsize;
    }

    private boolean isNextPlatfromUnlocked() {
        Platform nextPlatform = platforms.get(activePlatform.getIndex() + 1);
        for (int i = 0; i < unlockedPlatforms.size(); i++) {
            if (unlockedPlatforms.get(i).getIndex() == nextPlatform.getIndex()) {
                return true;
            }
        }
        return false;
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

    private Platform findNextPlatform(Platform activePlatform) {
        return Platform.builder().build();
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
