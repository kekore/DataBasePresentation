package DataBase;

import java.sql.DriverManager;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class DBConnection {

    private final static String DBURL = "jdbc:mariadb://s70.hekko.net.pl:3306/qtom97_bd2zones";
    private final static String DBUSER = "qtom97_bd2client";
    private final static String DBPASS = "PxC#210a";
    private final static String DBDRIVER = "org.mariadb.jdbc.Driver";

    private Connection connection;
    private java.sql.Statement statement;
    private int login = 0; // numer telefonu po ktorym identyfukujemy uzytkownika

    // utworzenie polaczenia administratora
    public DBConnection(String admin) {
        try {
            Class.forName(DBDRIVER).newInstance();
            connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
            if (admin != "123") {
                System.out.println("Bledne dane admina");
                closeConnection();
            } else {
                System.out.println("ZALOGOWANO ADMINA");
            }

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // utworzenie po��czenia przy poprawnym logowaniu
    public DBConnection(int phoneNumber, String password) {
        try {
            Class.forName(DBDRIVER).newInstance();
            connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
            if (!verify(phoneNumber, password)) {
                System.out.println("BLEDNE DANE");
                closeConnection();
            } else {
                login = phoneNumber;
                System.out.println("ZALOGOWANY: " + login);
            }

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // zamkniecie polaczenia wykonywanie przy wylogowaniu
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Connection doesn't exist");
        }
    }

    // sprawdzenie poprawnosci loginu i hasla
    public boolean verify(int telNumber, String password) {
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT TelNumber, Password From USER");
            statement.close();
            while (result.next()) {
                int TelNumber = result.getInt("TelNumber");
                if (TelNumber != telNumber)
                    continue;
                String Password = result.getString("Password");
                return Password.equals(password);
            }

        } catch (SQLException e) {
            System.out.println("Nie jestes zalogowany");
        }

        return false;
    }

    // generowanie raportow, wybor strefy, rodzaju pojazdu, zakresu czasu
    public ResultSet raport(String zone[], String vehicle[], Timestamp start, Timestamp end) {
        String query = "select ZONE.Name, PRESENCE.Start_date, PRESENCE.End_date, VEHICLE.Type, PRESENCE.Registration_number from PRESENCE\r\n"
                + "INNER JOIN ZONE ON PRESENCE.Zone_ID = ZONE.id\r\n"
                + "INNER JOIN VEHICLE ON PRESENCE.Registration_number = VEHICLE.Registration_number\r\n"
                + "INNER JOIN USER ON VEHICLE.User_id = USER.id\r\n" + "WHERE Start_date >= \"" + start + "\"\r\n"
                + "&& End_date <= \"" + end + "\" && USER.TelNumber = \"" + login + "\" &&";
        query += "(";
        for (int i = 0; i < zone.length; i++) {
            if (i == zone.length - 1)
                query += "ZONE.Name = \"" + zone[i] + "\") &&";
            else
                query += "ZONE.Name = \"" + zone[i] + "\" ||";
        }
        query += "(";
        for (int i = 0; i < vehicle.length; i++) {
            if (i == vehicle.length - 1)
                query += "PRESENCE.Registration_number = \"" + vehicle[i] + "\")";
            else
                query += "PRESENCE.Registration_number = \"" + vehicle[i] + "\" ||";
        }
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            statement.close();
            return result;
        } catch (SQLException e) {
            System.out.println("Nie jestes zalogowany");
        }
        return null;

    }

    // zwraca liste nazw mozliwych typow pojazdu
    public ResultSet vehicleList() {
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT Type from TARIFF_VEHICLE");
            statement.close();
            return result;
        } catch (SQLException e) {
            System.out.println("Nie jestes zalogowany");
        }
        return null;
    }

    // zwraca liste nazw mozliwych stref
    public ResultSet zoneList() {
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT Name from ZONE");
            statement.close();
            return result;
        } catch (SQLException e) {
            System.out.println("Nie jestes zalogowany");
        }
        return null;
    }

    // zwraca ID zalogowanego uzytkownika
    public int getUserId() {
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT id from USER where TelNumber = " + login);
            statement.close();
            while (result.next()) {
                int id = result.getInt("USER.id");
                return id;
            }
        } catch (SQLException e) {
            System.out.println("Nie jestes zalogowany");
        }
        return 0;
    }

    // dodaje pojazd zalogowanego uzytkownika do VEHICLE o podanym numerze
    // rejestracyjnym i typie pojazdu
    public void addVehicle(String regNumber, String type) {
        try {
            statement = connection.createStatement();
            statement.executeQuery(
                    "INSERT into VEHICLE VALUES (\"" + regNumber + "\", \"" + type + "\"," + getUserId() + ") ");
            statement.close();

        } catch (SQLException e) {
            System.out.println("Taki pojazd ju� dodano");
        }

    }

}
