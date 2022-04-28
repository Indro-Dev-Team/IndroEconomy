package io.github.indrodevteam.indroEconomy.datamanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.indrodevteam.indroEconomy.IndroEconomy;
import io.github.indrodevteam.indroEconomy.data.BankAccount;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class BankDataUtils {
    private static ArrayList<BankAccount> bankList = new ArrayList<>();

    public static BankAccount createBank(UUID ownerID,
                                         String bankName,
                                         int balance,
                                         ArrayList<UUID> members) {
        BankAccount bankObject = new BankAccount(ownerID, bankName, balance, members);
        bankList.add(bankObject);

        try {
            saveBanks();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bankObject;
    }

    public static BankAccount readBank(UUID ownerID, String bankName) {
        for (BankAccount bank: bankList) {
            if (bank.getOwnerID().equals(ownerID) && bank.getBankName().equals(bankName)) {
                return bank;
            }
        }
        return null;
    }

    public static BankAccount readBank(String bankName) {
        for (BankAccount bank: bankList) {
            if (bank.getBankName().equals(bankName)) {
                return bank;
            }
        }
        return null;
    }

    public static void updateBank(UUID ownerID, String bankName,  BankAccount newBank) {
        for (BankAccount bank: bankList) {
            if (bank.getOwnerID().equals(ownerID) && bank.getBankName().equals(bankName)) {
                bank.setOwnerID(newBank.getOwnerID());
                bank.setBalance(newBank.getBalance());
                bank.setMembers(newBank.getMembers());
                bank.setBankName(newBank.getBankName());

                try {
                    saveBanks();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return;
            }
        }
    }

    public static void updateBank(String bankName, BankAccount newBank) {
        for (BankAccount bank: bankList) {
            if (bank.getBankName().equals(bankName)) {
                bank.setOwnerID(newBank.getOwnerID());
                bank.setBalance(newBank.getBalance());
                bank.setMembers(newBank.getMembers());
                bank.setBankName(newBank.getBankName());

                try {
                    saveBanks();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return;
            }
        }
    }

    public static void deleteBank(UUID ownerID) {
        bankList.removeIf(bank -> bank.getOwnerID().equals(ownerID));

        try {
            saveBanks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadBanks() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(IndroEconomy.getInstance().getDataFolder().getAbsolutePath() + File.separator + "banks.json");
        if (!file.exists()) {
            saveBanks();
        }
        BankAccount[] model = gson.fromJson(new FileReader(file), BankAccount[].class);
        bankList = new ArrayList<>(Arrays.asList(model));
    }

    public static void saveBanks() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(IndroEconomy.getInstance().getDataFolder().getAbsolutePath() + File.separator + "banks.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }
        Writer writer = new FileWriter(file, false);
        gson.toJson(bankList, writer);
        writer.flush();
        writer.close();
    }

    public static ArrayList<BankAccount> readAllBanks() {
        return bankList;
    }
}
