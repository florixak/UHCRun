package me.florixak.uhcrun.listener;

import me.florixak.uhcrun.config.ConfigType;
import me.florixak.uhcrun.config.Messages;
import me.florixak.uhcrun.game.GameManager;
import me.florixak.uhcrun.player.PlayerManager;
import me.florixak.uhcrun.player.UHCPlayer;
import me.florixak.uhcrun.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class ChatListener implements Listener {

    private List<String> blockedCommands;

    private final GameManager gameManager;
    private final FileConfiguration config;

    public ChatListener(GameManager gameManager) {
        this.gameManager = gameManager;
        this.config = gameManager.getConfigManager().getFile(ConfigType.SETTINGS).getConfig();
    }

    @EventHandler
    public void handlePlayerChat(AsyncPlayerChatEvent event){
        PlayerManager pm = gameManager.getPlayerManager();

        Player p = event.getPlayer();
        UHCPlayer uhcPlayer = pm.getUHCPlayer(event.getPlayer().getUniqueId());

        String format = gameManager.isPlaying() ? (!uhcPlayer.isDead() ? config.getString("settings.chat.in-game-format")
                : config.getString("settings.chat.dead-format"))
                : config.getString("settings.chat.lobby-format");

        if (format == null || format.isEmpty()) return;

        event.setCancelled(true);

        String playerName = uhcPlayer.getName();
        String lp_prefix = uhcPlayer.getLuckPermsPrefix();
        String playerLevel = String.valueOf(uhcPlayer.getData().getUHCLevel());
        String message = p.hasPermission("uhcrun.color-chat") ? TextUtils.color(event.getMessage()) : event.getMessage();
        String team = uhcPlayer.hasTeam() ? TextUtils.color(uhcPlayer.getTeam().getDisplayName()) : "";

        format = format
                .replace("%player%", playerName)
                .replace("%message%", message)
                .replace("%luckperms-prefix%", TextUtils.color(lp_prefix))
                .replace("%uhc-level%", TextUtils.color(playerLevel))
                .replace("%team%", team);

        String finalFormat = format;
        if (!gameManager.isPlaying()) {
            pm.getOnlineList().forEach(uhcPlayers -> uhcPlayer.sendMessage(TextUtils.color(finalFormat)));
            return;
        }
        if (uhcPlayer.isSpectator()) {
            pm.getSpectatorList().stream().filter(UHCPlayer::isOnline).forEach(uhcPlayers -> uhcPlayer.sendMessage(TextUtils.color(finalFormat)));
            return;
        }
        if (!message.startsWith("!") && gameManager.isTeamMode()) {
            uhcPlayer.getTeam().sendMessage(TextUtils.color(format));
            return;
        }
        pm.getOnlineList().forEach(uhcPlayers -> uhcPlayers.sendMessage(TextUtils.color("&6[GLOBAL] ") + finalFormat.replaceFirst("!", "")));
    }

    @EventHandler
    public void handlePlayerCommand(PlayerCommandPreprocessEvent event){

        this.blockedCommands = config.getStringList("settings.chat.blocked-commands");

        Player p = event.getPlayer();
        String msg = event.getMessage();
        String args[] = msg.split(" ");

        if (this.blockedCommands.contains(event.getMessage().toLowerCase())) {
            event.setCancelled(true);
            p.sendMessage(Messages.NO_PERM.toString());
        }

        if (Bukkit.getServer().getHelpMap().getHelpTopic(args[0]) == null){
            event.setCancelled(true);
            p.sendMessage(Messages.INVALID_CMD.toString());
        }
    }

}