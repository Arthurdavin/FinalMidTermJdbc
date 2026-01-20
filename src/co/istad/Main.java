package co.istad;

import co.istad.config.DbConfig;
import co.istad.view.View;

public class Main {
    public static void main(String[] args) {
        DbConfig.init();
        new View().run();
    }
}