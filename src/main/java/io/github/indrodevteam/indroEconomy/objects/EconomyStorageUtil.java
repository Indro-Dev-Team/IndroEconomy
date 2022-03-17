package io.github.indrodevteam.indroEconomy.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.indrodevteam.indroEconomy.IndroEconomy;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EconomyStorageUtil {

    //CRUD - Create, Read, Update, Delete

    private static ArrayList<PlayerEconomyModel> economy = new ArrayList<>();

    public static PlayerEconomyModel createAccount (UUID playerUUID,
                                                        long wallet,
                                                        long bank,
                                                        long maxBank,
                                                        long playerXP,
                                                        long nextXP,
                                                        int XpLevel) {
        PlayerEconomyModel model = new PlayerEconomyModel(playerUUID, wallet, bank, maxBank, playerXP, nextXP, XpLevel);
        economy.add(model);
        return model;
    }

    public static PlayerEconomyModel findAccount(String id) {
        // linear search
        for (PlayerEconomyModel model : economy) {
            if (model.getId().equalsIgnoreCase(id)) {
                return model;
            }
        }
        return null;
    }

    public static PlayerEconomyModel findAccount(UUID uuid) {
        // linear search
        for (PlayerEconomyModel model : economy) {
            if (model.getPlayerUUID().equals(uuid)) {
                return model;
            }
        }
        return null;
    }

    public static void updateAccount(String id, PlayerEconomyModel newAccount) {
        // linear search
        for (PlayerEconomyModel model : economy) {
            if (model.getId().equalsIgnoreCase(id)) {
                model.setBank(newAccount.getBank());
                model.setPlayerUUID(newAccount.getPlayerUUID());
                model.setWallet(newAccount.getWallet());

                try {
                    saveAccounts();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    public static boolean deleteAccount(String id) {
        // linear search
        for (PlayerEconomyModel model : economy) {
            if (model.getId().equalsIgnoreCase(id)) {
                economy.remove(model);
                try {
                    saveAccounts();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static List<PlayerEconomyModel> findAllAccounts() {
        return economy;
    }

    public static void saveAccounts() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(IndroEconomy.getInstance().getDataFolder().getAbsolutePath() + File.separator + "economy.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }
        Writer writer = new FileWriter(file, false);
        gson.toJson(economy, writer);
        writer.flush();
        writer.close();
    }

    public static void loadAccounts() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(IndroEconomy.getInstance().getDataFolder().getAbsolutePath() + File.separator + "economy.json");
        if (!file.exists()) {
            saveAccounts();
        }
        PlayerEconomyModel[] model = gson.fromJson(new FileReader(file), PlayerEconomyModel[].class);
        economy = new ArrayList<>(Arrays.asList(model));
    }
}
