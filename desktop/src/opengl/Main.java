package opengl;

import game.Root;
import util.Application;
import util.Config;

public class Main {
    public static void main(String[] args) {
        Config config = new Config();
        config.height = 1200;
        config.width = 1600;
        config.fullScreen = true;
        new Application(new Root(), config);
    }
}
