package com.minecraft.core.database.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.minecraft.core.Constants;
import com.minecraft.core.database.enums.Columns;
import com.minecraft.core.database.enums.Tables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.postgresql.util.PGobject;

public class DataStorage {

    private final Map<Columns, Data> columnsDataMap;
    private final Map<Columns, Boolean> columnsLoaded;

    protected UUID uniqueId;
    protected String username;

    public DataStorage(UUID uniqueId, String username) {
        this.uniqueId = uniqueId;
        this.username = username;

        columnsDataMap = new ConcurrentHashMap<>();
        columnsLoaded = new ConcurrentHashMap<>();

        for (Columns column : Columns.values()) {
            getColumnsLoaded().put(column, false);
        }

        for (Columns columns : Columns.values()) {
            if (columns == Columns.UNIQUE_ID) {
                columnsDataMap.put(columns, new Data(columns, this.uniqueId));
            } else if (columns.getClassExpected().equals("JsonArray")) {
                columnsDataMap.put(columns, new Data(columns, new JsonArray()));
            } else if (columns.getClassExpected().equals("JsonObject")) {
                columnsDataMap.put(columns, new Data(columns, new JsonObject()));
            } else {
                columnsDataMap.put(columns, new Data(columns, columns.getDefaultValue()));
            }
        }
    }

    /**
     * Utility method for loading tables.
     *
     * @param tables The tables that will be loaded.
     * @return if it was a success.
     */
    @Deprecated
    public boolean load(Tables... tables) {
        List<Columns> columns = new ArrayList<>();

        for (Tables table : tables) {
            columns.addAll(Arrays.asList(table.getColumns()));
        }
        return loadColumns(columns);
    }

    /**
     * Utility method to save all columns from a table.
     */
    public void saveTable(Tables... tables) {
        try {
            boolean transactional = tables.length > 1;
            if (transactional) {
                Constants.getPostgreSQL().beginTransaction();
            }
            for (Tables table : tables) {
                List<Columns> changed = Arrays.stream(table.getColumns())
                        .filter(c -> getData(c).hasChanged())
                        .collect(Collectors.toList());
                if (changed.isEmpty()) {
                    System.out.println("Nothing to save to " + table.getName() + " from " + username + "'s account.");
                    continue;
                }
                upsertRow(table, changed.toArray(new Columns[0]));
            }
            if (transactional) {
                Constants.getPostgreSQL().commitTransaction();
            }
        } catch (SQLException exception) {
            try {
                Constants.getPostgreSQL().rollbackTransaction();
            } catch (SQLException ignored) {
            }
            exception.printStackTrace();
        }
    }

