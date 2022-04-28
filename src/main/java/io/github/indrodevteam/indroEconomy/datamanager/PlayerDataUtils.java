package io.github.indrodevteam.indroEconomy.datamanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.indrodevteam.indroEconomy.IndroEconomy;
import io.github.indrodevteam.indroEconomy.data.PlayerAccount;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class PlayerDataUtils {
    private static ArrayList<PlayerAccount> playerAccounts = new ArrayList<>();

    public static PlayerAccount createAccount(UUID ownerID,
                                         int balance,
                                         int safe,
                                         Map<String, Object> accountConfig) {
        PlayerAccount playerObject = new PlayerAccount(ownerID, balance, safe, accountConfig);
        playerAccounts.add(playerObject);

        try {
            saveAccounts();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return playerObject;
    }

    public static PlayerAccount readAccount(UUID ownerID) {
        for (PlayerAccount player: playerAccounts) {
            if (player.getOwnerID().equals(ownerID)) {
                return player;
            }
        }
        return null;
    }

    public static void updateAccount(UUID ownerID, PlayerAccount newPlayer) {
        for (PlayerAccount player: playerAccounts) {
            if (player.getOwnerID().equals(ownerID)) {
                player.setOwnerID(newPlayer.getOwnerID());
                player.setBalance(newPlayer.getBalance());
                player.setSafe(newPlayer.getSafe());
                player.setAccountConfig(newPlayer.getAccountConfig());

                try {
                    saveAccounts();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return;
            }
        }
    }

    public static void deleteAccount(UUID ownerID) {
        playerAccounts.removeIf(player -> player.getOwnerID().equals(ownerID));

        try {
            saveAccounts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<PlayerAccount> readAllAccounts() {
        return playerAccounts;
    }

    public static void loadAccounts() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(IndroEconomy.getInstance().getDataFolder().getAbsolutePath() + File.separator + "players.json");
        if (!file.exists()) {
            saveAccounts();
        }
        PlayerAccount[] model = gson.fromJson(new FileReader(file), PlayerAccount[].class);
        playerAccounts = new ArrayList<>(Arrays.asList(model));
    }

    public static void saveAccounts() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(IndroEconomy.getInstance().getDataFolder().getAbsolutePath() + File.separator + "players.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }
        Writer writer = new FileWriter(file, false);
        gson.toJson(playerAccounts, writer);
        writer.flush();
        writer.close();
    }
}
