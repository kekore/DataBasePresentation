import DataBase.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class Window extends JFrame implements ActionListener {
    private LoginPanel loginPanel;
    private MPanel mPanel;
    private RaportPanel raportPanel;
    private NewCarPanel newCarPanel;
    private RegisterPanel registerPanel;
    private State state;
    protected DBConnection connection;

    public Window(){
        super("Aplikacja");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(600,660);
        setLocation(screenSize.width/2-300,screenSize.height/2-330);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        loginPanel = new LoginPanel(this);
        add(loginPanel);

        setResizable(false);
        setVisible(true);

        state = State.LOGIN;
    }

    protected String hashPass(char[] pass){
        return String.valueOf(new String (pass).hashCode());
    }

    protected float hourDiff(Timestamp end, Timestamp start){
        System.out.println(end.getTime() - start.getTime());
        return (end.getTime() - start.getTime())/3600000.0f;
    }

    protected float countCost(Timestamp end, Timestamp start, float zonePrice, float typeFactor){
        return Math.round(hourDiff(end, start) * 120.0f * zonePrice * typeFactor)/100.0f;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        switch (state){
            case LOGIN:
                if(((JButton)e.getSource()).getText().equals("Zaloguj")){
                    System.out.println("KLIKNIETO ZALOGUJ");
                    //char[] pass1 = {'z','a','q','1','@','W','S','X'};
                    //connection = new DBConnection(114734205, hashPass(pass1));
                    connection = null;
                    if(loginPanel.topPanel.numberField.getText().length() > 0 &&
                            loginPanel.topPanel.numberField.getText().chars().allMatch(Character::isDigit)){
                        long phoneNumber = Long.parseLong(loginPanel.topPanel.numberField.getText());
                        connection = new DBConnection(phoneNumber, hashPass(loginPanel.topPanel.passField.getPassword()));
                    }

                    if(connection != null && connection.login != 0) {
                        remove(loginPanel);
                        mPanel = new MPanel(this);
                        add(mPanel);
                        revalidate();
                        repaint();
                        state = State.M;
                    }
                    else{
                        loginPanel.topPanel.messField.setText("Błędne dane logowania!");
                    }
                }
                else if(((JButton)e.getSource()).getText().equals("Zarejestruj się")){
                    System.out.println("KLIKNIETO ZAREJESTRUJ");
                    connection = new DBConnection("123");
                    remove(loginPanel);
                    registerPanel = new RegisterPanel(this);
                    add(registerPanel);
                    revalidate();
                    repaint();
                    state = State.REGISTER;
                }
                else if(((JButton)e.getSource()).getText().equals("Zamknij aplikację")){
                    dispose();
                }
                break;
            case M:
                if(((JButton)e.getSource()).getText().equals("Zobacz raporty")){
                    remove(mPanel);
                    raportPanel = new RaportPanel(this);
                    add(raportPanel);
                    revalidate();
                    repaint();
                    state = State.RAPORT;
                }
                else if(((JButton)e.getSource()).getText().equals("Dodaj nowe auto")){
                    remove(mPanel);
                    newCarPanel = new NewCarPanel(this);
                    add(newCarPanel);
                    revalidate();
                    repaint();
                    state = State.NEWCAR;
                }
                else if(((JButton)e.getSource()).getText().equals("Wyloguj")){
                    connection.closeConnection();
                    loginPanel.topPanel.numberField.setText("");
                    loginPanel.topPanel.passField.setText("");
                    loginPanel.topPanel.messField.setText("");
                    remove(mPanel);
                    add(loginPanel);
                    revalidate();
                    repaint();
                    state = State.LOGIN;
                }
                break;
            case RAPORT:
                if(((JButton)e.getSource()).getText().equals("Powrót do menu")){
                    remove(raportPanel);
                    add(mPanel);
                    revalidate();
                    repaint();
                    state = State.M;
                }
                break;
            case NEWCAR:
                if(((JButton)e.getSource()).getText().equals("Powrót do menu")){
                    remove(newCarPanel);
                    add(mPanel);
                    revalidate();
                    repaint();
                    state = State.M;
                }
                break;
            case REGISTER:
                if(((JButton)e.getSource()).getText().equals("Powrót")){
                    connection.closeConnection();
                    remove(registerPanel);
                    add(loginPanel);
                    revalidate();
                    repaint();
                    state = State.LOGIN;
                }
        }
    }
}
