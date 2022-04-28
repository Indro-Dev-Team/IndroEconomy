package io.github.indrodevteam.indroEconomy.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class PlayerAccount {
    UUID ownerID;
    int balance;
    int safe;
    Map<String, Object> accountConfig;

    public PlayerAccount(UUID ownerID, int balance, int safe, Map<String, Object> accountConfig) {
        this.ownerID = ownerID;
        this.balance = balance;
        this.safe = safe;
        this.accountConfig = accountConfig;
    }
}
