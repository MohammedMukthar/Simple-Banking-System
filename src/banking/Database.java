package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class Database {
    private String url;
    public Database(String url) {
        this.url = url;
        try (Connection conn = DriverManager.getConnection(url)) {
            try (Statement st = conn.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                        + "     id INTEGER PRIMARY KEY,\n" // autimaticly create new id
                        + "     number TEXT,\n"                       // don't write VARCHAR its mistake!
                        + "     pin TEXT,\n"
                        + "     balance INTEGER DEFAULT 0"
                        + ");";
                st.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void storeDataInDatabase(String accountNumber, String pin) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(this.url);
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                // Statement execution
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER PRIMARY KEY," +
                        "number TEXT NOT NULL," +
                        "pin TEXT NOT NULL," +
                        "balance INTEGER)");
                statement.executeUpdate("INSERT INTO card (number, pin, balance) VALUES ('"+accountNumber+"','"+ pin +"', 0)");
               // statement.executeUpdate("DROP THE card");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean isExist(String accountNumber, String pin) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(this.url);
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                String stmt = "SELECT pin FROM card WHERE number = " + accountNumber + ";";
                ResultSet rs = statement.executeQuery(stmt);
                while (rs.next()) {
                    if (rs.getString("pin").equals(pin)) {
                        return true;
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public String getData(String accountNumber) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(this.url);
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                String smt = "SELECT * FROM card WHERE number = " + accountNumber;
                ResultSet rs = statement.executeQuery(smt);
                String result = "";
                while (rs.next()) {
                    result += rs.getInt("id") + " " + rs.getString("number") + " " + rs.getString("pin") + " " + rs.getInt("balance");
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void addIncome(String accountNumber, int amoun) {
        int amount = Integer.valueOf(amoun);
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(this.url);
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            System.out.println(amount);
            String sql = "UPDATE card SET balance = balance + "+ amount +" WHERE number = " + accountNumber;
            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                System.out.println("executiong command");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                con.rollback();
            }
            System.out.println("amount has been added");
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transfer(String fromAccount, String toAccount, int amount) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(this.url);
        try(Connection con = dataSource.getConnection()) {
            Savepoint savepoint = con.setSavepoint();
            con.setAutoCommit(false);
            String sql = "UPDATE card SET balance = balance - " + amount + " WHERE number = " + fromAccount;
            String sql2 = "UPDATE card SET balance = balance + " + amount + " WHERE number = " + toAccount;
            try (PreparedStatement preparedStatement = con.prepareStatement(sql)){
                preparedStatement.executeUpdate();
            }catch (SQLException e) {
                con.rollback(savepoint);
                e.printStackTrace();
            }
            try (PreparedStatement preparedStatement = con.prepareStatement(sql2)) {
                preparedStatement.executeUpdate();
            }catch (SQLException e) {
                con.rollback(savepoint);
                e.printStackTrace();
            }
            con.commit();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String accountNumber) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(this.url);
        try(Connection con = dataSource.getConnection()) {
            Savepoint savepoint = con.setSavepoint();
            con.setAutoCommit(false);
            String sql = "DELETE FROM card WHERE number = " + accountNumber;
            try(PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                con.rollback(savepoint);
                e.printStackTrace();
            }
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
