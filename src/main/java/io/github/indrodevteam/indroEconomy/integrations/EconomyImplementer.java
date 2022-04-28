package io.github.indrodevteam.indroEconomy.integrations;

import io.github.indrodevteam.indroEconomy.EconomyUtils;
import io.github.indrodevteam.indroEconomy.config.ConfigTags;
import io.github.indrodevteam.indroEconomy.data.BankAccount;
import io.github.indrodevteam.indroEconomy.datamanager.BankDataUtils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class EconomyImplementer implements Economy {
    private final EconomyUtils eco;
    public EconomyImplementer() {
        this.eco = new EconomyUtils();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "IndroEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return true;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return EconomyUtils.format(((int) v));
    }

    @Override
    public String currencyNamePlural() {
        return currencyNameSingular();
    }

    @Override
    public String currencyNameSingular() {
        return (String) ConfigTags.MONEY_SYMBOL.get();
    }

    @Override
    public boolean hasAccount(String s) {
        OfflinePlayer player = Bukkit.getPlayer(s);
        return hasAccount(player);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return eco.hasAccount(offlinePlayer);
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        OfflinePlayer player = Bukkit.getPlayer(s);
        return hasAccount(player);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return hasAccount(offlinePlayer);
    }

    @Override
    public double getBalance(String s) {
        OfflinePlayer player = Bukkit.getPlayer(s);
        return getBalance(player);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return eco.getAccount(offlinePlayer).getBalance();
    }

    @Override
    public double getBalance(String s, String s1) {
        OfflinePlayer player = Bukkit.getPlayer(s);
        return getBalance(player);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return getBalance(offlinePlayer);
    }

    @Override
    public boolean has(String s, double v) {
        OfflinePlayer player = Bukkit.getPlayer(s);
        if (player != null) {
            return eco.hasAmount(player, (int) v);
        }
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return eco.hasAmount(offlinePlayer, (int) v);
    }

    @Override
    public boolean has(String s, String s1, double v) {
        OfflinePlayer player = Bukkit.getPlayer(s);
        if (player != null) {
            return eco.hasAmount(player, (int) v);
        }
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return has(offlinePlayer, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        OfflinePlayer player = Bukkit.getPlayer(s);
        if (player != null) {
            return withdrawPlayer(player, v);
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not exist!");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        eco.changeBalance(offlinePlayer, (int) (-1 * v));
        return new EconomyResponse(v, eco.getAccount(offlinePlayer).getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return withdrawPlayer(s, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return withdrawPlayer(offlinePlayer, v);
    }

    ///////////////////////////////////////////////////////////////////////////
    
    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        OfflinePlayer player = Bukkit.getPlayer(s);
        if (player != null) {
            return depositPlayer(player, v);
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not exist!");
    }
    
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        eco.changeBalance(offlinePlayer, (int) v);
        return new EconomyResponse(v, eco.getAccount(offlinePlayer).getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return depositPlayer(s, v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return depositPlayer(offlinePlayer, v);
    }

    // Bank methods
    
    @Override
    public EconomyResponse createBank(String bankName, String playerName) {
        OfflinePlayer player = Bukkit.getPlayer(playerName);
        if (player != null) {
            return createBank(bankName, playerName);
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not exist!");
    }

    @Override
    public EconomyResponse createBank(String bankName, OfflinePlayer offlinePlayer) {
        if (eco.createBank(offlinePlayer, bankName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account already exists!");
    }

    @Override
    public EconomyResponse deleteBank(String bankName) {
        if (eco.deleteBank(bankName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account does not exist!");
    }

    @Override
    public EconomyResponse bankBalance(String bankName) {
        return new EconomyResponse(0, eco.getBankBalance(bankName), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse bankHas(String bankName, double value) {
        int bankAmount = eco.getBankBalance(bankName);
        if (bankAmount >= value) {
            return new EconomyResponse(value, bankAmount, EconomyResponse.ResponseType.SUCCESS, null);
        }
        return new EconomyResponse(value, bankAmount, EconomyResponse.ResponseType.FAILURE, "This bank does not have enough money!");
    }

    @Override
    public EconomyResponse bankWithdraw(String bankName, double value) {
        eco.changeBankBalance(bankName, (int) (-1 * value));
        return new EconomyResponse(value, eco.getBankBalance(bankName), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse bankDeposit(String bankName, double value) {
        eco.changeBankBalance(bankName, (int) value);
        return new EconomyResponse(value, eco.getBankBalance(bankName), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse isBankOwner(String bankName, String player) {
        OfflinePlayer offlinePlayer = Bukkit.getPlayer(player);
        BankAccount account = eco.getBankAccount(bankName);
        if (offlinePlayer != null && account != null) {
            if (account.getOwnerID().equals(offlinePlayer.getUniqueId())) {
                return new EconomyResponse(0, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
            } else {
                return new EconomyResponse(0, account.getBalance(), EconomyResponse.ResponseType.FAILURE, "This player is not the bank owner!");
            }
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account, or player does not exist!");
    }

    @Override
    public EconomyResponse isBankOwner(String bankName, OfflinePlayer offlinePlayer) {
        BankAccount account = eco.getBankAccount(bankName);
        if (account != null) {
            if (account.getOwnerID().equals(offlinePlayer.getUniqueId())) {
                return new EconomyResponse(0, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
            }
            return new EconomyResponse(0, account.getBalance(), EconomyResponse.ResponseType.FAILURE, "This player is not the bank owner!");
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account, or player does not exist!");
    }

    @Override
    public EconomyResponse isBankMember(String bankName, String player) {
        OfflinePlayer offlinePlayer = Bukkit.getPlayer(player);
        BankAccount account = eco.getBankAccount(bankName);
        if (offlinePlayer != null && account != null) {
            if (account.getMembers().contains(offlinePlayer.getUniqueId())) {
                return new EconomyResponse(0, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
            } else {
                return new EconomyResponse(0, account.getBalance(), EconomyResponse.ResponseType.FAILURE, "This player is not a bank member!");
            }
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account, or player does not exist!");
    }

    @Override
    public EconomyResponse isBankMember(String bankName, OfflinePlayer offlinePlayer) {
        BankAccount account = eco.getBankAccount(bankName);
        if (offlinePlayer != null && account != null) {
            if (account.getMembers().contains(offlinePlayer.getUniqueId())) {
                return new EconomyResponse(0, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
            } else {
                return new EconomyResponse(0, account.getBalance(), EconomyResponse.ResponseType.FAILURE, "This player is not a bank member!");
            }
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account, or player does not exist!");
    }

    @Override
    public List<String> getBanks() {
        List<String> bankNames = new ArrayList<>();
        for (int i = 0; i < BankDataUtils.readAllBanks().size(); i++) {
            bankNames.add(BankDataUtils.readAllBanks().get(i).getBankName());
        }
        return bankNames;
    }

    @Override
    public boolean createPlayerAccount(String s) {
        OfflinePlayer player = Bukkit.getPlayer(s);
        if (player != null) {
            return createPlayerAccount(player);
        }
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return eco.createAccount(offlinePlayer, (int) ConfigTags.MONEY_DEFAULT_AMOUNT.get()) != null;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return createPlayerAccount(s);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return createPlayerAccount(offlinePlayer);
    }
}
