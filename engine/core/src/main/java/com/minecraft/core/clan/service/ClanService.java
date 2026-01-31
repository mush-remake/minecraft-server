package com.minecraft.core.clan.service;

import com.minecraft.core.Constants;
import com.minecraft.core.clan.Clan;
import org.postgresql.util.PGobject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// CREATE TABLE IF NOT EXISTS `clans`
// (`index` INT UNSIGNED NOT NULL AUTO_INCREMENT, `name` VARCHAR(16) NOT NULL,
// `tag` VARCHAR(16) NOT NULL, `members` LONGTEXT NOT NULL, `slots` INT(100) NOT NULL,
// `points` INT(100) NOT NULL, `creation` BIGINT NOT NULL, PRIMARY KEY(`index`));

public class ClanService {

    private final Map<Integer, Clan> clanMap = new ConcurrentHashMap<>();

    public ClanService() {
        try {
            PreparedStatement ps = Constants.getPostgreSQL().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS clans (index SERIAL PRIMARY KEY, name VARCHAR(16) NOT NULL, tag VARCHAR(16) NOT NULL, members JSONB NOT NULL, slots INT NOT NULL, points INT NOT NULL, creation BIGINT NOT NULL, color VARCHAR(16) NOT NULL)");
            ps.execute();
            ps.close();
        } catch (SQLException ignored) {
        }
    }

    public Clan getClan(int index) {
        return clanMap.computeIfAbsent(index, this::loadClan);
    }

    public Clan fetch(int index) {

        if (!clanMap.containsKey(index))
            return null;

        return clanMap.get(index);
    }

    public Clan getClan(String name) {
        Clan clan = clanMap.values().stream().filter(c -> c.getName().equalsIgnoreCase(name) || c.getTag().equalsIgnoreCase(name)).findFirst().orElse(null);

        if (clan == null) {
            clan = loadClan(name);
        }

        clanMap.put(clan.getIndex(), clan);

        return clan;
    }

    private Clan loadClan(int index) {

        String query = "SELECT * FROM clans WHERE index = ? LIMIT 1;";

        try (PreparedStatement statement = Constants.getPostgreSQL().getConnection().prepareStatement(query)) {
            statement.setInt(1, index);
            try (ResultSet resultSet = statement.executeQuery()) {
                return (resultSet.next() ? Clan.parse(resultSet) : null);
            }
        } catch (Exception e) {
            throw new RuntimeException("A Exception occurred while loading '" + index + "' clan.", e);
        }
    }

    private Clan loadClan(String name) {

        String query = "SELECT * FROM clans WHERE name = ? OR tag = ? LIMIT 1;";

        try (PreparedStatement statement = Constants.getPostgreSQL().getConnection().prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                return Clan.parse(resultSet.next() ? resultSet : null);
            }
        } catch (Exception e) {
            throw new RuntimeException("A Exception occurred while loading '" + name + "' clan.", e);
        }
    }

    public void pushClan(Clan clan) throws SQLException {
        String query = "UPDATE clans SET tag = ?, members = ?, slots = ?, creation = ?, points = ? WHERE index = ?";

        try (PreparedStatement ps = Constants.getPostgreSQL().getConnection().prepareStatement(query)) {
            ps.setString(1, clan.getTag());
            PGobject jsonb = new PGobject();
            jsonb.setType("jsonb");
            jsonb.setValue(Constants.GSON.toJson(clan.getMembers()));
            ps.setObject(2, jsonb);
            ps.setInt(3, clan.getSlots());
            ps.setLong(4, clan.getCreation());
            ps.setInt(5, clan.getPoints());
            ps.setInt(6, clan.getIndex());
            ps.executeUpdate();
        }
    }

    public void register(Clan clan) throws SQLException {

        String query = "INSERT INTO clans(name, tag, members, slots, points, creation, color) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING index;";

        try (PreparedStatement ps = Constants.getPostgreSQL().getConnection().prepareStatement(query)) {
            ps.setString(1, clan.getName());
            ps.setString(2, clan.getTag());
            PGobject jsonb = new PGobject();
            jsonb.setType("jsonb");
            jsonb.setValue(Constants.GSON.toJson(clan.getMembers()));
            ps.setObject(3, jsonb);
            ps.setInt(4, clan.getSlots());
            ps.setInt(5, clan.getPoints());
            ps.setLong(6, clan.getCreation());
            ps.setString(7, clan.getColor());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    clan.setIndex(rs.getInt(1));
                }
            }

            clanMap.put(clan.getIndex(), clan);
        }
    }

    public boolean isClanExists(String name, String tag) {

        Clan clan = clanMap.values().stream().filter(c -> c.getName().equalsIgnoreCase(name) || c.getTag().equalsIgnoreCase(tag)).findFirst().orElse(null);

        if (clan != null)
            return true;

        String sql = "SELECT index FROM clans WHERE name = LOWER(?) OR tag = LOWER(?) LIMIT 1";

        try (PreparedStatement ps = Constants.getPostgreSQL().getConnection().prepareStatement(sql)) {

            ps.setString(1, name.toLowerCase());
            ps.setString(2, tag.toLowerCase());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Clan clan) throws SQLException {
        String sql = "DELETE FROM clans WHERE index = ?";

        try (PreparedStatement ps = Constants.getPostgreSQL().getConnection().prepareStatement(sql)) {
            ps.setInt(1, clan.getIndex());

            boolean successful = ps.executeUpdate() > 0;

            if (successful)
                clanMap.remove(clan.getIndex());

            return successful;
        }
    }

    public void forget(Clan clan) {
        clanMap.remove(clan.getIndex());
    }

    public void add(Clan clan) {
        clanMap.put(clan.getIndex(), clan);
    }

    public List<Clan> getClans() {
        return new ArrayList<>(clanMap.values());
    }
}
