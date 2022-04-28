package io.github.indrodevteam.indroEconomy;

public enum AccountConfigTags {
    MAX_SAFE_VALUE(null);

    final Object defaultValue;

    AccountConfigTags(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
}
