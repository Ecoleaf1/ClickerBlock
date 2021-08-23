package net.wigoftime.clickycommand;

import net.wigoftime.clickycommand.objects.ClickerBlock;
import net.wigoftime.clickycommand.objects.CommandType;
import net.wigoftime.clickycommand.objects.DeleteFileResult;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BlockFiles {
    public static final String path = Main.main.getDataFolder().getAbsolutePath()+"/Blocks";

    public static void getBlocks() {
        File folder = new File(path);

        if (!folder.exists()) folder.mkdirs();

        Bukkit.getLogger().info(path);

        for (File file : folder.listFiles()) {
            YamlConfiguration yaml =  YamlConfiguration.loadConfiguration(file);
            try {
                String[] locationBits = file.getName().split(" ");

                World world = Bukkit.getWorld(locationBits[0]);

                if (world == null) {
                    Bukkit.getLogger().info("ERROR: WORLD NOT FOUND");
                    Bukkit.getLogger().info("INVALID CustomBlock File: " + file.getName());
                    continue;
                }

                Location location =
                        new Location(
                            world,
                            Integer.parseInt(locationBits[1]),
                            Integer.parseInt(locationBits[2]),
                            Integer.parseInt(locationBits[3].split(".yml")[0])
                        );
                List<String> commands = yaml.getStringList("Commands");
                CommandType type = CommandType.valueOf(yaml.getString("Run by").toUpperCase());
                new ClickerBlock(location, type, commands);

            } catch (NumberFormatException invalidNumberException) {
                Bukkit.getLogger().info("ERROR: A custom block has an invalid location");
                Bukkit.getLogger().info("Block naming: WORLD_NAME X Y Z.yml");
                Bukkit.getLogger().info("INVALID CustomBlock File: " + file.getName());
            } catch (ArrayIndexOutOfBoundsException indexOutOfBoundsException) {
                Bukkit.getLogger().info("ERROR: A custom block file has too few detail");
                Bukkit.getLogger().info("Block naming: WORLD_NAME X Y Z.yml");
                Bukkit.getLogger().info("INVALID CustomBlock File: " + file.getName());
            }
        }
    }

    public static boolean createFile(@NotNull Location location, @NotNull CommandType type, @NotNull List<String> commands) {
        final String fileName = String.format("%s %d %d %d.yml",
                                location.getWorld().getName(),
                                location.getBlockX(),
                                location.getBlockY(),
                                location.getBlockZ());
        File blockFile = new File(path+"/"+fileName);

        if (blockFile.exists()) return false;

        try {
            blockFile.createNewFile();

            YamlConfiguration yaml = new YamlConfiguration();
            yaml.set("Run by", type.toString());
            yaml.set("Commands", commands);
            yaml.save(blockFile);

            return true;
        } catch (IOException exception) {
            Bukkit.getLogger().info("ERROR: File creation error on creating Custom Block File");
        }

        return false;
    }

    public static @NotNull DeleteFileResult deleteFile(@NotNull Location location) {
        final String fileName = String.format("%s %d %d %d.yml",
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ());
        File blockFile = new File(path+"/"+fileName);

        if (!blockFile.exists()) return DeleteFileResult.NONEXISTENT;

        try {
             if (blockFile.delete())
                 return DeleteFileResult.SUCCESS;
             else
                 return DeleteFileResult.FAILED;
        } catch (SecurityException exception) {
            exception.printStackTrace();
            Bukkit.getLogger().info("ERROR: CAN NOT DELETE CLICKER BLOCK " + fileName);
            Bukkit.getLogger().info("CHECK IF FILE HAS PERMISSION");
            return DeleteFileResult.SECURITY_EXCEPTION;
        }
    }
}
