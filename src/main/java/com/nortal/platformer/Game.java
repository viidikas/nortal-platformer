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
            Platform nextplatform = platforms.get(activePlatform.getIndex() + 1);

            //Which platform to move, forward or back?
            //Based on how many points are needed to unlock next level. If you have enough points to unlock next level, move on.
            // If you have moved back one step(unlocked level - 2 for example), then does moving
            //forward again give enough points to unlock the locked level. If it doesnt,
            // I will move back one more step to collect more points and then
            //go into the cycle again.
            if (calculator(activePlatform, currentPoints) && canIMoveToNextPlatform(nextplatform)) {
                moveToNextPlatform(nextplatform);
            } else {
                Platform previousplatform = platforms.get(activePlatform.getIndex() - 1);
                moveToPreviousPlatform(previousplatform);
            }
        }
    }

    private boolean canIMoveToNextPlatform(Platform platform) {
        if (unlockedPlatforms.containsKey(platform.getIndex())) {
            return true;
        } else return !unlockedPlatforms.containsKey(platform.getIndex()) && platform.getCost() <= currentPoints;
    }


    private void moveToNextPlatform(Platform nextplatform) {

        if (!isNextPlatformLocked()) {
            currentPoints = currentPoints - nextplatform.getCost();
            unlockedPlatforms.put(activePlatform.getIndex(), activePlatform.getCost());
        } else {
            currentPoints = currentPoints + nextplatform.getCost();
        }

    }

    private boolean isLatestPlatform() {
        int platformsize = platforms.size();
        return activePlatform.getIndex() + 1 >= platformsize;
    }

    private boolean isNextPlatformLocked() {
        return unlockedPlatforms.containsKey(activePlatform.getIndex() + 1);
    }

    private void moveToPreviousPlatform(Platform previousplatform) {
        if (activePlatform.getIndex() > 0) {
            activePlatform = previousplatform;
            currentPoints = currentPoints + activePlatform.getCost();
        }

    }

    private boolean calculator(Platform activeplatform, Integer currentPoints) {
        int differenceToLocked = unlockedPlatforms.size() - activeplatform.getIndex();
        int possiblePointsToGet = 0;
        int nextLockedPlatform = unlockedPlatforms.size() + 1;

        for (Integer i = 1; i < differenceToLocked; i++) {
            possiblePointsToGet = platforms.get(activePlatform.getIndex() + i).getCost();
        }
        return possiblePointsToGet + currentPoints > nextLockedPlatform;
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
