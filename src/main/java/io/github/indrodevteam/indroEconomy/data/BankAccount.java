package io.github.indrodevteam.indroEconomy.data;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
public class BankAccount {
    UUID ownerID;
    String bankName;
    int balance;
    ArrayList<UUID> members;

    public BankAccount(UUID ownerID, String bankName, int balance, ArrayList<UUID> members) {
        this.ownerID = ownerID;
        this.bankName = bankName;
        this.balance = balance;
        this.members = members;
    }
}
