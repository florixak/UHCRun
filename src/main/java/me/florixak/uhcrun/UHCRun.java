package me.florixak.uhcrun;

import me.florixak.uhcrun.action.ActionManager;
import me.florixak.uhcrun.commands.AnvilCommand;
import me.florixak.uhcrun.commands.CreatorModeCommand;
import me.florixak.uhcrun.commands.WorkbenchCommand;
import me.florixak.uhcrun.config.ConfigManager;
import me.florixak.uhcrun.config.ConfigType;
import me.florixak.uhcrun.listener.ChatListener;
import me.florixak.uhcrun.manager.*;
import me.florixak.uhcrun.inventory.InventoryManager;
import me.florixak.uhcrun.listener.InteractListener;
import me.florixak.uhcrun.listener.GameListener;
import me.florixak.uhcrun.listener.PlayerListener;
import me.florixak.uhcrun.manager.gameManager.GameManager;
import me.florixak.uhcrun.scoreboard.ScoreboardManager;
import me.florixak.uhcrun.sql.MySQL;
import me.florixak.uhcrun.sql.SQLGetter;
import me.florixak.uhcrun.utility.TeleportUtil;
import me.florixak.uhcrun.utility.TextUtil;
import me.florixak.uhcrun.utility.Utilities;
import me.florixak.uhcrun.utility.VanishUtil;
import me.florixak.uhcrun.utility.placeholderapi.PlaceholderExp;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class UHCRun extends JavaPlugin {

    /*
    TODO:
        - spectator teleport
        - custom craft menu
        - celkově doladit
    */


    private static Economy econ = null;
    private static LuckPerms luckPerms = null;
    public static UHCRun plugin;
    public MySQL SQL;
    public SQLGetter data;
    private GameManager gameManager;
    private ConfigManager configManager;
    private ScoreboardManager scoreboardManager;
    private LobbyManager lobbyManager;
    private BorderManager borderManager;
    private Utilities utilities;
    private ActionManager actionManager;
    private InventoryManager inventoryManager;
    private StatisticsManager statisticManager;
    private LevelManager levelManager;
    private KitsManager kitsManager;
    private TeleportUtil teleportUtil;
    private SoundManager soundManager;
    private TeamManager teamManager;
    private VanishUtil vanishUtil;

    @Override
    public void onEnable() {

        getLogger().info("Author: FloriXak");
        getLogger().info("Web: www.florixak.tk");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("Created 4fun");

        plugin = this;
        this.configManager = new ConfigManager();
        this.configManager.loadFiles(this);
        this.gameManager = new GameManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
        this.lobbyManager = new LobbyManager(this);
        this.borderManager = new BorderManager(this);
        this.utilities = new Utilities(this);
        this.actionManager = new ActionManager(this);
        this.inventoryManager = new InventoryManager();
        this.inventoryManager.onEnable(this);
        this.statisticManager = new StatisticsManager(this);
        this.levelManager = new LevelManager(this);
        this.kitsManager = new KitsManager();
        this.soundManager = new SoundManager(this);
        this.teamManager = new TeamManager(this);
        this.vanishUtil = new VanishUtil();

        this.SQL = new MySQL();
        this.data = new SQLGetter(this);

        this.teleportUtil = new TeleportUtil(this);

        registerAddons();
        connectToDatabase();
        registerRecipes();

        try {
            new CreatorModeCommand(this);
            new WorkbenchCommand(this);
            new AnvilCommand(this);
        } catch (Exception e) {
            getLogger().info(TextUtil.color("&cThere is error in commands!"));
            e.printStackTrace();
        }

        try {
            getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
            getServer().getPluginManager().registerEvents(new GameListener(this), this);
            getServer().getPluginManager().registerEvents(new InteractListener(this), this);
            getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        } catch (Exception e) {
            getLogger().info(TextUtil.color("&cThere is error in listeners!"));
            e.printStackTrace();
        }

        // getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getGame().runActivityRewards();
        getGame().runAutoBroadcast();
        getGame().updateScoreboard();
        getGame().checkBorder();

        try {
            gameManager.setOreSpawn();
            getLogger().info(TextUtil.color("&aAll ores are set!"));
        } catch (Exception e) {
            getLogger().info(TextUtil.color("&cThere is error in ore spawn!"));
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        this.SQL = new MySQL();
        this.data = new SQLGetter(this);

        SQL.disconnect();
    }

    private void registerAddons() {
        if (configManager.getFile(ConfigType.SETTINGS).getConfig().getBoolean("use-Vault", true)) {
            if (!setupEconomy()) {
                getLogger().info(TextUtil.color("&cNo economy plugin found. Disabling UHCRun."));
            }
            else {
                getLogger().info(TextUtil.color("&aVault plugin found."));
            }
        }

        if (configManager.getFile(ConfigType.SETTINGS).getConfig().getBoolean("use-LuckPerms", true)) {
            if (!setupLuckPerms()) {
                getLogger().info(TextUtil.color("&cLuckPerms plugin not found."));
            }
            else {
                getLogger().info(TextUtil.color("&aLuckPerms plugin found."));
            }
        }

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderExp(this).register();
        }
    }
    private void connectToDatabase() {
        if (configManager.getFile(ConfigType.SETTINGS).getConfig().getBoolean("MySQL.enabled", true)) {
            try {
                SQL.connect();
            } catch (ClassNotFoundException | SQLException e) {
                getLogger().info(TextUtil.color("&cDabatase is not connected!"));
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            if (SQL.isConnected()) {
                getLogger().info(TextUtil.color("&aDabatase is connected!"));
                data.createTable();
            }
        }
    }
    private void registerRecipes() {
        try {
            new RecipeManager();
            getLogger().info(TextUtil.color("&aAll recipes are loaded! (" + RecipeManager.recipes + ")"));
        } catch (Exception e) {
            getLogger().info(TextUtil.color("&cThere is error in recipes!"));
            e.printStackTrace();
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    private boolean setupLuckPerms() {
        for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
            if (plugin.getName().contains("LuckPerms")) {
                return true;
            }
        }

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) {
            return false;
        }
        luckPerms = provider.getProvider();
        return luckPerms != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
    public static LuckPerms getLuckPerms() {
        return luckPerms;
    }
    public GameManager getGame() {
        return gameManager;
    }
    public ConfigManager getConfigManager() {
        return configManager;
    }
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
    public Utilities getUtilities() { return utilities; }
    public LobbyManager getLobbyManager() { return lobbyManager; }
    public BorderManager getBorderManager() { return borderManager; }
    public ActionManager getActionManager() {
        return actionManager;
    }
    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
    public StatisticsManager getStatisticManager() {
        return statisticManager;
    }
    public LevelManager getLevelManager() {
        return levelManager;
    }
    public SoundManager getSoundManager() {
        return soundManager;
    }
    public TeamManager getTeamManager() {
        return teamManager;
    }
    public KitsManager getKitsManager() {
        return kitsManager;
    }
}