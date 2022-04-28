package io.github.indrodevteam.indroEconomy;

import io.github.indrodevteam.indroEconomy.config.ConfigTags;
import io.github.indrodevteam.indroEconomy.data.BankAccount;
import io.github.indrodevteam.indroEconomy.data.PlayerAccount;
import io.github.indrodevteam.indroEconomy.datamanager.BankDataUtils;
import io.github.indrodevteam.indroEconomy.datamanager.PlayerDataUtils;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyUtils {
    ///////////////////////////////////////////////////////////////////////////
    // Player Account methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Create an account that the player can use
     * @param owner The player target for creating the account
     * @param defaultBalance The value
     * @return
     */
    public PlayerAccount createAccount(OfflinePlayer owner, int defaultBalance) {
        Map<String, Object> configTagsObjectMap = new HashMap<>();
        for (AccountConfigTags tag : AccountConfigTags.values()) {
            configTagsObjectMap.put(tag.toString(), tag.defaultValue);
        }

        return PlayerDataUtils.createAccount(owner.getUniqueId(), 0, defaultBalance, configTagsObjectMap);
    }

    /**
     * Deletes a player account
     * @param owner The player target for creating the account
     * @return True if the account existed, and is deleted; False if the account didn't exist
     */
    public boolean deleteAccount(OfflinePlayer owner) {
        if (PlayerDataUtils.readAccount(owner.getUniqueId()) != null) {
            PlayerDataUtils.deleteAccount(owner.getUniqueId());
            return true;
        }
        return false;
    }

    public void setBalance(OfflinePlayer owner, int value) {
        PlayerAccount playerAccount = PlayerDataUtils.readAccount(owner.getUniqueId());
        if (playerAccount != null) {
            playerAccount.setBalance(value);
            PlayerDataUtils.updateAccount(owner.getUniqueId(), playerAccount);
        }
    }

    public void setSafe(OfflinePlayer owner, int value) {
        PlayerAccount playerAccount = PlayerDataUtils.readAccount(owner.getUniqueId());
        if (playerAccount != null) {
            playerAccount.setSafe(value);
            PlayerDataUtils.updateAccount(owner.getUniqueId(), playerAccount);
        }
    }

    public boolean depositAccount(OfflinePlayer owner, int value) {
        PlayerAccount playerAccount = PlayerDataUtils.readAccount(owner.getUniqueId());
        if (playerAccount != null) {
            int balance = playerAccount.getBalance();
            int safe = playerAccount.getSafe();

            Double maxSafe = (Double) playerAccount.getAccountConfig().get(AccountConfigTags.MAX_SAFE_VALUE.toString());

            if (balance >= value) {
                // if max safe does not have a value
                if (maxSafe == null) {
                    balance -= value;
                    safe += value;

                    playerAccount.setBalance(balance);
                    playerAccount.setSafe(safe);
                    return true;
                }
                // if max safe has a value, and it's bigger than the total it'll receive
                if (safe + value <= maxSafe) {
                    balance -= value;
                    safe += value;

                    playerAccount.setBalance(balance);
                    playerAccount.setSafe(safe);
                    PlayerDataUtils.updateAccount(owner.getUniqueId(), playerAccount);
                    return true;
                }
                return false; // ERROR REASON: safe is too small
            }
            return false; // ERROR REASON: balance doesn't contain that much
        }
        return false; // ERROR REASON: account doesn't exist
    }

    public boolean withdrawAccount(OfflinePlayer owner, int value) {
        PlayerAccount playerAccount = PlayerDataUtils.readAccount(owner.getUniqueId());
        if (playerAccount != null) {
            int balance = playerAccount.getBalance();
            int safe = playerAccount.getSafe();

            if (safe >= value) {
                balance += value;
                safe -= value;

                playerAccount.setBalance(balance);
                playerAccount.setSafe(safe);
                PlayerDataUtils.updateAccount(owner.getUniqueId(), playerAccount);
                return true;
            }
            return false; // ERROR REASON: safe doesn't contain that much
        }
        return false; // ERROR REASON: account doesn't exist
    }

    public boolean hasAmount(OfflinePlayer owner, int value) {
        PlayerAccount playerAccount = PlayerDataUtils.readAccount(owner.getUniqueId());
        if (playerAccount != null) {
            return playerAccount.getBalance() >= value;
        }
        return false; // player's account doesn't exist
    }

    public boolean hasAccount(OfflinePlayer owner) {
        return PlayerDataUtils.readAccount(owner.getUniqueId()) != null;
    }

    public boolean sendAccount(OfflinePlayer sender, OfflinePlayer reciever, int value) {
        PlayerAccount senderAccount = PlayerDataUtils.readAccount(sender.getUniqueId());
        PlayerAccount recieverAccount = PlayerDataUtils.readAccount(reciever.getUniqueId());
        if (senderAccount != null && recieverAccount != null) { // both accounts exist
            if (senderAccount.getBalance() <= value) {return false;}

            // change the account balances
            int senderAccountBalance = senderAccount.getBalance();
            int recieverAccountBalance = recieverAccount.getBalance();
            senderAccountBalance -= value;
            recieverAccountBalance += value;
            senderAccount.setBalance(senderAccountBalance);
            recieverAccount.setBalance(recieverAccountBalance);


            PlayerDataUtils.updateAccount(sender.getUniqueId(), senderAccount);
            PlayerDataUtils.updateAccount(reciever.getUniqueId(), recieverAccount);
            return true;
        }
        return false; // either account doesn't exist
    }

    /**
     * Changes the value in relation to the current balance
     * @param owner
     * @param value
     */
    public void changeBalance(OfflinePlayer owner, int value) {
        PlayerAccount playerAccount = PlayerDataUtils.readAccount(owner.getUniqueId());
        if (playerAccount != null) {
            playerAccount.setBalance(playerAccount.getBalance() + value);
            PlayerDataUtils.updateAccount(owner.getUniqueId(), playerAccount);
        }
    }

    @NotNull
    public PlayerAccount getAccount(OfflinePlayer owner) {
        PlayerAccount account = PlayerDataUtils.readAccount(owner.getUniqueId());
        if (account == null) {
            account = createAccount(owner, (int) ConfigTags.MONEY_DEFAULT_AMOUNT.get());
        }
        return account;
    }


    ///////////////////////////////////////////////////////////////////////////
    // Bank methods
    ///////////////////////////////////////////////////////////////////////////

    @Nullable
    public BankAccount getBankAccount(String bankName) {
        return BankDataUtils.readBank(bankName);
    }

    public boolean createBank(OfflinePlayer owner, String bankName) {
        ArrayList<UUID> members = new ArrayList<>(); // zero-array initializer, to prevent null errors
        if (BankDataUtils.readBank(bankName) != null) {
            return false;
        }
        BankDataUtils.createBank(owner.getUniqueId(), bankName, 0, members);
        return true;
    }

    public boolean deleteBank(String bankName) {
        BankAccount account = BankDataUtils.readBank(bankName);
        if (account != null) {
            BankDataUtils.deleteBank(account.getOwnerID());
            return true;
        }
        return false;
    }

    public int getBankBalance(String bankName) {
        BankAccount account = BankDataUtils.readBank(bankName);
        if (account != null) {
            return account.getBalance();
        }
        return 0;
    }

    public void changeBankBalance(String bankName, int value) {
        BankAccount account = BankDataUtils.readBank(bankName);
        if (account != null) {
            account.setBalance(account.getBalance() + value);
            BankDataUtils.updateBank(bankName, account);
        }
    }


    public void addMember(OfflinePlayer owner, String bankName, OfflinePlayer member) {
        BankAccount bankAccount = BankDataUtils.readBank(owner.getUniqueId(), bankName);
        if (bankAccount != null) {
            ArrayList<UUID> members = bankAccount.getMembers();
            members.add(member.getUniqueId());

            bankAccount.setMembers(members);
            BankDataUtils.updateBank(owner.getUniqueId(), bankName, bankAccount);
        }
        // the bank with those details do not exist
    }

    public void removeMember(OfflinePlayer owner, String bankName, OfflinePlayer member) {
        BankAccount bankAccount = BankDataUtils.readBank(owner.getUniqueId(), bankName);
        if (bankAccount != null) {
            ArrayList<UUID> members = bankAccount.getMembers();
            members.remove(member.getUniqueId());

            bankAccount.setMembers(members);
            BankDataUtils.updateBank(owner.getUniqueId(), bankName, bankAccount);
        }
        // the bank with those details do not exist
    }

    public boolean depositBank(OfflinePlayer owner, String bankName, OfflinePlayer depositor, int value) {
        BankAccount bankAccount = BankDataUtils.readBank(owner.getUniqueId(), bankName);
        PlayerAccount playerAccount = PlayerDataUtils.readAccount(depositor.getUniqueId());
        if (bankAccount != null && playerAccount != null) {
            int bankBalance = bankAccount.getBalance();
            int depositorBalance = playerAccount.getBalance();

            if (depositorBalance >= value) {
                depositorBalance -= value;
                bankBalance += value;

                playerAccount.setBalance(depositorBalance);
                bankAccount.setBalance(bankBalance);

                PlayerDataUtils.updateAccount(depositor.getUniqueId(), playerAccount);
                BankDataUtils.updateBank(owner.getUniqueId(), bankName, bankAccount);
                return true;
            }
            return false; // ERROR REASON: safe doesn't contain that much
        }
        return false; // the bank with those details do not exist, or the depositor doesn't exist
    }

    public boolean withdrawBank(OfflinePlayer owner, String bankName, OfflinePlayer depositor, int value) {
        BankAccount bankAccount = BankDataUtils.readBank(owner.getUniqueId(), bankName);
        PlayerAccount playerAccount = PlayerDataUtils.readAccount(depositor.getUniqueId());
        if (bankAccount != null && playerAccount != null) {
            int bankBalance = bankAccount.getBalance();
            int depositorBalance = playerAccount.getBalance();

            if (bankBalance >= value) {
                depositorBalance += value;
                bankBalance -= value;

                playerAccount.setBalance(depositorBalance);
                bankAccount.setBalance(bankBalance);

                PlayerDataUtils.updateAccount(depositor.getUniqueId(), playerAccount);
                BankDataUtils.updateBank(owner.getUniqueId(), bankName, bankAccount);
                return true;
            }
            return false; // ERROR REASON: safe doesn't contain that much
        }
        return false; // the bank with those details do not exist, or the depositor doesn't exist
    }

    // vault methods


    /**
     * vault method
     * @param bankName
     * @param member
     */
    public void addMember(String bankName, OfflinePlayer member) {
        BankAccount bankAccount = BankDataUtils.readBank(bankName);
        if (bankAccount != null) {
            ArrayList<UUID> members = bankAccount.getMembers();
            members.add(member.getUniqueId());

            bankAccount.setMembers(members);

            BankDataUtils.updateBank(bankName, bankAccount);
        }
        // the bank with those details do not exist
    }

    /**
     * vault method
     * @param bankName
     * @param member
     */
    public void removeMember(String bankName, OfflinePlayer member) {
        BankAccount bankAccount = BankDataUtils.readBank(bankName);
        if (bankAccount != null) {
            ArrayList<UUID> members = bankAccount.getMembers();
            members.remove(member.getUniqueId());

            bankAccount.setMembers(members);
            BankDataUtils.updateBank(bankName, bankAccount);
        }
        // the bank with those details do not exist
    }

    /**
     * Vault method
     * @param bankName
     * @param depositor
     * @param value
     * @return
     */
    public boolean depositBank(String bankName, OfflinePlayer depositor, int value) {
        BankAccount bankAccount = BankDataUtils.readBank(bankName);
        PlayerAccount playerAccount = PlayerDataUtils.readAccount(depositor.getUniqueId());
        if (bankAccount != null && playerAccount != null) {
            int bankBalance = bankAccount.getBalance();
            int depositorBalance = playerAccount.getBalance();

            if (depositorBalance >= value) {
                depositorBalance -= value;
                bankBalance += value;

                playerAccount.setBalance(depositorBalance);
                bankAccount.setBalance(bankBalance);

                PlayerDataUtils.updateAccount(depositor.getUniqueId(), playerAccount);
                BankDataUtils.updateBank(bankName, bankAccount);
                return true;
            }
            return false; // ERROR REASON: safe doesn't contain that much
        }
        return false; // the bank with those details do not exist, or the depositor doesn't exist
    }

    /**
     * Vault method
     * @param bankName
     * @param depositor
     * @param value
     * @return
     */
    public boolean withdrawBank(String bankName, OfflinePlayer depositor, int value) {
        BankAccount bankAccount = BankDataUtils.readBank(bankName);
        PlayerAccount playerAccount = PlayerDataUtils.readAccount(depositor.getUniqueId());
        if (bankAccount != null && playerAccount != null) {
            int bankBalance = bankAccount.getBalance();
            int depositorBalance = playerAccount.getBalance();

            if (bankBalance >= value) {
                depositorBalance += value;
                bankBalance -= value;

                playerAccount.setBalance(depositorBalance);
                bankAccount.setBalance(bankBalance);

                PlayerDataUtils.updateAccount(depositor.getUniqueId(), playerAccount);
                BankDataUtils.updateBank(bankName, bankAccount);
                return true;
            }
            return false; // ERROR REASON: safe doesn't contain that much
        }
        return false; // the bank with those details do not exist, or the depositor doesn't exist
    }


    ///////////////////////////////////////////////////////////////////////////
    // Formatting methods
    ///////////////////////////////////////////////////////////////////////////

    public static String format(int amount) {
        final String symbol = (String) ConfigTags.MONEY_SYMBOL.get();
        final String separator = (String) ConfigTags.SEPARATOR.get();
        final char[] chars = String.valueOf(amount).toCharArray();
        long initialCount;
        final String symbolPosition = (String) ConfigTags.SYMBOL_POSITION.get();


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
}

