package net.wigoftime.clickycommand.commands;

import net.wigoftime.clickycommand.BlockFiles;
import net.wigoftime.clickycommand.objects.CommandType;
import net.wigoftime.clickycommand.objects.ClickerBlock;
import net.wigoftime.clickycommand.objects.SubcommandUsage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class ClickerBlockCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            displayUsage(sender, SubcommandUsage.HELP);

            return true;
        }

        switch (args[0].toLowerCase()) {
            case "add":
                if (args.length < 3) {
                    displayUsage(sender, SubcommandUsage.ADD);
                    break;
                }
                add(sender, args);
                break;
            case "remove":
                remove(sender);
                break;
            case "reload":
                reload(sender);
                break;
            default:
                displayUsage(sender, SubcommandUsage.HELP);
        }
        return true;
    }

    public static void add(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        RayTraceResult result = player.rayTraceBlocks(5.0);
        if (result == null) {
            player.sendMessage(ChatColor.DARK_RED + "Sorry, but block not found");
            return;
        }

        Block targetBlock = result.getHitBlock();
        if (targetBlock == null) {
            player.sendMessage(ChatColor.DARK_RED + "Sorry, but block not found");
            return;
        }

        List<String> commands = new LinkedList<String>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 2; index < args.length; index++) {
            if (args[index].equalsIgnoreCase("$AND$")) {
                commands.add(stringBuilder.toString());
                stringBuilder = new StringBuilder();
                continue;
            }

            stringBuilder.append(args[index]);

            if (index+1 < args.length)
                stringBuilder.append(" ");
        }
        commands.add(stringBuilder.toString());

        Location location = targetBlock.getLocation();
        CommandType type = CommandType.valueOf(args[1].toUpperCase());
        if (BlockFiles.createFile(location, type, commands)) {
            player.sendMessage(ChatColor.GREEN + "ClickerBlock created");
            new ClickerBlock(location, type, commands);
        } else
            player.sendMessage(ChatColor.DARK_RED + "Failed to create clickerBlock created");
    }

    public static void remove(@NotNull CommandSender sender) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        RayTraceResult result = player.rayTraceBlocks(5.0);
        if (result == null) {
            player.sendMessage(ChatColor.DARK_RED + "Sorry, but block not found");
            return;
        }

        Block targetBlock = result.getHitBlock();
        if (targetBlock == null) {
            player.sendMessage(ChatColor.DARK_RED + "Sorry, but block not found");
            return;
        }

        ClickerBlock targetCustomBlock = ClickerBlock.get(result.getHitBlock().getLocation());
        if (targetCustomBlock == null) {
            player.sendMessage(ChatColor.DARK_RED + "Sorry, but ClickerBlock not found");
            return;
        }

        switch (targetCustomBlock.delete()) {
            case SUCCESS:
                player.sendMessage(ChatColor.RED+"ClickerBlock removed");
                break;
            case NONEXISTENT:
                player.sendMessage(ChatColor.DARK_RED+"Sorry, but ClickerBlock not found");
                break;
            case SECURITY_EXCEPTION:
                player.sendMessage(ChatColor.DARK_RED+"Failed to delete file: Security Exception");
                player.sendMessage(ChatColor.DARK_RED+ "Contact the server operator if the correct files permissions at set");
                break;
            case FAILED:
                player.sendMessage(ChatColor.DARK_RED+"Failed to delete ClickerBlock");
                break;
        }
    }

    public static void reload(CommandSender sender) {
        ClickerBlock.clearExisting();
        BlockFiles.getBlocks();

        sender.sendMessage(ChatColor.GREEN + "Clickerblocks reloaded");
    }

    public static void displayUsage(CommandSender sender, SubcommandUsage usage) {
        switch (usage) {
            case ADD:
                sender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY + "/clickerblock add {PLAYER/CONSOLE} [Command 1, Command 2, ..]");
                break;
            case REMOVE:
                sender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY + "/clickerblock remove");
                break;
            case HELP:
                sender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY + "Clickerblocks help");
                sender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY + "/clickerblock add {PLAYER/CONSOLE} [Command 1, Command 2, etc] - Add clickerblock");
                sender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY + "/clickerblock remove - Remove clickerblock by looking");
                sender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY + "/clickerblock reload - reloads the plugin");
                break;
        }
    }
}
