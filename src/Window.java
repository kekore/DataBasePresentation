import DataBase.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

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
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(600,660);
        setLocation(screenSize.width/2-300,screenSize.height/2-330);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        loginPanel = new LoginPanel(this);
        //raportPanel = new RaportPanel(this);
        add(loginPanel);

        setResizable(false);
        setVisible(true);

        state = State.LOGIN;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        switch (state){
            case LOGIN:
                if(((JButton)e.getSource()).getText().equals("Zaloguj")){
                    System.out.println("KLIKNIETO ZALOGUJ");
                    //if(authorized)
                    connection = new DBConnection(916361628, "BKi-I%Z(0mx");
                    //
                    remove(loginPanel);
                    mPanel = new MPanel(this);
                    add(mPanel);
                    revalidate();
                    repaint();
                    state = State.M;
                    //else wypisz blad
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
                    //connection.closeConnection();//logout of database
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
                    //Wyloguj
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
