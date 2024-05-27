package me.florixak.uhcrun.game;

import me.florixak.uhcrun.config.ConfigType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class GameValues {

    private static final GameManager gameManager = GameManager.getGameManager();
    private static final FileConfiguration config = gameManager.getConfigManager().getFile(ConfigType.SETTINGS).getConfig();

    public static int ERROR_INT_VALUE = -1;

    public static final World GAME_WORLD = Bukkit.getWorld("world");

    public static final String CHAT_FORMAT_IN_GAME = getConfigString("settings.chat.in-game-format", "");
    public static final String CHAT_FORMAT_DEAD = getConfigString("settings.chat.dead-format", "");
    public static final String CHAT_FORMAT_LOBBY = getConfigString("settings.chat.lobby-format", "");
    public static final List<String> CHAT_BLOCKED_COMMANDS = getConfigStringList("settings.chat.blocked-commands");

    public static final boolean TEAM_MODE = getConfigBoolean("settings.teams.team-mode", true);
    public static final boolean KITS_ENABLED = getConfigBoolean("settings.kits.enabled", true);
    public static final boolean PERKS_ENABLED = getConfigBoolean("settings.perks.enabled", true);
    public static final boolean FRIENDLY_FIRE = getConfigBoolean("settings.teams.friendly-fire", false);
    public static final boolean TELEPORT_AFTER_MINING = getConfigBoolean("settings.game.teleport-after-mining", true);
    public static final boolean CUSTOM_DROPS_ENABLED = getConfigBoolean("settings.game.custom-drops", true);
    public static final boolean STATS_ADD_ON_END = getConfigBoolean("settings.statistics.add-up-game-ends", false);
    public static final boolean DEATH_CHESTS_ENABLED = getConfigBoolean("settings.death-chest.enabled", true);
    public static final boolean EXPLOSIONS_DISABLED = getConfigBoolean("settings.game.no-explosions", true);
    public static final boolean RANDOM_DROPS_ENABLED = getConfigBoolean("settings.game.random-drops", false);
    public static final boolean NETHER_ENABLED = getConfigBoolean("settings.game.allow-nether", false);
    public static final boolean PROJECTILE_HIT_HP_ENABLED = getConfigBoolean("settings.game.projectile-hit-hp", false);

    public static final boolean CAN_USE_VAULT = config.getBoolean("settings.addons.use-Vault", true);
    public static final boolean CAN_USE_LUCKPERMS = config.getBoolean("settings.addons.use-LuckPerms", true);
    public static final boolean CAN_USE_PLACEHOLDERAPI = config.getBoolean("settings.addons.use-PlaceholderAPI", true);
    public static final boolean CAN_USE_PROTOCOLLIB = config.getBoolean("settings.addons.use-ProtocolLib", true);

    public static final int TEAM_SIZE = getConfigInt("settings.teams.max-size", 3);
    public static final int MIN_PLAYERS = getConfigInt("settings.game.min-players", 2);

    public static final int STARTING_COUNTDOWN = getConfigInt("settings.game.countdowns.starting", 20);
    public static final int MINING_COUNTDOWN = getConfigInt("settings.game.countdowns.mining", 600);
    public static final int PVP_COUNTDOWN = getConfigInt("settings.game.countdowns.pvp", 600);
    public static final int DEATHMATCH_COUNTDOWN = getConfigInt("settings.game.countdowns.deathmatch", 300);
    public static final int ENDING_COUNTDOWN = getConfigInt("settings.game.countdowns.ending", 20);

    public static final double BASE_REWARD = getConfigDouble("settings.rewards.base-reward", 100);
    public static final double REWARD_COEFFICIENT = getConfigDouble("settings.rewards.reward-coefficient", 1);
    public static final double REQUIRED_EXP_MULTIPLIER = getConfigDouble("settings.player-level.required-exp-multiplier", 1.2);

    public static final double MONEY_FOR_WIN = getConfigDouble("settings.rewards.win.money", 0);
    public static final double UHC_EXP_FOR_WIN = getConfigInt("settings.rewards.win.uhc-exp", 0);

    public static final double MONEY_FOR_LOSE = getConfigDouble("settings.rewards.lose.money", 0);
    public static final double UHC_EXP_FOR_LOSE = getConfigInt("settings.rewards.lose.uhc-exp", 0);

    public static final double MONEY_FOR_KILL = getConfigDouble("settings.rewards.kill.money", 0);
    public static final double UHC_EXP_FOR_KILL = getConfigDouble("settings.rewards.kill.uhc-exp", 0);
    public static final double EXP_FOR_KILL = getConfigDouble("settings.rewards.kill.exp", 0);

    public static final double MONEY_FOR_ASSIST = getConfigDouble("settings.rewards.assist.money", 0);
    public static final double UHC_EXP_FOR_ASSIST = getConfigDouble("settings.rewards.assist.uhc-exp", 0);
    public static final double EXP_FOR_ASSIST = getConfigDouble("settings.rewards.assist.exp", 0);

    public static final boolean BROADCAST_ENABLED = getConfigBoolean("settings.auto-broadcast.enabled", true);
    public static final int BROADCAST_INTERVAL = getConfigInt("settings.auto-broadcast.period", 300);

    public static final boolean ACTIVITY_REWARDS_ENABLED = getConfigBoolean("settings.rewards.activity.enabled", true);
    public static final int ACTIVITY_REWARDS_INTERVAL = getConfigInt("settings.rewards.activity.period", 300);
    public static final double ACTIVITY_REWARDS_MONEY = getConfigDouble("settings.rewards.activity.money", 10);
    public static final double ACTIVITY_REWARDS_EXP = getConfigDouble("settings.rewards.activity.uhc-exp", 20);

    private static boolean getConfigBoolean(String path, boolean def) {
      return config.getBoolean(path, def);
    }
    private static String getConfigString(String path, String def) {
      return config.getString(path, def);
    }
    private static int getConfigInt(String path, int def) {
        return config.getInt(path, def);
    }
    private static double getConfigDouble(String path, double def) {
        return config.getDouble(path, def);
    }
    private static List<String> getConfigStringList(String path) {
        return config.getStringList(path);
    }
}

