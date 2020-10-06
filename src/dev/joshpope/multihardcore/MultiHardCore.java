package dev.joshpope.multihardcore;


import org.bukkit.plugin.java.JavaPlugin;

public class MultiHardCore extends JavaPlugin {
    //Lives - 4,
    //If 0 lives matched health

    private int numberOfLifes = 4;
    private Events events = new Events(this);

    @Override
    public void onEnable() {
        getLogger().info("Multi Hard Core has been enabled");
        this.getServer().getPluginManager().registerEvents(events, this);

    }

    @Override
    public void onDisable() {
        getLogger().info("Multi Hard Core has been disabled");
    }
}
