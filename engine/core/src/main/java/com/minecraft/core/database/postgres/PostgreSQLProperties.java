package com.minecraft.core.database.postgres;

import com.minecraft.core.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

@AllArgsConstructor
@Getter
public class PostgreSQLProperties {

    private final String host;
    private final int port;
    private final String username, password, database;

    public static PostgreSQLProperties load(File file) {
        PostgreSQLProperties properties = new PostgreSQLProperties("localhost", 5432, "server", "pass", "minecraft");
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(Constants.GSON.toJson(properties));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JSONParser jsonParser = new JSONParser();
            try {
                properties = Constants.GSON.fromJson(((JSONObject) jsonParser.parse(new FileReader(file))).toJSONString(), PostgreSQLProperties.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return properties;
    }
}
