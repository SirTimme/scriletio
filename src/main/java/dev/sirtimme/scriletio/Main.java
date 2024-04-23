package dev.sirtimme.scriletio;

import dev.sirtimme.scriletio.events.EventHandler;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class Main {
    public static void main(String[] args) {
        JDABuilder.createLight(System.getenv("TOKEN"))
                  .addEventListeners(new EventHandler())
                  .setActivity(Activity.playing("Silentium"))
                  .build();
    }
}