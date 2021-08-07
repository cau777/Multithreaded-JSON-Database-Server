package server.databases;

public abstract class Database {
    public abstract Object get(String[] cellPath);
    public abstract void set(String[] cellPath, Object value);
    public abstract void delete(String[] cellPath);

    public static class KeyNotFoundException extends RuntimeException {}
}
