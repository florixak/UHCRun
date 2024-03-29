package me.florixak.uhcrun.listener;

import me.florixak.uhcrun.config.Messages;
import me.florixak.uhcrun.game.GameConst;
import me.florixak.uhcrun.game.GameManager;
import me.florixak.uhcrun.game.GameState;
import me.florixak.uhcrun.listener.events.GameKillEvent;
import me.florixak.uhcrun.manager.lobby.LobbyType;
import me.florixak.uhcrun.player.PlayerState;
import me.florixak.uhcrun.player.UHCPlayer;
import me.florixak.uhcrun.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    private final GameManager gameManager;

    public PlayerListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {

        Player p = event.getPlayer();
        event.setJoinMessage(null);

        UHCPlayer uhcPlayer = gameManager.getPlayerManager().getOrCreateUHCPlayer(p.getUniqueId());
        gameManager.getPlayerManager().addPlayer(uhcPlayer);

        if (gameManager.getPlayerManager().getOnlineList().size() >= GameConst.MAX_PLAYERS) {
            p.kickPlayer("Full game, sorry...");
            return;
        }

        if (gameManager.isPlaying()) {
            gameManager.getPlayerManager().setSpectator(uhcPlayer, PlayerState.SPECTATOR);
            return;
        }

        p.setGameMode(GameMode.ADVENTURE);
        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(20);
        p.giveExp(-p.getTotalExperience());
        p.setTotalExperience(0);
        p.setLevel(0);

        p.teleport(gameManager.getLobbyManager().getLocation(LobbyType.WAITING));

        uhcPlayer.clearInventory();
        gameManager.getKitsManager().getLobbyKit(uhcPlayer);

        Utils.broadcast(Messages.JOIN.toString()
                .replace("%player%", p.getDisplayName())
                .replace("%online%", String.valueOf(gameManager.getPlayerManager().getOnlineList().size())));
        p.sendMessage(Messages.PLAYERS_TO_START.toString()
                .replace("%min-players%", "" + GameConst.MIN_PLAYERS));
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {

        Player p = event.getPlayer();
        event.setQuitMessage(null);

        UHCPlayer uhcPlayer = gameManager.getPlayerManager().getUHCPlayer(p.getUniqueId());
        gameManager.getScoreboardManager().removeScoreboard(uhcPlayer.getPlayer());

        if (gameManager.getGameState().equals(GameState.LOBBY) || gameManager.getGameState().equals(GameState.STARTING)) {
            Utils.broadcast(Messages.QUIT.toString()
                    .replace("%player%", uhcPlayer.getName())
                    .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()-1)));
        } else {
            if (!gameManager.areStatsAddOnEnd()) {
                uhcPlayer.getData().addLose(1);
                uhcPlayer.getData().addDeaths(1);
            }
        }

        gameManager.getPlayerManager().removePlayer(uhcPlayer);
    }

    @EventHandler
    public void handleDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);

        UHCPlayer uhcKiller = null;
        if (event.getEntity().getKiller() instanceof Player) {
            uhcKiller = gameManager.getPlayerManager().getUHCPlayer(event.getEntity().getKiller().getUniqueId());
        }
        UHCPlayer uhcVictim = gameManager.getPlayerManager().getUHCPlayer(event.getEntity().getPlayer().getUniqueId());

        if (gameManager.isDeathChestEnabled()) {
            gameManager.getDeathChestManager().createDeathChest(event.getEntity().getPlayer(), event.getDrops());
        }

        event.getEntity().setHealth(20);
        event.getEntity().setFoodLevel(20);
        event.getEntity().setFireTicks(0);
        event.getEntity().getActivePotionEffects().clear();
        event.getEntity().spigot().respawn();

        Bukkit.getServer().getPluginManager().callEvent(new GameKillEvent(uhcKiller, uhcVictim));
        event.getDrops().clear();
    }

    @EventHandler
    public void handleItemDrop(PlayerDropItemEvent event) {
        UHCPlayer uhcPlayer = gameManager.getPlayerManager().getUHCPlayer(event.getPlayer().getUniqueId());

        if (!gameManager.isPlaying() || uhcPlayer.isDead() || gameManager.getGameState().equals(GameState.ENDING)) {
            event.setCancelled(true);
        }
    }

    @Deprecated
    @EventHandler
    public void handleItemPickUp(PlayerPickupItemEvent event) {
        UHCPlayer uhcPlayer = gameManager.getPlayerManager().getUHCPlayer(event.getPlayer().getUniqueId());

        if (!gameManager.isPlaying() || uhcPlayer.isDead() || gameManager.getGameState().equals(GameState.ENDING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handlePortalTeleport(PlayerTeleportEvent event) {
        if (!gameManager.isNetherAllowed() && event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            event.setCancelled(true);
        }
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            event.setCancelled(true);
        }
    }
}