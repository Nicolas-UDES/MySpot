package com.ift604.udes.myspot.Entites;

import com.orm.SugarRecord;

/**
 * Created by Squirrel on 2017-11-26.
 */

public class ServerId extends SugarRecord<ServerId> {

    private int serverId;

    public ServerId() {
    }

    public ServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
