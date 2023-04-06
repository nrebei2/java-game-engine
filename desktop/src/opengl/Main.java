package opengl;

import util.Application;
import game.Root;
import util.Config;

public class Main {
    public static void main(String[] args) {
        Config config = new Config();
        config.height = 1200;
        config.width = 1600;
        new Application(new Root(), config);
    }
}
