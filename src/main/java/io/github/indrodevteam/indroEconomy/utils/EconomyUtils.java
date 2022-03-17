package io.github.indrodevteam.indroEconomy.utils;

import io.github.indrodevteam.indroEconomy.IndroEconomy;
import io.github.indrodevteam.indroEconomy.objects.EconomyStorageUtil;
import io.github.indrodevteam.indroEconomy.objects.PlayerEconomyModel;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.MetadataValueAdapter;

@SuppressWarnings("unused")
public class EconomyUtils {
    private PlayerEconomyModel profile;

    public EconomyUtils(Player player) {
        FileConfiguration config = new ConfigManager("config.yml", true).config;
        this.profile = EconomyStorageUtil.findAccount(player.getUniqueId());
        if (profile != null) {
            if (profile.getWallet() <= config.getLong("money.minimum")) {
                player.setMetadata("deathCausePoverty",  new FixedMetadataValue(IndroEconomy.getInstance(), true));
                player.setHealth(0);
            }
        }
    }

    public void changeWallet(long value) {
        profile.setWallet(profile.getWallet() + value);
        saveToProfile();
    }

    /**
     * Modifies the value of the bank
     * @param value The value that is to be changed
     * @return true if the transfer was successful, false if not
     */
    public boolean changeBank(long value) {
        if (profile.getBank() + value <= profile.getMaxBank()) {
            profile.setBank(profile.getBank() + value);
            saveToProfile();
            return true;
        }
        return false;
    }

    /**
     * Transfers from one money storage type to another.
     * @param type The destination storage type
     * @param valueTransferred The amount transferred
     * @return true if successful, false if not
     */
    public boolean transferToStorage(StorageType type, long valueTransferred) {
        switch (type) {
            case BANK -> {
                if (profile.getWallet() >= valueTransferred && profile.getBank() + valueTransferred <= profile.getMaxBank()) {
                    changeWallet(valueTransferred * -1);
                    changeBank(valueTransferred);
                    return true;
                }
            }
            case WALLET -> {
                if (profile.getBank() >= valueTransferred) {
                    changeWallet(valueTransferred);
                    changeBank(valueTransferred * -1);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean transferToPlayer(Player target, long valueTransferred) {
        PlayerEconomyModel targetProfile = EconomyStorageUtil.findAccount(target.getUniqueId());
        if (profile.getWallet() < valueTransferred || targetProfile == null) {
            return false;
        } else {
            profile.setWallet(profile.getWallet() - valueTransferred);
            targetProfile.setWallet(profile.getWallet() + valueTransferred);
            saveToProfile();
            return true;
        }
    }


    public void saveToProfile() {
        EconomyStorageUtil.updateAccount(profile.getId(), profile);
    }

    public static String format(long amount) {
        // typical config tools
        final String symbol = new ConfigManager("config.yml", true).config.getString("moneySymbol");
        final String separator = new ConfigManager("config.yml", true).config.getString("separator");
        final char[] chars = String.valueOf(amount).toCharArray();
        long initialCount;
        String symbolPosition = new ConfigManager("config.yml", true).config.getString("symbolPosition");
        if (symbolPosition == null) {
            Bukkit.getLogger().info("Defaulting to prefix formatting");
            symbolPosition = "prefix";
        }

        switch (chars.length % 3) {
            case 0 -> initialCount = 0;
            case 1 -> initialCount = 1;
            case 2 -> initialCount = 2;
            default -> throw new IllegalStateException("Unexpected value: " + chars.length % 3);
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {
            boolean topped = (i+1) >= chars.length;
            stringBuilder.append(chars[i]);
            if (topped) break;
            boolean multiple3 = ((i+1) % 3) - initialCount == 0;

            if (multiple3) {
                stringBuilder.append(separator);
            }
        }
        if (symbolPosition.equalsIgnoreCase("prefix")) {
            stringBuilder.insert(0, symbol);
        } else if (symbolPosition.equalsIgnoreCase("suffix")){
            stringBuilder.append(symbol);
        } else {
            return null;
        }

        return stringBuilder.toString();
    }

    public PlayerEconomyModel getProfile() {
        return profile;
    }

    public void setProfile(PlayerEconomyModel profile) {
        this.profile = profile;
    }

    public enum StorageType {
        BANK,
        WALLET
    }
}

