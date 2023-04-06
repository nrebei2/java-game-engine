package opengl;

import util.Application;
import game.Root;
import util.Config;

public class Main {
    public static void main(String[] args) {
        Config config = new Config();
        config.height = 600;
        config.width = 800;
        new Application(new Root(), config);
    }
}
