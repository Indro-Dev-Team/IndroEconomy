package io.github.indrodevteam.indroEconomy.objects;

import lombok.Data;

import java.util.UUID;

@Data
public class PlayerEconomyModel {

    private final String id;

    private UUID playerUUID;
    private long wallet;
    private long bank;
    private long maxBank;
    private long currentXP;
    private long nextXP;
    private int xpLevel;

    public PlayerEconomyModel(UUID playerUUID,
                              long wallet,
                              long bank,
                              long maxBank,
                              long currentXP,
                              long nextXP,
                              int xpLevel) {
        this.playerUUID = playerUUID;
        this.wallet = wallet;
        this.bank = bank;
        this.maxBank = maxBank;
        this.currentXP = currentXP;
        this.nextXP = nextXP;
        this.xpLevel = xpLevel;

        this.id = UUID.randomUUID().toString();
    }
}
