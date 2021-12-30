package com.nortal.platformer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Game {

    private final String gameFile;
    private final Integer points = 500;
    private Platform activePlatform;
    public Game(String gameFile) {
        this.gameFile = gameFile;
    }

    public static void main(String[] args) throws IOException {
        Game game = new Game("C:\\dev\\WinterUniversity_2022\\WinterUniversity_2022\\java-platformer\\src\\main\\resources\\platforms.csv");
        game.run();
    }

    public void run() throws IOException {
        List<Platform> platforms = readPlatforms();

        // TODO: Implement your mighty algorithm and jump to oblivion.
//        activePlatform = platforms.get(0);
//        Platform nextPlatform = findNextPlatform(activePlatform);
//        jumpTo(nextPlatform);

    }

    /**
     * Reads platforms from csv file and returns the as list.
     *
     * @return platforms - Platforms as list
     */
    private List<Platform> readPlatforms() throws IOException {
        List<Platform> platforms = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(gameFile));

        for (int i = 1; i < lines.size(); i++) {
            String[] lineparts = lines.get(i).split(", ");
            Platform platform = Platform.builder()
                    .index(Integer.parseInt(lineparts[0]))
                    .cost(Integer.parseInt(lineparts[1]))
                    .build();
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
