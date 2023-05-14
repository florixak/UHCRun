package me.florixak.uhcrun.customDrop;

import me.florixak.uhcrun.game.GameManager;
import me.florixak.uhcrun.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CustomDrop {

    private Material material;
    private List<Material> drops;
    private HashMap<Material, Integer> amount;
    private int exp;

    public CustomDrop(Material material, List<Material> drops, HashMap<Material, Integer> amount, int exp) {
        this.material = material;
        this.drops = drops;
        this.amount = amount;
        this.exp = exp;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean hasDrops() {
        return !getDrops().isEmpty() && getDrops() != null && !getDrops().contains(XMaterial.AIR.parseMaterial());
    }

    public List<Material> getDrops() {
        return drops;
    }

    public int getExp() {
        return exp;
    }

    public int getAmount(Material material) {
        if (amount.containsKey(material)) {
            return amount.get(material);
        }
        return 1;
    }

    public void dropItem(BlockBreakEvent event) {

        Player p = event.getPlayer();
        Block block = event.getBlock();
        Location loc = block.getLocation();

        Random ran = new Random();

        int exp = getExp();
        if (exp > 0) {
            p.giveExp(exp);
            GameManager.getGameManager().getSoundManager().playOreDestroySound(p);
        }

        if (hasDrops()) {
            block.setType(XMaterial.AIR.parseMaterial());
            event.setDropItems(false);
            event.setExpToDrop(0);

            Material drop = getDrops().get(ran.nextInt(getDrops().size()));
            int amount = getAmount(drop) != 0 ? ran.nextInt(getAmount(drop))+1 : 1;
            ItemStack drop_is = new ItemStack(drop, amount);

            Location location = loc.add(0.5, 0.5, 0.5);
            Bukkit.getWorld(loc.getWorld().getName()).dropItem(location, drop_is);
        }
    }
}
