package server.databases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import shared.ConnectionInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonDatabase extends Database {
    private final Path PATH = Path.of(ConnectionInfo.SERVER_PATH + "db.json");
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock writeLock = readWriteLock.writeLock();
    private final Lock readLock = readWriteLock.readLock();

    private Map<String, Object> map;

    public JsonDatabase() {
        map = loadDatabase();
        if (map == null) map = new HashMap<>();
    }

    @Override
    public Object get(String[] cellPath) {
        Map<String, Object> currentMap = map;

        for (int i = 0; i < cellPath.length - 1; i++) {
            String cell = cellPath[i];
            Object foundObj = currentMap.get(cell);
            if (foundObj == null) {
                throw new KeyNotFoundException();
            } else {
                currentMap = (Map<String, Object>) foundObj;
            }
        }

        return currentMap.get(cellPath[cellPath.length - 1]);
    }

    @Override
    public void set(String[] cellPath, Object value) {
        Map<String, Object> currentMap = map;

        for (int i = 0; i < cellPath.length - 1; i++) {
            String cell = cellPath[i];
            Object foundObj = currentMap.get(cell);
            if (foundObj == null) {
                Map<String, Object> newMap = new HashMap<>();
                currentMap.put(cell, newMap);
                currentMap = newMap;
            } else {
                currentMap = (Map<String, Object>) foundObj;
            }
        }
        currentMap.put(cellPath[cellPath.length - 1], value);

        saveDatabase();
    }

    @Override
    public void delete(String[] cellPath) {
        Map<String, Object> currentMap = map;

        for (int i = 0; i < cellPath.length - 1; i++) {
            String cell = cellPath[i];
            Object foundObj = currentMap.get(cell);
            if (foundObj == null) {
                throw new KeyNotFoundException();
            } else {
                currentMap = (Map<String, Object>) foundObj;
            }
        }
        currentMap.remove(cellPath[cellPath.length - 1]);

        saveDatabase();
    }

    private void saveDatabase() {
        writeLock.lock();

        try {
            checkFile();
            Gson gson = new Gson();

            Files.writeString(PATH, gson.toJson(map));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    private Map<String, Object> loadDatabase() {
        readLock.lock();
        Map<String, Object> result = null;

        try {
            checkFile();

            Gson gson = new Gson();
            String content = Files.readString(PATH);
            result = gson.fromJson(content, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }

        return result;
    }

    private void checkFile() throws IOException {
        if (!Files.exists(PATH)) {
            Files.createFile(PATH);
        }
    }
}
