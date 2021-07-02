package ru.netology.data;


import java.sql.*;


public class SQLHelper {
    private static final String url = System.getProperty("db.url");
    private static final String user = "app";
    private static final String password = "pass";

    public static void cleanTables() {
        String codeSQL = "DELETE from order_entity;";
        String codeSQL2 = "DELETE from payment_entity;";
        String codeSQL3 = "DELETE from credit_request_entity;";

        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement orderEntity = connection.prepareStatement(codeSQL);
                PreparedStatement paymentEntity = connection.prepareStatement(codeSQL2);
                PreparedStatement creditEntity = connection.prepareStatement(codeSQL3);
        ) {
            orderEntity.executeUpdate();
            paymentEntity.executeUpdate();
            creditEntity.executeUpdate();
        } catch (SQLException e) {
            e.getErrorCode();
        }
    }

    public static String selectBuyStatus() {
        String stmt = "SELECT status FROM payment_entity";
        String status = "status";
        return getStatus(stmt, status);
    }

    public static String selectCreditStatus() {
        String stmt = "SELECT status FROM credit_request_entity";
        String status = "status";
        return getStatus(stmt, status);
    }

    //
    private static String getStatus(String stmt, String status) {
        String result = "";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(stmt);
        ) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            result = resultSet.getString(status);
        } catch (SQLException e) {
            e.getErrorCode();
        }
        return result;
    }
}

