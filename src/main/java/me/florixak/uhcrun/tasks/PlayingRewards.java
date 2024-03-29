package me.florixak.uhcrun.tasks;

import me.florixak.uhcrun.config.ConfigType;
import me.florixak.uhcrun.config.Messages;
import me.florixak.uhcrun.game.GameManager;
import me.florixak.uhcrun.player.UHCPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayingRewards extends BukkitRunnable {

    private final GameManager gameManager;
    private final FileConfiguration config;
    private final String path;

    public PlayingRewards(GameManager gameManager) {
        this.gameManager = gameManager;
        this.config = gameManager.getConfigManager().getFile(ConfigType.SETTINGS).getConfig();
        this.path = "settings.rewards.playing-time";
    }

    @Override
    public void run() {
        if (gameManager.isPlaying()) {
            for (UHCPlayer uhcPlayer : gameManager.getPlayerManager().getAliveList()) {
                double money = config.getDouble(path + ".money");
                double player_exp = config.getDouble(path + ".player-exp");

                uhcPlayer.getData().depositMoney(money);
                uhcPlayer.getData().addUHCExp(player_exp);

                uhcPlayer.sendMessage(Messages.REWARDS_PER_TIME.toString()
                        .replace("%money-per-time%", String.valueOf(money))
                        .replace("%xp-per-time%", String.valueOf(player_exp)));
            }
        }
    }
}
