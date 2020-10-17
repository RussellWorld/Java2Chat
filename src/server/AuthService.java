package server;

import java.sql.*;
import java.util.ArrayList;

public class AuthService {
    private static Connection connection;
    private static Statement stmt;


    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            stmt = connection.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getNickByLoginAndPass(String login, String pass) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT nickname, password FROM main WHERE login = '" + login + "'");
            int myHash = pass.hashCode();
            if (rs.next()) {
                String nick = rs.getString(1);
                int dbHash = rs.getInt(2);
                if (myHash == dbHash) {
                    return nick;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getIdbyNick(String nick) {
        try {
            String sql = String.format("SELECT id FROM main Where nickname = '%s'", nick);
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<String> getBlackList(int id) {
        ArrayList<String> blacklist = new ArrayList<>();
        try {
            String getBlockedId = String.format("SELECT nickname FROM BlackList JOIN main ON id = BlackList.id_block_user WHERE BlackList.id_user = '%s'", id);
            ResultSet rs = stmt.executeQuery(getBlockedId);

            while (rs.next()) {
                String blockedNick = rs.getString(1);
                blacklist.add(blockedNick);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blacklist;
    }

    public static void addToBlockedList(String nick, String blockedNick) {
        try {
            int id = getIdbyNick(nick);
            int id_blocked = getIdbyNick(blockedNick);
            String query = "INSERT INTO BlackList (id_user, id_block_user) VALUES (?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.setInt(2, id_blocked);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeFromBlockedList(String nick, String blockedNick) {
        try {
            int id = getIdbyNick(nick);
            int id_blocked = getIdbyNick(blockedNick);
            String query = "DELETE FROM BlackList where id_user = "+ id +" and id_block_user = "+id_blocked;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}