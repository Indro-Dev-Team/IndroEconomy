package io.github.indrodevteam.indroEconomy.config;

import org.bukkit.configuration.file.YamlConfiguration;

public enum ConfigTags {
    MOBS_ENABLED("mobs.enabled", false),
    MONEY_SYMBOL("moneySymbol", '$'),
    MONEY_DEFAULT_AMOUNT("money.defaultAmount", 200),
    DEATH_LOSS_PERCENT("money.deathLossPercent", 25),
    DEATH_KILLER_GAIN("money.killerGetsDeathMoney", true),
    DEATH_KILLER_GAIN_PERCENT("money.killerGetsDeathMoney", 0.10),
    POVERTY_PERMITTED("deathByPoverty", false),
    MONEY_MINIMUM("money.minimum", -1000000),
    SEPARATOR("separator", ","),
    SYMBOL_POSITION("symbolPosition", "prefix");

    final Object defaultValue;
    final String path;
    final YamlConfiguration config;

    ConfigTags(String path, Object defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
        config = new ConfigManager("config.yml", true).config;
    }

    public Object get() {
        if (config.contains(path)) {
            return config.get(path);
        }
        return defaultValue;
    }
}
