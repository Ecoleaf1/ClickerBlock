package net.wigoftime.clickycommand.objects;

import net.wigoftime.clickycommand.BlockFiles;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickerBlock {
    private static @NotNull Map<Location, ClickerBlock> customBlocks = new HashMap<Location, ClickerBlock>();
    public final @NotNull CommandType commandType;
    private final @NotNull List<String> commands;

    public final @NotNull Location location;

    public ClickerBlock(@NotNull Location location, @NotNull CommandType type, @NotNull List<String> commands) {
        this.location = location;
        this.commandType = type;
        this.commands = commands;
        customBlocks.put(location, this);
    }

    public @NotNull DeleteFileResult delete() {
        DeleteFileResult result = BlockFiles.deleteFile(location);

        synchronized (customBlocks) {
            customBlocks.remove(location);
        }

        return result;
    }

    public @Nullable List<String> getFormatCommands(@NotNull Player player) {
        List<String> formatCommands = new ArrayList<String>(commands.size());
        commands.forEach(rawCommand -> {
            rawCommand = rawCommand.replaceAll("%DISPLAYNAME%", player.getDisplayName());

            rawCommand = rawCommand.replaceAll("%UUID%", player.getUniqueId().toString());
            formatCommands.add(rawCommand);
        });

        return formatCommands;
    }

    public @NotNull List<String> getRawCommands(@NotNull Player player) {
        return commands;
    }

    public static @Nullable ClickerBlock get(Location location) {
        synchronized (customBlocks) {
            return customBlocks.get(location);
        }
    }

    public static void clearExisting() {
        synchronized (customBlocks) {
            customBlocks.clear();
        }
    }
}
