package net.paiique;

import net.paiique.database.InitDbConnect;

import java.sql.Connection;

public class Main {
    public static Connection conDb;

    public static void main(String[] args) {
        conDb = new InitDbConnect().getCon();

        Thread watcherThread = new CheckThread();
        watcherThread.start();
    }
}