    /**
     * Utility method to save one column from a table.
     */
    public void saveColumn(Columns column) {

        if (!getData(column).hasChanged())
            return;

        try {
            upsertRow(column.getTable(), column);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Utility method to save multiples columns from a table.
     */
    public void saveColumnsFromSameTable(Columns... columns) {
        try {
            List<Columns> changed = Arrays.stream(columns)
                    .filter(c -> getData(c).hasChanged())
                    .collect(Collectors.toList());
            if (changed.isEmpty()) {
                return;
            }
            upsertRow(columns[0].getTable(), changed.toArray(new Columns[0]));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    public boolean loadColumns(List<Columns> columns) {
        try {

            if (columns.isEmpty())
                return true;

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("SELECT accounts.unique_id, ");

            List<Tables> tables = removeDuplicates(columns.stream().map(Columns::getTable).filter(c -> c != Tables.ACCOUNT).collect(Collectors.toList()));

            for (int i = 0; i < columns.size(); i++) {
                Columns column = columns.get(i);
                stringBuilder.append(column.getTable().getName().toLowerCase()).append(".").append(column.getField());
                if (i != columns.size() - 1)
                    stringBuilder.append(",");
                stringBuilder.append(" ");
            }

            stringBuilder.append("FROM accounts accounts ");

            for (Tables table : tables) {
                stringBuilder.append("LEFT JOIN ").append(table.getName()).append(" ").append(table.getName()).append(" ON accounts.unique_id = ").append(table.getName()).append(".unique_id ");
            }
            stringBuilder.append("WHERE accounts.unique_id=? LIMIT 1;");

            PreparedStatement preparedStatement = Constants.getPostgreSQL().getConnection().prepareStatement(stringBuilder.toString());
            preparedStatement.setObject(1, uniqueId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                for (Columns column : columns) {
                    Object value = loadData(column, resultSet, column.getField());
                    getData(column).setData(value);
                    getColumnsLoaded().put(column, true);
                }
            }
            resultSet.close();
            preparedStatement.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean loadIfUnloaded(Columns... columns) {
        List<Columns> toLoad = new ArrayList<>();

        for (Columns column : columns) {
            if (!isLoaded(column))
                toLoad.add(column);
        }

        System.out.println("(" + username + ")" + "The method loadIfUnloaded loaded " + toLoad.size() + "/" + columns.length + ". (" + Arrays.toString(toLoad.toArray()) + ")");

        return loadColumns(toLoad);
    }

    public void loadColumns(boolean createIfNotExists, Columns... columns) {
        try {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("SELECT ");

            int inx = 0;
            int max = columns.length;

            while (inx < max) {
                Columns current = columns[inx];
                if (inx == 0)
                    stringBuilder.append(current.getField());
                else
                    stringBuilder.append(", ").append(current.getField());
                inx++;
            }

            Tables table = columns[0].getTable();

            stringBuilder.append(" FROM ").append(table.getName());
            stringBuilder.append(" WHERE unique_id=? LIMIT 1;");

            PreparedStatement preparedStatement = Constants.getPostgreSQL().getConnection().prepareStatement(stringBuilder.toString());
            preparedStatement.setObject(1, uniqueId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                for (Columns column : columns) {
                    Object value = loadData(column, resultSet, column.getField());
                    getData(column).setData(value);
                }
            } else {
                if (createIfNotExists) {
                    insertRow(table, columns);
                }
            }

            resultSet.close();
            preparedStatement.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void insertRow(Tables table, Columns... columns) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(table.getName()).append(" (unique_id");
        for (Columns c : columns) {
            sql.append(", ").append(c.getField());
        }
        sql.append(") VALUES (?");
        for (int i = 0; i < columns.length; i++) {
            sql.append(", ?");
        }
        sql.append(");");
        PreparedStatement ps = Constants.getPostgreSQL().getConnection().prepareStatement(sql.toString());
        int idx = 1;
        ps.setObject(idx++, uniqueId);
        for (Columns c : columns) {
            Object value = getDefaultForColumn(c);
            setParam(ps, idx++, c, value);
        }
        ps.execute();
        ps.close();
    }

    private void upsertRow(Tables table, Columns... changedColumns) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(table.getName()).append(" (unique_id");
        for (Columns c : changedColumns) {
            sql.append(", ").append(c.getField());
        }
        sql.append(") VALUES (?");
        for (int i = 0; i < changedColumns.length; i++) {
            sql.append(", ?");
        }
        sql.append(") ON CONFLICT (unique_id) DO UPDATE SET ");
        for (int i = 0; i < changedColumns.length; i++) {
            Columns c = changedColumns[i];
            sql.append(c.getField()).append("=EXCLUDED.").append(c.getField());
            if (i < changedColumns.length - 1) sql.append(", ");
        }
        PreparedStatement ps = Constants.getPostgreSQL().getConnection().prepareStatement(sql.toString());
        int idx = 1;
        ps.setObject(idx++, uniqueId);
        for (Columns c : changedColumns) {
            Object value = getData(c).getAsObject();
            setParam(ps, idx++, c, value);
            getData(c).setChanged(false);
        }
        ps.executeUpdate();
        ps.close();
    }

    public static void createTables() {
        try {

            PreparedStatement logs = Constants.getPostgreSQL().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS logs (id SERIAL PRIMARY KEY, unique_id UUID NOT NULL, nickname VARCHAR(16) NOT NULL, server VARCHAR(20) NOT NULL, content TEXT NOT NULL, type VARCHAR(20) NOT NULL, created_at TIMESTAMP NOT NULL DEFAULT NOW());");
            logs.execute();
            logs.close();

            for (Tables tables : Tables.values()) {
                PreparedStatement preparedStatement = Constants.getPostgreSQL().getConnection().prepareStatement(DataStorage.createTable(tables, tables.getColumns()));
                preparedStatement.execute();
                preparedStatement.close();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static String createTable(Tables tables, Columns... columns) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("CREATE TABLE IF NOT EXISTS ").append(tables.getName()).append(" (");
        stringBuilder.append("id SERIAL NOT NULL,");

        int inx = 0;
        int max = columns.length;

        stringBuilder.append("unique_id UUID UNIQUE");

        while (inx < max) {
            Columns current = columns[inx];

            stringBuilder.append(", ").append(current.getField()).append(" ").append(current.getColumnType());
            inx++;
        }

        stringBuilder.append(", PRIMARY KEY (id));");
        return stringBuilder.toString();
    }

    public static Object loadData(Columns columns, ResultSet resultSet, String fieldName) {
        try {
            Object obj = resultSet.getObject(fieldName);

            if (obj == null) {
                obj = columns.getDefaultValue();
            }

            switch (columns.getClassExpected()) {
                case "JsonArray":
                    String jsonArrayStr = (obj instanceof PGobject) ? ((PGobject) obj).getValue() : obj.toString();
                    return Constants.GSON.fromJson(jsonArrayStr, JsonArray.class);
                case "JsonObject":
                    String jsonObjStr = (obj instanceof PGobject) ? ((PGobject) obj).getValue() : obj.toString();
                    return Constants.GSON.fromJson(jsonObjStr, JsonObject.class);
                case "String":
                    return obj.toString();
                case "Int":
                    return Integer.valueOf(obj.toString());
                case "Long":
                    return Long.valueOf(obj.toString());
                case "Boolean":
                    if (obj instanceof Boolean) return (Boolean) obj;
                    return Boolean.valueOf(obj.toString());
                default:
                    throw new IllegalStateException();
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Data getData(Columns columns) {
        return getColumnsDataMap().get(columns);
    }

    public Map<Columns, Boolean> getColumnsLoaded() {
        return columnsLoaded;
    }

    public Map<Columns, Data> getColumnsDataMap() {
        return columnsDataMap;
    }

    private <T> ArrayList<T> removeDuplicates(List<T> list) {
        Set<T> set = new LinkedHashSet<>(list);
        list.clear();
        list.addAll(set);
        return new ArrayList<>(list);
    }

    public boolean isLoaded(Columns column) {
        return getColumnsLoaded().get(column);
    }

    private void setParam(PreparedStatement ps, int index, Columns column, Object value) throws SQLException {
        switch (column.getClassExpected()) {
            case "JsonArray":
            case "JsonObject":
                PGobject jsonb = new PGobject();
                jsonb.setType("jsonb");
                jsonb.setValue(value == null ? (column.getClassExpected().equals("JsonArray") ? "[]" : "{}") : value.toString());
                ps.setObject(index, jsonb);
                break;
            case "String":
                ps.setString(index, value == null ? "" : value.toString());
                break;
            case "Int":
                ps.setInt(index, value == null ? 0 : Integer.parseInt(value.toString()));
                break;
            case "Long":
                ps.setLong(index, value == null ? 0L : Long.parseLong(value.toString()));
                break;
            case "Boolean":
                boolean b = false;
                if (value != null) {
                    if (value instanceof Boolean) b = (Boolean) value;
                    else b = Boolean.parseBoolean(value.toString());
                }
                ps.setBoolean(index, b);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private Object getDefaultForColumn(Columns column) {
        Object def = column.getDefaultValue();
        if (def == null) {
            if (column.getClassExpected().equals("JsonArray")) return new JsonArray();
            if (column.getClassExpected().equals("JsonObject")) return new JsonObject();
            if (column.getClassExpected().equals("String")) return "";
            if (column.getClassExpected().equals("Int")) return 0;
            if (column.getClassExpected().equals("Long")) return 0L;
            if (column.getClassExpected().equals("Boolean")) return false;
        }
        if (column.getClassExpected().equals("JsonArray") && def instanceof String) {
            return Constants.GSON.fromJson(def.toString(), JsonArray.class);
        }
        if (column.getClassExpected().equals("JsonObject") && def instanceof String) {
            return Constants.GSON.fromJson(def.toString(), JsonObject.class);
        }
        return def;
    }
}
