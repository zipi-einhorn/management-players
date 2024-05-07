package com.test.management.players.controller;

public class PlayerCSV {
    Integer id;
    String nickName;

    public PlayerCSV(long id, String nickName) {
        this.id = Math.toIntExact(id);
        this.nickName = nickName;
    }
}