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
    List<Platform> platforms = new ArrayList<>();
    HashMap<Integer, Integer> unlockedPlatforms = new HashMap<>();
    private Platform activePlatform;
    private Platform nextLockedPlatform;
    private int currentPoints = 0;
    private Platform nextPlatform;
    private Platform previousPlatform;


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
        nextLockedPlatform = platforms.get(unlockedPlatforms.size() + 1);

        while (!isLatestPlatform()) {
            if (activePlatform.getIndex() != platforms.size()) {
                nextPlatform = platforms.get(activePlatform.getIndex() + 1);
            }
            if (activePlatform.getIndex() != 0) {
                previousPlatform = platforms.get(activePlatform.getIndex() - 1);
            }

            if (isNextPlatformLocked()) {
                if (currentPoints >= nextLockedPlatform.getCost()) {
                    moveToNextPlatform(nextPlatform);
                } else if (activePlatform.getIndex() == 0) {
                    moveToNextPlatform(nextPlatform);
                } else {
                    moveToPreviousPlatform(previousPlatform);
                }
            } else if (pointsCalculator(activePlatform, currentPoints, nextLockedPlatform)) {
                moveToNextPlatform(nextPlatform);
            } else if (activePlatform.getIndex() == 0) {
                moveToNextPlatform(nextPlatform);
            } else {
                moveToPreviousPlatform(previousPlatform);
            }
        }
    }

    private boolean isNextPlatformLocked() {
        if (!unlockedPlatforms.containsKey(nextPlatform.getIndex())) {
            return true;
        }
        return false;
    }


    private void moveToNextPlatform(Platform nextplatform) {
        if (isNextPlatformLocked()) {
            unlockedPlatforms.put(nextplatform.getIndex(), nextplatform.getCost());
            currentPoints = currentPoints - nextplatform.getCost();
            if (unlockedPlatforms.size() != platforms.size() - 1) {
                nextLockedPlatform = platforms.get(unlockedPlatforms.size() + 1);
            }
        } else {
            currentPoints = currentPoints + nextplatform.getCost();
        }
        activePlatform = platforms.get(activePlatform.getIndex() + 1);
        jumpTo(activePlatform);

    }

    private void moveToPreviousPlatform(Platform previousplatform) {
        if (activePlatform.getIndex() > 0) {
            activePlatform = previousplatform;
            currentPoints = currentPoints + activePlatform.getCost();
            jumpTo(activePlatform);
        }
    }

    private boolean isLatestPlatform() {
        int platformsize = platforms.size();
        return activePlatform.getIndex() + 1 >= platformsize;
    }

    /**
     * Calculates if the amount of possible points if moving forward is enough
     * to unlock next level.
     */
    private boolean pointsCalculator(Platform activeplatform, Integer currentPoints, Platform nextLockedPlatform) {
        int differenceToLocked = nextLockedPlatform.getIndex() - activeplatform.getIndex() - 1;
        int possiblePointsToGet = 0;
        for (int i = 1; i < differenceToLocked; i++) {
            possiblePointsToGet = possiblePointsToGet + platforms.get(activePlatform.getIndex() + i).getCost();
        }
        if (possiblePointsToGet + currentPoints > nextLockedPlatform.getCost()) {
            return true;
        }
        return false;


    }


    /**
     * Reads platforms from csv file and returns the as list.
     *
     * @return platforms - Platforms as list
     */
    private List<Platform> readPlatforms() throws IOException, URISyntaxException {
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
