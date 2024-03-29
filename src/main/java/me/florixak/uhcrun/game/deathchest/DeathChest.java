package me.florixak.uhcrun.game.deathchest;

import me.florixak.uhcrun.UHCRun;
import me.florixak.uhcrun.game.GameManager;
import me.florixak.uhcrun.player.UHCPlayer;
import me.florixak.uhcrun.tasks.DeathChestExpire;
import me.florixak.uhcrun.utils.TextUtils;
import me.florixak.uhcrun.utils.TimeUtils;
import me.florixak.uhcrun.utils.XSeries.XMaterial;
import me.florixak.uhcrun.utils.hologram.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DeathChest {

    private final UHCPlayer uhcPlayer;
    private final Location loc;
    private List<ItemStack> contents;

    private String title;
    private Hologram hologram;

    private final boolean expire;
    private final DeathChestExpire deathChestExpire;

    public DeathChest(UHCPlayer uhcPlayer, Location loc, String title, List<ItemStack> contents, boolean expire) {
        this.uhcPlayer = uhcPlayer;
        this.loc = loc;
        this.title = title;
        this.contents = contents;

        this.deathChestExpire = new DeathChestExpire(this);
        this.expire = expire;

        if (this.title.isEmpty() || this.title == null) {
            this.title = "Death Chest";
        }
        createChest();
    }
    public void createChest() {
        Bukkit.getWorld(loc.getWorld().getName()).getBlockAt(loc).setType(XMaterial.CHEST.parseMaterial());
        BlockState state = loc.getBlock().getState();

        Chest chest = (Chest) state;
        chest.setCustomName(TextUtils.color(this.title));
        addHologram();

        if (!getContents().isEmpty()) {
            for (ItemStack item : getContents()) {
                chest.getInventory().addItem(item);
            }
        }

        if (canExpire()) {
            startExpiring();
        }
    }

    public UHCPlayer getPlayer() {
        return this.uhcPlayer;
    }

    public boolean canExpire() {
        return this.expire;
    }
    public void startExpiring() {
        this.deathChestExpire.runTaskTimer(UHCRun.getInstance(), 20L, 20L);
    }
    public DeathChestExpire getExpireTask() {
        return this.deathChestExpire;
    }
    public String getExpireTime() {
        return TimeUtils.getFormattedTime(getExpireTask().getExpireTime());
    }

    public List<ItemStack> getContents() {
        return this.contents;
    }

    public void addHologram() {
        String text = GameManager.getGameManager().getDeathChestManager().getHologramText()
                .replace("%player%", uhcPlayer.getName())
                .replace("%countdown%", getExpireTime());

        this.hologram = new Hologram(text, loc.add(0.5, -0.5, 0.5));
    }
    public Hologram getHologram() {
        return this.hologram;
    }

    public void removeChest() {

        Block block = Bukkit.getWorld(loc.getWorld().getName()).getBlockAt(loc.add(0.5, 1, 0.5));
        block.setType(XMaterial.AIR.parseMaterial());
        getHologram().remove();

        if (getExpireTask() != null && !getExpireTask().isCancelled()) {
            getExpireTask().cancel();
        }

        // TODO create explode

        /*for (ItemStack itemStack : getContents()) {
            if (itemStack == null || itemStack.equals(XMaterial.AIR.parseMaterial())) return;
            Location location = loc.add(0.5, 0.5, 0.5);
            Bukkit.getWorld(loc.getWorld().getName()).dropItem(location, itemStack);
        }*/
    }
}