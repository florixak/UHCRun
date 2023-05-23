package me.florixak.uhcrun.utils;

import me.florixak.uhcrun.config.ConfigType;
import me.florixak.uhcrun.game.GameManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Random;

public class TeleportUtils {

    private static GameManager gameManager;
    private static FileConfiguration config;

    public static HashSet<Material> bad_blocks = new HashSet<>();

    public TeleportUtils(GameManager gameManager) {
        this.gameManager = gameManager;
        this.config = gameManager.getConfigManager().getFile(ConfigType.SETTINGS).getConfig();
    }

    static {
        bad_blocks.add(Material.LAVA);
        bad_blocks.add(Material.FIRE);
        bad_blocks.add(Material.CACTUS);
        bad_blocks.add(Material.WATER);
    }

    public static Location generateLocation(){

        Random random = new Random();

        World world = gameManager.getGameWorld();
        double x = random.nextInt(((int) gameManager.getBorderManager().getSize()/2)-10);
        double y = 150.0;
        double z = random.nextInt(((int) gameManager.getBorderManager().getSize()/2)-10);

        Location randomLocation = new Location(world, x, y, z);
        y = randomLocation.getWorld().getHighestBlockYAt(randomLocation);
        randomLocation.setY(y);

        return randomLocation;
    }

    public static Location getSafeLocation(){

        Location randomLocation = generateLocation();

        while (!isLocationSafe(randomLocation)){
            randomLocation = generateLocation();
        }
        return randomLocation;
    }

    public static boolean isLocationSafe(Location location){

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);

        return !(bad_blocks.contains(below.getType())) || (block.getType().isSolid()) || (above.getType().isSolid());
    }

}
