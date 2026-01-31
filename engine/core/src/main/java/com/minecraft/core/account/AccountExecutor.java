package com.minecraft.core.account;

public abstract class AccountExecutor {
    public abstract void sendMessage(String message);

    public void sendPluginMessage(String channel, byte[] bytes) {
    }
}
