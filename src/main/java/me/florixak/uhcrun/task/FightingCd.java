package me.florixak.uhcrun.task;

import me.florixak.uhcrun.UHCRun;
import me.florixak.uhcrun.config.ConfigType;
import me.florixak.uhcrun.config.Messages;
import me.florixak.uhcrun.manager.SoundManager;
import me.florixak.uhcrun.manager.gameManager.GameState;
import me.florixak.uhcrun.player.UHCPlayer;
import me.florixak.uhcrun.utils.TimeUtils;
import me.florixak.uhcrun.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

public class FightingCd extends BukkitRunnable {

    private UHCRun plugin;
    private FileConfiguration config;
    public static int count;

    public FightingCd(UHCRun plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager().getFile(ConfigType.SETTINGS).getConfig();
        this.count = config.getInt("fighting-countdown");
    }

    @Override
    public void run() {

        if (count <= 0) {
            cancel();
            plugin.getGame().setGameState(GameState.DEATHMATCH);
            return;
        }
        if (count <= 10) {
            Utils.broadcast(Messages.DEATHMATCH_STARTING.toString()
                    .replace("%countdown%", "" + TimeUtils.getFormattedTime(count)));
            for (UHCPlayer player : plugin.getPlayerManager().getPlayersList()) {
                SoundManager.playDMStarts(player.getPlayer());
            }
        }
        plugin.getGame().checkGame();
        plugin.getBorderManager().setSize(plugin.getBorderManager().getSize()-plugin.getBorderManager().getSpeed());
        count--;
    }
}