package dev.joshpope.multihardcore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.List;

public class Events implements Listener {
    private int lives = 4;
    private MultiHardCore plugin;
    private boolean matchedEnabled;
    private boolean lastDeath;

    private int matchedHP = 20;

    public Events(MultiHardCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!matchedEnabled) {
            lives -= 1;
            event.setDeathMessage(ChatColor.RED + event.getDeathMessage() + " " + lives + " lives remain!");
            if (lives == 0) {
                matchedEnabled = true;
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if((Player) event.getEntity() != player){
                        player.setHealth(20);
                        player.setFoodLevel(20);
                    }
                }
            }
        }else{
            lastDeath = true;
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        if(lastDeath){
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
        }else{
            if(lives==0){
                plugin.getServer().broadcastMessage(ChatColor.GREEN+"Last Life all players being restored to full HP/Hunger");
            }
        }
    }

    @EventHandler
    public void onLoseHP(EntityDamageEvent event) {
        if (matchedEnabled) {
            if(event.getCause() == EntityDamageEvent.DamageCause.CUSTOM){
                event.setCancelled(false);
            }else{
                event.setCancelled(true);
                if (event.getEntity() instanceof Player) {
                    Player player = (Player) event.getEntity();
                    matchedHP -= event.getDamage();
                    for(Player p: plugin.getServer().getOnlinePlayers()){
                        p.damage(event.getDamage());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLoseHunger(FoodLevelChangeEvent event) {
        if (matchedEnabled) {
            if (event.getEntity() instanceof Player) {
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    if (p != (Player) event.getEntity()) {
                        p.setFoodLevel(event.getFoodLevel());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onRegainHP(EntityRegainHealthEvent event) {
        if (matchedEnabled) {
            if(event.getRegainReason() == EntityRegainHealthEvent.RegainReason.CUSTOM){
                event.setCancelled(false);
            }else{
                event.setCancelled(true);
                if (event.getEntity() instanceof Player) {
                    matchedHP += event.getAmount();
                    for(Player p: plugin.getServer().getOnlinePlayers()){
                        p.setHealth(matchedHP);
                    }
                }
            }
        }
    }
}