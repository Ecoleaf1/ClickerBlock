package net.wigoftime.clickycommand;

import net.wigoftime.clickycommand.commands.ClickerBlockCommand;
import net.wigoftime.clickycommand.events.PlayerInteract;
import org.bukkit.Bukkit;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main main;

    public void onEnable() {
        main = this;
        BlockFiles.getBlocks();
        Listener listener = new Listener() {};
        Bukkit.getPluginManager().registerEvent(PlayerInteractEvent.class, listener, EventPriority.LOW, new PlayerInteract(), this);
        getCommand("ClickerBlock").setExecutor(new ClickerBlockCommand());
    }

    public void onDisable() {

    }
}
