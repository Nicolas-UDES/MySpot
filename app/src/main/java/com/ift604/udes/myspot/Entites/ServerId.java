package com.ift604.udes.myspot.Entites;

import com.orm.SugarRecord;

/**
 * Created by Squirrel on 2017-11-26.
 */

public class ServerId extends SugarRecord {

    private int server;

    public ServerId() {
    }

    public ServerId(int serverId) {
        this.server = serverId;
    }

    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public static Integer getServerId() {
        try {
            return ServerId.findAll(ServerId.class).next().getServer();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean clearServerId() {
        try {
            ServerId.findAll(ServerId.class).next().delete();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
