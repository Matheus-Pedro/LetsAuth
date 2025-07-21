package org.caprioli.letsAuth;

import java.sql.*;
import java.security.MessageDigest;

public class DatabaseManager {
    private Connection conn;
    private final String url, user, password;


    public DatabaseManager(String url, String user, String password) throws SQLException {
        this.url = url;
        this.user = user;
        this.password = password;
        reconnect();
        createTable();
    }

    public boolean isRegistered(String username) throws SQLException {
        if (!isConnectionValid()) reconnect();

        var ps = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
        ps.setString(1, username);
        var rs = ps.executeQuery();
        return rs.next();
    }

    public void register(String username, String password) throws Exception {
        if (!isConnectionValid()) reconnect();

        var ps = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
        ps.setString(1, username);
        ps.setString(2, hash(password));
        ps.executeUpdate();
    }

    public boolean checkPassword(String username, String password) {
        try {
            if (!isConnectionValid()) reconnect();

            var ps = conn.prepareStatement("SELECT password FROM users WHERE username = ?");
            ps.setString(1, username);
            var rs = ps.executeQuery();
            if (rs.next()) {
                String stored = rs.getString("password");
                return stored.equals(hash(password));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String hash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashed = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashed) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private boolean isConnectionValid() {
        try {
            return conn != null && !conn.isClosed() && conn.isValid(2);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void reconnect() throws SQLException {
        if (conn != null && !conn.isClosed()) conn.close();
        conn = DriverManager.getConnection(url, user, password);
    }

    private void createTable() throws SQLException {
        conn.createStatement().executeUpdate("""
        CREATE TABLE IF NOT EXISTS users (
            username VARCHAR(16) PRIMARY KEY,
            password VARCHAR(128)
        );
    """);
    }

}
