package com.ift604.udes.myspot.Entites;

import com.orm.SugarRecord;

/**
 * Created by Squirrel on 2017-11-26.
 */

public class ServerId extends SugarRecord {

    private long server;

    public ServerId() {
    }

    public ServerId(long serverId) {
        this.server = serverId;
    }

    public long getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public static Long getServerId() {
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
