package io.github.indrodevteam.indroEconomy.menus;

import io.github.indrodevteam.indroEconomy.objects.PlayerEconomyModel;
import io.github.indrodevteam.indroEconomy.utils.EconomyUtils;
import io.github.indrodevteam.indroEconomy.utils.LanguageLoader;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.heads.SkullCreator;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class OpShopMenu extends Menu {
    public OpShopMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "OP Shop";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) throws MenuManagerNotSetupException, MenuManagerException {
        ItemStack itemStack = inventoryClickEvent.getCurrentItem();
        if (itemStack == null) return;
        if (itemStack.getItemMeta() == null) return;
        String itemName = itemStack.getItemMeta().getDisplayName();
        Player player = (Player) inventoryClickEvent.getWhoClicked();

        EconomyUtils eco = new EconomyUtils(player);
        PlayerEconomyModel profile;
        try {
            profile = eco.getProfile();
        } catch (NullPointerException e) {
            player.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_ACCOUNT_NOT_EXISTING);
            return;
        }

        int extraEnchChance;
        int overlevelChance;
        int maxEnchants;
        String boughtItem = "null";
        
        switch (itemName) {
            case "Tier 1" -> {
                if (profile.getWallet() >= 2500) {
                    eco.changeWallet(-2500);

                    //init chance gates
                    extraEnchChance = 25;
                    overlevelChance = 100;
                    maxEnchants = 3;

                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    ItemStack enchBook = generateEnchants(book, maxEnchants, overlevelChance, extraEnchChance);
                    player.getInventory().addItem(enchBook);
                    boughtItem = "Tier 1 Enchanted Book";
                } else {
                    player.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_NO_MONEY);
                    return;
                }
            }

            case "Tier 2" -> {
                if (profile.getWallet() >= 16500) {
                    eco.changeWallet(-16500);

                    //init chance gates
                    extraEnchChance = 50;
                    overlevelChance = 100;
                    maxEnchants = 4;

                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    ItemStack enchBook = generateEnchants(book, maxEnchants, overlevelChance, extraEnchChance);
                    player.getInventory().addItem(enchBook);
                    boughtItem = "Tier 2 Enchanted Book";
                } else {
                    player.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_NO_MONEY);
                    return;
                }
            }

            case "Tier 3" -> {
                if (profile.getWallet() >= 24550) {
                    eco.changeWallet(-24500);

                    //init chance gates
                    extraEnchChance = 75;
                    overlevelChance = 100;
                    maxEnchants = 5;

                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    ItemStack enchBook = generateEnchants(book, maxEnchants, overlevelChance, extraEnchChance);
                    player.getInventory().addItem(enchBook);
                    boughtItem = "Tier 1 Enchanted Book";
                } else {
                    player.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_NO_MONEY);
                    return;
                }
            }
            case "Loot Crate" -> {
                if (profile.getWallet() >= 24550) {
                    eco.changeBank(-24500);

                    ItemStack crate = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/e34a3619dc66fc5f940f69aa331e588b5285f6e2e9981b8f93b99916c294b48");
                    ItemMeta crateMeta = crate.getItemMeta();
                    assert crateMeta != null;
                    crateMeta.setDisplayName(ChatColor.AQUA + "Loot Crate");
                    List<String> lore = new ArrayList<>();
                    lore.add(ColorTranslator.translateColorCodes("&5This crate can give you good, &kor bad luck!"));
                    lore.add(ColorTranslator.translateColorCodes("&5Interact with this item to open it!"));
                    lore.add(ChatColor.DARK_PURPLE + "Costs " + EconomyUtils.format(24500));
                    crateMeta.setLore(lore);
                    crateMeta.setLocalizedName("Loot Crate");
                    crate.setItemMeta(crateMeta);

                    player.getInventory().addItem(crate);
                    boughtItem = "Loot Box";
                }
            }
        }
        player.sendMessage("Bought a " + boughtItem + "!");
    }

    @Override
    public void setMenuItems() {
        ItemStack tierOne = makeItem(Material.ENCHANTED_BOOK, "Tier 1",
                ChatColor.BOLD + "Costs $2,500",
                        "Guaranteed max level enchants.",
                        "25% chance for an extra enchant, for a max of 3");
        ItemStack tierTwo = makeItem(Material.ENCHANTED_BOOK, "Tier 2",
                ChatColor.BOLD + "Costs $16,500",
                        "Guaranteed max level enchants.",
                        "50% chance for an extra enchant, for a max of 4");
        ItemStack tierThree = makeItem(Material.ENCHANTED_BOOK, "Tier 3",
                ChatColor.BOLD + "Costs $24,550",
                        "Guaranteed max level enchants.",
                        "75% chance for an extra enchant, for a max of 5.");

        ItemStack crate = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/e34a3619dc66fc5f940f69aa331e588b5285f6e2e9981b8f93b99916c294b48");
        ItemMeta crateMeta = crate.getItemMeta();
        assert crateMeta != null;
        crateMeta.setDisplayName("Loot Crate");
        List<String> lore = new ArrayList<>();
        lore.add(ColorTranslator.translateColorCodes("&5This crate can give you good, &kor bad luck!"));
        lore.add(ColorTranslator.translateColorCodes("&5Interact with this item to open it!"));
        lore.add(ChatColor.DARK_PURPLE + "Costs " + EconomyUtils.format(24500));
        crateMeta.setLore(lore);
        crateMeta.setLocalizedName("Loot Crate");
        crate.setItemMeta(crateMeta);

        inventory.setItem(3, tierOne);
        inventory.setItem(4, tierTwo);
        inventory.setItem(5, tierThree);
        inventory.setItem(7, crate);

        setFillerGlass();
    }


    private ItemStack generateEnchants(ItemStack enchantedItem, int maxEnch, int overLevelChance, int extraEnchantChance) {
        int enchCount = 0;
        int percentRoll;

        // enchants that break at lvl 5
        ArrayList<Enchantment> blackListedEnchants = new ArrayList<>();
        blackListedEnchants.add(Enchantment.CHANNELING);
        blackListedEnchants.add(Enchantment.ARROW_FIRE);
        blackListedEnchants.add(Enchantment.ARROW_INFINITE);
        blackListedEnchants.add(Enchantment.MULTISHOT);
        blackListedEnchants.add(Enchantment.SILK_TOUCH);
        blackListedEnchants.add(Enchantment.WATER_WORKER);
        blackListedEnchants.add(Enchantment.DEPTH_STRIDER);
        blackListedEnchants.add(Enchantment.MENDING);
        blackListedEnchants.add(Enchantment.BINDING_CURSE);
        blackListedEnchants.add(Enchantment.VANISHING_CURSE);

        EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) enchantedItem.getItemMeta();
        Random random = new Random();

        if (itemMeta == null) return null;

        do {
            enchCount++;
            Enchantment enchantment = Enchantment.values()[(int) (Math.random()*Enchantment.values().length)];
            int maxEnchLvl;
            if (isEnchOverleveled(overLevelChance) && !blackListedEnchants.contains(enchantment)) {
                maxEnchLvl = 5;
            } else {
                maxEnchLvl = enchantment.getMaxLevel();
            }
            itemMeta.addStoredEnchant(enchantment, maxEnchLvl, true);
            percentRoll = random.nextInt(100);
        } while (percentRoll <= extraEnchantChance && enchCount != maxEnch);

        // set the enchanted items to the item
        enchantedItem.setItemMeta(itemMeta);
        return enchantedItem;
    }


    private boolean isEnchOverleveled(int chance) {
        int roll = new Random().nextInt(100);
        return roll <= chance;
    }
}
