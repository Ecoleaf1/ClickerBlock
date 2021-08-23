package net.wigoftime.clickycommand.events;

import net.wigoftime.clickycommand.objects.ClickerBlock;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.EventExecutor;

public class PlayerInteract implements EventExecutor {
    @Override
    public void execute(Listener listener, Event event) throws EventException {
        PlayerInteractEvent interactEvent = (PlayerInteractEvent) event;

        ClickerBlock selectedCustomBlock;
        switch (interactEvent.getAction()) {
            case LEFT_CLICK_BLOCK:
            case RIGHT_CLICK_BLOCK:
                selectedCustomBlock = ClickerBlock.get(interactEvent.getClickedBlock().getLocation());
                break;
            default:
                selectedCustomBlock = null;
        }

        if (selectedCustomBlock == null) return;

        interactEvent.setCancelled(true);

        switch (selectedCustomBlock.commandType) {
            case PLAYER:
                selectedCustomBlock.getFormatCommands(interactEvent.getPlayer()).
                        forEach(interactEvent.getPlayer()::performCommand);
                break;
            case CONSOLE:
                selectedCustomBlock.getFormatCommands(interactEvent.getPlayer())
                        .forEach(command -> {
                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
                });
                break;
        }
    }
}
