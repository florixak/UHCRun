package me.florixak.uhcrun.manager;

import me.florixak.uhcrun.config.ConfigType;
import me.florixak.uhcrun.game.GameManager;
import me.florixak.uhcrun.utils.ItemUtils;
import me.florixak.uhcrun.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeManager {

    private final GameManager gameManager;
    private final FileConfiguration recipe_config;

    private List<ShapedRecipe> recipes;
    public HashMap<String, Material> recipe;

    public RecipeManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.recipe_config = gameManager.getConfigManager().getFile(ConfigType.CUSTOM_RECIPES).getConfig();
        this.recipes = new ArrayList<>();

        this.recipe = new HashMap<>();
    }

    public void registerRecipes() {

        if (recipe_config.getConfigurationSection("custom-recipes") == null) return;

        for (String recipe : recipe_config.getConfigurationSection("custom-recipes").getKeys(false)) {
            ItemStack item = new ItemStack(XMaterial.matchXMaterial(recipe.toUpperCase()).get().parseMaterial(),
                    recipe_config.getInt("custom-recipes." + recipe + ".amount"));

            ShapedRecipe itemRecipe = new ShapedRecipe(item);

            itemRecipe.shape("ABC", "DEF", "GHI");

            itemRecipe.setIngredient('A', XMaterial.matchXMaterial(
                    recipe_config.getString("custom-recipes." + recipe + ".top-left", "BARRIER").toUpperCase()).get().parseMaterial());
            itemRecipe.setIngredient('B', XMaterial.matchXMaterial(
                    recipe_config.getString("custom-recipes." + recipe + ".top-middle", "BARRIER").toUpperCase()).get().parseMaterial());
            itemRecipe.setIngredient('C', XMaterial.matchXMaterial(
                    recipe_config.getString("custom-recipes." + recipe + ".top-right", "BARRIER").toUpperCase()).get().parseMaterial());
            itemRecipe.setIngredient('D', XMaterial.matchXMaterial(
                    recipe_config.getString("custom-recipes." + recipe + ".middle-left", "BARRIER").toUpperCase()).get().parseMaterial());
            itemRecipe.setIngredient('E', XMaterial.matchXMaterial(
                    recipe_config.getString("custom-recipes." + recipe + ".middle", "BARRIER").toUpperCase()).get().parseMaterial());
            itemRecipe.setIngredient('F', XMaterial.matchXMaterial(
                    recipe_config.getString("custom-recipes." + recipe + ".middle-right", "BARRIER").toUpperCase()).get().parseMaterial());
            itemRecipe.setIngredient('G', XMaterial.matchXMaterial(
                    recipe_config.getString("custom-recipes." + recipe + ".bottom-left", "BARRIER").toUpperCase()).get().parseMaterial());
            itemRecipe.setIngredient('H', XMaterial.matchXMaterial(
                    recipe_config.getString("custom-recipes." + recipe + ".bottom-middle", "BARRIER").toUpperCase()).get().parseMaterial());
            itemRecipe.setIngredient('I', XMaterial.matchXMaterial(
                    recipe_config.getString("custom-recipes." + recipe + ".bottom-right", "BARRIER").toUpperCase()).get().parseMaterial());

            Bukkit.getServer().addRecipe(itemRecipe);
            this.recipes.add(itemRecipe);
            System.out.println(recipe);
        }


    }

    private void goldenApple() {

        ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE);
        ShapedRecipe shapedRecipe = new ShapedRecipe(NamespacedKey.minecraft("light-gapple"), itemStack);

        shapedRecipe.shape(" G ", "GAG", " G ");
        shapedRecipe.setIngredient('G', Material.GOLD_INGOT);
        shapedRecipe.setIngredient('A', Material.APPLE);

        Bukkit.addRecipe(shapedRecipe);
        recipe.put("Golden Apple", Material.GOLDEN_APPLE);
    }

    private void anvil() {

        ItemStack itemStack = new ItemStack(Material.ANVIL);
        ShapedRecipe shapedRecipe = new ShapedRecipe(NamespacedKey.minecraft("light-anvil"), itemStack);

        shapedRecipe.shape("III", " B ", "III");
        shapedRecipe.setIngredient('B', Material.IRON_BLOCK);
        shapedRecipe.setIngredient('I', Material.IRON_INGOT);

        Bukkit.addRecipe(shapedRecipe);
        recipe.put("Light Anvil", Material.ANVIL);
    }

    private void fishingRod() {

        ItemStack itemStack = new ItemStack(Material.FISHING_ROD);
        ShapedRecipe shapedRecipe = new ShapedRecipe(NamespacedKey.minecraft("light-rod"), itemStack);

        shapedRecipe.shape("  T", " TS", "T  ");
        shapedRecipe.setIngredient('T', Material.STICK);
        shapedRecipe.setIngredient('S', Material.STRING);

        Bukkit.addRecipe(shapedRecipe);
        recipe.put("Light Fishing Rod", Material.FISHING_ROD);
    }

    private void woodenTools() {

        ItemStack itemStack1 = new ItemStack(XMaterial.WOODEN_AXE.parseItem());
        ItemUtils.addEnchant(itemStack1, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack1, Enchantment.DURABILITY, 3, false);

        ItemStack itemStack2 = new ItemStack(XMaterial.WOODEN_PICKAXE.parseItem());
        ItemUtils.addEnchant(itemStack2, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack2, Enchantment.DURABILITY, 3, false);

        ItemStack itemStack3 = new ItemStack(XMaterial.WOODEN_SHOVEL.parseItem());
        ItemUtils.addEnchant(itemStack3, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack3, Enchantment.DURABILITY, 3, false);

        ShapedRecipe shapedRecipe1 = new ShapedRecipe(NamespacedKey.minecraft("wood-axe"), itemStack1);
        shapedRecipe1.shape("WW ", "WS ", " S ");
        shapedRecipe1.setIngredient('W', XMaterial.OAK_PLANKS.parseMaterial());
        shapedRecipe1.setIngredient('S', XMaterial.STICK.parseMaterial());

        ShapedRecipe shapedRecipe2 = new ShapedRecipe(NamespacedKey.minecraft("wood-pickaxe"), itemStack2);
        shapedRecipe2.shape("WWW", " S ", " S ");
        shapedRecipe2.setIngredient('W', XMaterial.OAK_PLANKS.parseMaterial());
        shapedRecipe2.setIngredient('S', XMaterial.STICK.parseMaterial());

        ShapedRecipe shapedRecipe3 = new ShapedRecipe(NamespacedKey.minecraft("wood-shovel"), itemStack3);
        shapedRecipe3.shape(" W ", " S ", " S ");
        shapedRecipe3.setIngredient('W', XMaterial.OAK_PLANKS.parseMaterial());
        shapedRecipe3.setIngredient('S', XMaterial.STICK.parseMaterial());


        Bukkit.addRecipe(shapedRecipe1);
        Bukkit.addRecipe(shapedRecipe2);
        Bukkit.addRecipe(shapedRecipe3);
    }
    private void stoneTools() {

        ItemStack itemStack1 = new ItemStack(XMaterial.STONE_AXE.parseItem());
        ItemUtils.addEnchant(itemStack1, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack1, Enchantment.DURABILITY, 3, false);

        ItemStack itemStack2 = new ItemStack(XMaterial.STONE_PICKAXE.parseItem());
        ItemUtils.addEnchant(itemStack2, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack2, Enchantment.DURABILITY, 3, false);

        ItemStack itemStack3 = new ItemStack(XMaterial.STONE_SHOVEL.parseItem());
        ItemUtils.addEnchant(itemStack3, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack3, Enchantment.DURABILITY, 3, false);

        ShapedRecipe shapedRecipe1 = new ShapedRecipe(NamespacedKey.minecraft("stone-axe"), itemStack1);
        shapedRecipe1.shape("CC ", "CS ", " S ");
        shapedRecipe1.setIngredient('C', XMaterial.COBBLESTONE.parseMaterial());
        shapedRecipe1.setIngredient('S', XMaterial.STICK.parseMaterial());

        ShapedRecipe shapedRecipe2 = new ShapedRecipe(NamespacedKey.minecraft("stone-pickaxe"), itemStack2);
        shapedRecipe2.shape("CCC", " S ", " S ");
        shapedRecipe2.setIngredient('C', XMaterial.COBBLESTONE.parseMaterial());
        shapedRecipe2.setIngredient('S', XMaterial.STICK.parseMaterial());

        ShapedRecipe shapedRecipe3 = new ShapedRecipe(NamespacedKey.minecraft("stone-shovel"), itemStack3);
        shapedRecipe3.shape(" C ", " S ", " S ");
        shapedRecipe3.setIngredient('C', XMaterial.COBBLESTONE.parseMaterial());
        shapedRecipe3.setIngredient('S', XMaterial.STICK.parseMaterial());


        Bukkit.addRecipe(shapedRecipe1);
        Bukkit.addRecipe(shapedRecipe2);
        Bukkit.addRecipe(shapedRecipe3);
    }
    private void goldenTools() {

        ItemStack itemStack1 = new ItemStack(XMaterial.GOLDEN_AXE.parseItem());
        ItemUtils.addEnchant(itemStack1, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack1, Enchantment.DURABILITY, 3, false);

        ItemStack itemStack2 = new ItemStack(XMaterial.GOLDEN_PICKAXE.parseItem());
        ItemUtils.addEnchant(itemStack2, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack2, Enchantment.DURABILITY, 3, false);

        ItemStack itemStack3 = new ItemStack(XMaterial.GOLDEN_SHOVEL.parseItem());
        ItemUtils.addEnchant(itemStack3, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack3, Enchantment.DURABILITY, 3, false);

        ShapedRecipe shapedRecipe1 = new ShapedRecipe(NamespacedKey.minecraft("gold-axe"), itemStack1);
        shapedRecipe1.shape("GG ", "GS ", " S ");
        shapedRecipe1.setIngredient('G', XMaterial.GOLD_INGOT.parseMaterial());
        shapedRecipe1.setIngredient('S', XMaterial.STICK.parseMaterial());

        ShapedRecipe shapedRecipe2 = new ShapedRecipe(NamespacedKey.minecraft("gold-pickaxe"), itemStack2);
        shapedRecipe2.shape("GGG", " S ", " S ");
        shapedRecipe2.setIngredient('G', XMaterial.GOLD_INGOT.parseMaterial());
        shapedRecipe2.setIngredient('S', XMaterial.STICK.parseMaterial());

        ShapedRecipe shapedRecipe3 = new ShapedRecipe(NamespacedKey.minecraft("gold-shovel"), itemStack3);
        shapedRecipe3.shape(" G ", " S ", " S ");
        shapedRecipe3.setIngredient('G', XMaterial.GOLD_INGOT.parseMaterial());
        shapedRecipe3.setIngredient('S', XMaterial.STICK.parseMaterial());

        Bukkit.addRecipe(shapedRecipe1);
        Bukkit.addRecipe(shapedRecipe2);
        Bukkit.addRecipe(shapedRecipe3);
    }
    private void ironTools() {

        ItemStack itemStack1 = new ItemStack(XMaterial.IRON_AXE.parseItem());
        ItemUtils.addEnchant(itemStack1, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack1, Enchantment.DURABILITY, 3, false);

        ItemStack itemStack2 = new ItemStack(XMaterial.IRON_PICKAXE.parseItem());
        ItemUtils.addEnchant(itemStack2, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack2, Enchantment.DURABILITY, 3, false);

        ItemStack itemStack3 = new ItemStack(XMaterial.IRON_SHOVEL.parseItem());
        ItemUtils.addEnchant(itemStack3, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack3, Enchantment.DURABILITY, 3, false);

        ShapedRecipe shapedRecipe1 = new ShapedRecipe(NamespacedKey.minecraft("iron-axe"), itemStack1);
        shapedRecipe1.shape("II ", "IS ", " S ");
        shapedRecipe1.setIngredient('I', XMaterial.IRON_INGOT.parseMaterial());
        shapedRecipe1.setIngredient('S', XMaterial.STICK.parseMaterial());

        ShapedRecipe shapedRecipe2 = new ShapedRecipe(NamespacedKey.minecraft("iron-pickaxe"), itemStack2);
        shapedRecipe2.shape("III", " S ", " S ");
        shapedRecipe2.setIngredient('I', XMaterial.IRON_INGOT.parseMaterial());
        shapedRecipe2.setIngredient('S', XMaterial.STICK.parseMaterial());

        ShapedRecipe shapedRecipe3 = new ShapedRecipe(NamespacedKey.minecraft("iron-shovel"), itemStack3);
        shapedRecipe3.shape(" I ", " S ", " S ");
        shapedRecipe3.setIngredient('I', XMaterial.IRON_INGOT.parseMaterial());
        shapedRecipe3.setIngredient('S', XMaterial.STICK.parseMaterial());

        Bukkit.addRecipe(shapedRecipe1);
        Bukkit.addRecipe(shapedRecipe2);
        Bukkit.addRecipe(shapedRecipe3);
    }
    private void diamondTools() {

        ItemStack itemStack1 = new ItemStack(XMaterial.DIAMOND_AXE.parseItem());
        ItemUtils.addEnchant(itemStack1, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack1, Enchantment.DURABILITY, 3, false);

        ItemStack itemStack2 = new ItemStack(XMaterial.DIAMOND_PICKAXE.parseItem());
        ItemUtils.addEnchant(itemStack2, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack2, Enchantment.DURABILITY, 3, false);

        ItemStack itemStack3 = new ItemStack(XMaterial.DIAMOND_SHOVEL.parseItem());
        ItemUtils.addEnchant(itemStack3, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack3, Enchantment.DURABILITY, 3, false);

        ShapedRecipe shapedRecipe1 = new ShapedRecipe(NamespacedKey.minecraft("diamond-axe"), itemStack1);
        shapedRecipe1.shape("DD ", "DS ", " S ");
        shapedRecipe1.setIngredient('D', XMaterial.DIAMOND.parseMaterial());
        shapedRecipe1.setIngredient('S', XMaterial.STICK.parseMaterial());

        ShapedRecipe shapedRecipe2 = new ShapedRecipe(NamespacedKey.minecraft("diamond-pickaxe"), itemStack2);
        shapedRecipe2.shape("DDD", " S ", " S ");
        shapedRecipe2.setIngredient('D', XMaterial.DIAMOND.parseMaterial());
        shapedRecipe2.setIngredient('S', XMaterial.STICK.parseMaterial());

        ShapedRecipe shapedRecipe3 = new ShapedRecipe(NamespacedKey.minecraft("diamond-shovel"), itemStack3);
        shapedRecipe3.shape(" D ", " S ", " S ");
        shapedRecipe3.setIngredient('D', XMaterial.DIAMOND.parseMaterial());
        shapedRecipe3.setIngredient('S', XMaterial.STICK.parseMaterial());

        Bukkit.addRecipe(shapedRecipe1);
        Bukkit.addRecipe(shapedRecipe2);
        Bukkit.addRecipe(shapedRecipe3);
    }
    private void netheriteTools() {

        ItemStack itemStack1 = new ItemStack(XMaterial.NETHERITE_AXE.parseItem());
        ItemUtils.addEnchant(itemStack1, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack1, Enchantment.DURABILITY, 3, false);

        ItemStack itemStack2 = new ItemStack(XMaterial.NETHERITE_PICKAXE.parseItem());
        ItemUtils.addEnchant(itemStack2, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack2, Enchantment.DURABILITY, 3, false);

        ItemStack itemStack3 = new ItemStack(XMaterial.NETHERITE_SHOVEL.parseItem());
        ItemUtils.addEnchant(itemStack3, Enchantment.DIG_SPEED, 3, false);
        ItemUtils.addEnchant(itemStack3, Enchantment.DURABILITY, 3, false);

        ShapedRecipe shapedRecipe1 = new ShapedRecipe(NamespacedKey.minecraft("netherite-axe"), itemStack1);
        shapedRecipe1.shape("NN ", "NS ", " S ");
        shapedRecipe1.setIngredient('N', XMaterial.NETHERITE_INGOT.parseMaterial());
        shapedRecipe1.setIngredient('S', XMaterial.STICK.parseMaterial());

        ShapedRecipe shapedRecipe2 = new ShapedRecipe(NamespacedKey.minecraft("netherite-pickaxe"), itemStack2);
        shapedRecipe2.shape("NNN", " S ", " S ");
        shapedRecipe2.setIngredient('N', XMaterial.NETHERITE_INGOT.parseMaterial());
        shapedRecipe2.setIngredient('S', XMaterial.STICK.parseMaterial());

        ShapedRecipe shapedRecipe3 = new ShapedRecipe(NamespacedKey.minecraft("netherite-shovel"), itemStack3);
        shapedRecipe3.shape(" N ", " S ", " S ");
        shapedRecipe3.setIngredient('N', XMaterial.NETHERITE_INGOT.parseMaterial());
        shapedRecipe3.setIngredient('S', XMaterial.STICK.parseMaterial());

        Bukkit.addRecipe(shapedRecipe1);
        Bukkit.addRecipe(shapedRecipe2);
        Bukkit.addRecipe(shapedRecipe3);
    }
}
