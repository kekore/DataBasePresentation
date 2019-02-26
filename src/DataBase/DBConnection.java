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
    public long login = 0; // numer telefonu po ktorym identyfukujemy uzytkownika

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
    public DBConnection(long phoneNumber, String password) {
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
    public boolean verify(long telNumber, String password) {
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT TelNumber, Password From USER");
            statement.close();
            while (result.next()) {
                long TelNumber = result.getLong("TelNumber");
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
        String query = "select ZONE.Name, PRESENCE.Start_date, PRESENCE.End_date, VEHICLE.Type, PRESENCE.Registration_number, PRESENCE.id from PRESENCE\r\n"
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

        System.out.println(query);
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            statement.close();
            return result;
        } catch (SQLException e) {
            System.out.println("Nie jestes zalogowany");
            System.out.println(e.getSQLState());
        }
        return null;

    }

    // zwraca liste nazw mozliwych typow pojazdu
    public ResultSet vehicleList() {
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT Type, Factor from TARIFF_VEHICLE");
            statement.close();
            return result;
        } catch (SQLException e) {
            System.out.println("Nie jestes zalogowany");
        }
        return null;
    }

    // zwraca liste rejestracji uzytkownika
    public ResultSet RegistrationList() {
        try {
            statement = connection.createStatement();
            ResultSet result = statement
                    .executeQuery("SELECT Registration_number from VEHICLE where User_id =  " + getUserId());
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
            ResultSet result = statement.executeQuery("SELECT Name, Price from ZONE");
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
            ResultSet result = statement
                    .executeQuery("SELECT id from USER where TelNumber = " + Long.toString(login));
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
    public int addVehicle(String regNumber, String type) {
        try {
            statement = connection.createStatement();
            statement.executeQuery(
                    "INSERT into VEHICLE VALUES (\"" + regNumber + "\", \"" + type + "\"," + getUserId() + ") ");
            statement.close();

        } catch (SQLException e) {
            System.out.println("Taki pojazd ju� dodano");
            return 1;
        }
        return 0;
    }

    public void addPayment(float amount, int id) {
        try {
            statement = connection.createStatement();
            statement.executeQuery(
                    "UPDATE PAYMENT SET Amount =" + amount + "where Presence_ID =" + id);
            statement.close();

        } catch (SQLException e) {
            System.out.println("Taki pojazd ju� dodano");
        }

    }

    // rejestracja nowego uzytkownika
    public void addUser(String[] rejestracja) {
        try {
            int userId = getMaxId("id", "USER") + 1;
            int cardId = getMaxId("id", "PAYMENT_CARD") + 1;
            int freeUserId = getMaxId("id", "FREE_ACCESS_USER") + 1;
            int zone = getZoneId("Ochota");
            statement = connection.createStatement();

            statement.executeQuery("INSERT into USER VALUES (" + userId + ",\"" + rejestracja[0] + "\", \"" + rejestracja[1]
                    + "\", \"" + rejestracja[2] + "\", \"" + rejestracja[9] + "\", 'aktywny');");
            statement.executeQuery("INSERT into VEHICLE VALUES (\"" + rejestracja[3] + "\", \"" + rejestracja[4] + "\"," + userId
                    + ");");
            statement.executeQuery("INSERT into PAYMENT_CARD VALUES (" + cardId + ",\"" + rejestracja[6] + "\", \""
                    + rejestracja[7] + "\", \"" + rejestracja[8] + "\"," + userId + ");");
            statement.executeQuery("INSERT into FREE_ACCESS_USER VALUES(" + freeUserId + ", 'mieszkaniec', " + userId + "," + zone
                    + ");");
            statement.close();
        } catch (SQLException e) {
            System.out.println("Pojazd jest juz w bazie");
            System.out.println(e.getSQLState());
        }

    }



    public int getMaxId(String column, String table) {
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select MAX(" + column + ") from " + table);
            statement.close();
            while (result.next()) {
                int maxId = result.getInt("MAX(id)");
                return maxId;
            }
        } catch (SQLException e) {
            System.out.println("Nie jestes zalogowany");
        }
        return 0;
    }

    public int getZoneId(String zoneName) {
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select id from ZONE where Name = \"" + zoneName + "\"");
            statement.close();
            while (result.next()) {
                int zoneId = result.getInt("ZONE.id");
                return zoneId;
            }
        } catch (SQLException e) {
            System.out.println("Nie jestes zalogowany");
        }
        return 0;
    }

    public float getZonePrice(String zoneName) {
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select Price from ZONE where Name = \"" + zoneName + "\"");
            statement.close();
            while (result.next()) {
                float zonePrice = result.getFloat("ZONE.Price");
                return zonePrice;
            }
        } catch (SQLException e) {
            System.out.println("Nie jestes zalogowany");
        }
        return 0;
    }

    public float getVehicleFactor(String vehicleType) {
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select Factor from TARIFF_VEHICLE where Type = \'" + vehicleType + "\'");
            statement.close();
            while (result.next()) {
                float vehicleFactory = result.getFloat("TARIFF_VEHICLE.Factor");
                return vehicleFactory;
            }
        } catch (SQLException e) {
            System.out.println("Nie jestes zalogowany");
        }
        return 0;
    }

}
