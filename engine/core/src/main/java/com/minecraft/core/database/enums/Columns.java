/*
 * Copyright (C) YoloMC, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.minecraft.core.database.enums;

import java.util.Arrays;

public enum Columns {

    UNIQUE_ID(null, "String", "unique_id", "VARCHAR(36)"),
    USERNAME("...", "String", "username", "VARCHAR(16)"),
    RANKS("[]", "JsonArray", "ranks", "TEXT"),
    PERMISSIONS("[]", "JsonArray", "permissions", "TEXT"),
    TAGS("[]", "JsonArray", "tags", "TEXT"),
    CLANTAGS("[]", "JsonArray", "clantags", "TEXT"),
    MEDALS("[]", "JsonArray", "medals", "TEXT"),
    PUNISHMENTS("[]", "JsonArray", "punishments", "TEXT"),
    CLAN(-1, "Int", "clan_id", "INT"),
    FLAGS(0, "Int", "flags", "INT"),
    PREMIUM(false, "Boolean", "premium", "VARCHAR(10)"),
    BANNED(false, "Boolean", "banned", "VARCHAR(10)"),
    MUTED(false, "Boolean", "muted", "VARCHAR(10)"),
    FIRST_LOGIN(0L, "Long", "firstLogin", "BIGINT"),
    LAST_LOGIN(0L, "Long", "lastLogin", "BIGINT"),
    ADDRESS("...", "String", "address", "VARCHAR(50)"),

    PASSWORD("...", "String", "password", "VARCHAR(24)"),
    PASSWORD_LAST_UPDATE(0L, "Long", "lastUpdate", "BIGINT"),
    SESSION_EXPIRES_AT(0L, "Long", "session_expiresAt", "BIGINT"),
    SESSION_ADDRESS("...", "String", "session_address", "VARCHAR(50)"),
    REGISTERED_AT(0L, "Long", "registeredAt", "BIGINT"),

    NICK("...", "String", "nick", "VARCHAR(16)"),
    TAG("EalNl", "String", "tag", "VARCHAR(6)"),
    CLANTAG("yQFBm", "String", "clantag", "VARCHAR(6)"),
    PREFIXTYPE("dMjgl", "String", "prefixtype", "VARCHAR(6)"),
    MEDAL("TaAEd", "String", "medal", "VARCHAR(6)"),
    LANGUAGE("VIxPa", "String", "language", "VARCHAR(6)"),
    PREFERENCES(0, "Int", "preferences", "INT"),
    SKIN("{}", "JsonObject", "skin", "TEXT"),
    BLOCKEDS("[]", "JsonArray", "blocks", "TEXT"),

    HG_KILLS(0, "Int", "hg_kills", "INT"),
    HG_DEATHS(0, "Int", "hg_deaths", "INT"),
    HG_WINS(0, "Int", "hg_wins", "INT"),
    HG_GAMES(0, "Int", "hg_played_games", "INT"),
    HG_MAX_GAME_KILLS(0, "Int", "hg_max_game_kills", "INT"),

    SCRIM_KILLS(0, "Int", "scrim_kills", "INT"),
    SCRIM_DEATHS(0, "Int", "scrim_deaths", "INT"),
    SCRIM_WINS(0, "Int", "scrim_wins", "INT"),
    SCRIM_GAMES(0, "Int", "scrim_played_games", "INT"),
    SCRIM_MAX_GAME_KILLS(0, "Int", "scrim_max_game_kills", "INT"),

    HG_RANK(1, "Int", "hg_rank", "INT"),
    HG_RANK_EXP(0, "Int", "hg_rank_exp", "INT"),

    SCRIM_RANK(1, "Int", "scrim_rank", "INT"),
    SCRIM_RANK_EXP(0, "Int", "scrim_rank_exp", "INT"),

    HG_COINS(0, "Int", "coins", "INT"),
    HG_KITS("[]", "JsonArray", "kits", "TEXT"),
    HG_DAILY_KITS("{}", "JsonObject", "dailyKit", "TEXT"),

    PVP_ARENA_KILLS(0, "Int", "arena_kills", "INT"),
    PVP_ARENA_DEATHS(0, "Int", "arena_deaths", "INT"),
    PVP_ARENA_KILLSTREAK(0, "Int", "arena_killstreak", "INT"),
    PVP_ARENA_MAX_KILLSTREAK(0, "Int", "arena_max_killstreak", "INT"),

    PVP_FPS_KILLS(0, "Int", "fps_kills", "INT"),
    PVP_FPS_DEATHS(0, "Int", "fps_deaths", "INT"),
    PVP_FPS_KILLSTREAK(0, "Int", "fps_killstreak", "INT"),
    PVP_FPS_MAX_KILLSTREAK(0, "Int", "fps_max_killstreak", "INT"),

    PVP_DAMAGE_SETTINGS("[]", "JsonArray", "damage_settings", "TEXT"),

    PVP_DAMAGE_EASY(0, "Int", "damage_easy", "INT"),
    PVP_DAMAGE_MEDIUM(0, "Int", "damage_medium", "INT"),
    PVP_DAMAGE_HARD(0, "Int", "damage_hard", "INT"),
    PVP_DAMAGE_EXTREME(0, "Int", "damage_extreme", "INT"),

    PVP_RANK(1, "Int", "rank", "INT"),
    PVP_RANK_EXP(0, "Int", "rank_exp", "INT"),

    PVP_COINS(0, "Int", "coins", "INT"),
    PVP_KITS("[]", "JsonArray", "kits", "TEXT"),

    DUELS_SOUP_WINS(0, "Int", "soup_wins", "INT"),
    DUELS_SOUP_LOSSES(0, "Int", "soup_losses", "INT"),
    DUELS_SOUP_WINSTREAK(0, "Int", "soup_winstreak", "INT"),
    DUELS_SOUP_MAX_WINSTREAK(0, "Int", "soup_max_winstreak", "INT"),
    DUELS_SOUP_GAMES(0, "Int", "soup_games", "INT"),
    DUELS_SOUP_RATING(1500, "Int", "soup_rating", "INT"),
    DUELS_SOUP_INVENTORY("...", "String", "soup_inventory", "VARCHAR(5000)"),

    DUELS_SIMULATOR_WINS(0, "Int", "simulator_wins", "INT"),
    DUELS_SIMULATOR_LOSSES(0, "Int", "simulator_losses", "INT"),
    DUELS_SIMULATOR_WINSTREAK(0, "Int", "simulator_winstreak", "INT"),
    DUELS_SIMULATOR_MAX_WINSTREAK(0, "Int", "simulator_max_winstreak", "INT"),
    DUELS_SIMULATOR_GAMES(0, "Int", "simulator_games", "INT"),
    DUELS_SIMULATOR_RATING(1500, "Int", "simulator_rating", "INT"),
    DUELS_SIMULATOR_INVENTORY("...", "String", "simulator_inventory", "VARCHAR(5000)"),

    DUELS_UHC_WINS(0, "Int", "uhc_wins", "INT"),
    DUELS_UHC_LOSSES(0, "Int", "uhc_losses", "INT"),
    DUELS_UHC_WINSTREAK(0, "Int", "uhc_winstreak", "INT"),
    DUELS_UHC_MAX_WINSTREAK(0, "Int", "uhc_max_winstreak", "INT"),
    DUELS_UHC_GAMES(0, "Int", "uhc_games", "INT"),
    DUELS_UHC_RATING(1500, "Int", "uhc_rating", "INT"),
    DUELS_UHC_INVENTORY("...", "String", "uhc_inventory", "VARCHAR(5000)"),

    DUELS_SUMO_WINS(0, "Int", "sumo_wins", "INT"),
    DUELS_SUMO_LOSSES(0, "Int", "sumo_losses", "INT"),
    DUELS_SUMO_WINSTREAK(0, "Int", "sumo_winstreak", "INT"),
    DUELS_SUMO_MAX_WINSTREAK(0, "Int", "sumo_max_winstreak", "INT"),
    DUELS_SUMO_GAMES(0, "Int", "sumo_games", "INT"),
    DUELS_SUMO_RATING(1500, "Int", "sumo_rating", "INT"),
    DUELS_SUMO_INVENTORY("...", "String", "sumo_inventory", "VARCHAR(5000)"),

    DUELS_SCRIM_WINS(0, "Int", "scrim_wins", "INT"),
    DUELS_SCRIM_LOSSES(0, "Int", "scrim_losses", "INT"),
    DUELS_SCRIM_WINSTREAK(0, "Int", "scrim_winstreak", "INT"),
    DUELS_SCRIM_MAX_WINSTREAK(0, "Int", "scrim_max_winstreak", "INT"),
    DUELS_SCRIM_GAMES(0, "Int", "scrim_games", "INT"),
    DUELS_SCRIM_RATING(1500, "Int", "scrim_rating", "INT"),
    DUELS_SCRIM_INVENTORY("...", "String", "scrim_inventory", "VARCHAR(5000)"),

    DUELS_GLADIATOR_WINS(0, "Int", "gladiator_wins", "INT"),
    DUELS_GLADIATOR_LOSSES(0, "Int", "gladiator_losses", "INT"),
    DUELS_GLADIATOR_WINSTREAK(0, "Int", "gladiator_winstreak", "INT"),
    DUELS_GLADIATOR_MAX_WINSTREAK(0, "Int", "gladiator_max_winstreak", "INT"),
    DUELS_GLADIATOR_GAMES(0, "Int", "gladiator_games", "INT"),
    DUELS_GLADIATOR_RATING(1500, "Int", "gladiator_rating", "INT"),
    DUELS_GLADIATOR_INVENTORY("...", "String", "gladiator_inventory", "VARCHAR(5000)"),
    DUELS_GLADIATOR_OLD_RATING(1500, "Int", "old_soup_rating", "INT"),
    DUELS_GLADIATOR_OLD_INVENTORY("...", "String", "old_gladiator_inventory", "VARCHAR(5000)"),

    DUELS_BOXING_WINS(0, "Int", "boxing_wins", "INT"),
    DUELS_BOXING_LOSSES(0, "Int", "boxing_losses", "INT"),
    DUELS_BOXING_WINSTREAK(0, "Int", "boxing_winstreak", "INT"),
    DUELS_BOXING_MAX_WINSTREAK(0, "Int", "boxing_max_winstreak", "INT"),
    DUELS_BOXING_GAMES(0, "Int", "boxing_games", "INT"),
    DUELS_BOXING_RATING(1500, "Int", "boxing_rating", "INT"),
    DUELS_BOXING_INVENTORY("...", "String", "boxing_inventory", "VARCHAR(5000)"),

    BRIDGE_SOLO_WINS(0, "Int", "solo_wins", "INT"),
    BRIDGE_SOLO_LOSSES(0, "Int", "solo_losses", "INT"),
    BRIDGE_SOLO_KILLS(0, "Int", "solo_kills", "INT"),
    BRIDGE_SOLO_DEATHS(0, "Int", "solo_deaths", "INT"),
    BRIDGE_SOLO_POINTS(0, "Int", "solo_points", "INT"),
    BRIDGE_SOLO_ROUNDS(0, "Int", "solo_rounds", "INT"),
    BRIDGE_SOLO_WINSTREAK(0, "Int", "solo_winstreak", "INT"),
    BRIDGE_SOLO_MAX_WINSTREAK(0, "Int", "solo_max_winstreak", "INT"),

    BRIDGE_DOUBLES_WINS(0, "Int", "doubles_wins", "INT"),
    BRIDGE_DOUBLES_LOSSES(0, "Int", "doubles_losses", "INT"),
    BRIDGE_DOUBLES_KILLS(0, "Int", "doubles_kills", "INT"),
    BRIDGE_DOUBLES_DEATHS(0, "Int", "doubles_deaths", "INT"),
    BRIDGE_DOUBLES_POINTS(0, "Int", "doubles_points", "INT"),
    BRIDGE_DOUBLES_ROUNDS(0, "Int", "doubles_rounds", "INT"),
    BRIDGE_DOUBLES_WINSTREAK(0, "Int", "doubles_winstreak", "INT"),
    BRIDGE_DOUBLES_MAX_WINSTREAK(0, "Int", "doubles_max_winstreak", "INT"),

    BRIDGE_COINS(0, "Int", "coins", "INT"),
    BRIDGE_RANK(1, "Int", "rank", "INT"),
    BRIDGE_RANK_EXP(0, "Int", "rank_exp", "INT"),
    BRIDGE_DATA("[]", "JsonArray", "data", "TEXT"),

    STAFF_WEEKLY_BANS(0, "Int", "weekly_bans", "INT"),
    STAFF_WEEKLY_MUTES(0, "Int", "weekly_mutes", "INT"),
    STAFF_WEEKLY_EVENTS(0, "Int", "weekly_events", "INT"),

    STAFF_MONTHLY_BANS(0, "Int", "monthly_bans", "INT"),
    STAFF_MONTHLY_MUTES(0, "Int", "monthly_mutes", "INT"),
    STAFF_MONTHLY_EVENTS(0, "Int", "monthly_events", "INT"),

    STAFF_LIFETIME_BANS(0, "Int", "lifetime_bans", "INT"),
    STAFF_LIFETIME_MUTES(0, "Int", "lifetime_mutes", "INT"),
    STAFF_LIFETIME_EVENTS(0, "Int", "lifetime_events", "INT");

    private final Object defaultValue;
    private final String classExpected, field, columnType;

    Columns(Object defaultValue, String classExpected, String field, String columnType) {
        this.defaultValue = defaultValue;
        this.classExpected = classExpected;
        this.field = field;
        this.columnType = columnType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public String getClassExpected() {
        return classExpected;
    }

    public String getField() {
        return field;
    }

    public String getColumnType() {
        return columnType;
    }

    public Tables getTable() {
        return Arrays.stream(Tables.values()).filter(table -> Arrays.asList(table.getColumns()).contains(this)).findFirst().orElse(null);
    }
}
