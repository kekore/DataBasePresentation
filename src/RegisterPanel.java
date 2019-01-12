import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;

//TODO maskowanie hasla
public class RegisterPanel extends JPanel implements ActionListener {
    private JTextField titleField;
    private FormPanel formPanel;
    private JButton registerBut;
    private JButton backBut;
    private JTextArea messField;
    RegisterPanel(Window parentWindow){
        formPanel = new FormPanel(parentWindow);

        titleField = new JTextField("Zarejestruj się");
        titleField.setEditable(false);
        titleField.setBorder(null);
        titleField.setFont(new Font("Comic Sans MS",Font.PLAIN, 30));
        titleField.setHorizontalAlignment(JTextField.CENTER);

        registerBut = new JButton("Zarejestruj");
        registerBut.addActionListener(this);

        backBut = new JButton("Powrót");
        backBut.addActionListener(parentWindow);

        messField = new JTextArea("TUTAJ BEDZIE INFO CO EWENTUALNIE ZLE WPISAL");
        messField.setEditable(false);
        messField.setBorder(null);
        messField.setOpaque(false);
        //messField.setHorizontalAlignment(JTextField.CENTER);
        messField.setLineWrap(true);
        messField.setWrapStyleWord(true);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(titleField);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(formPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(registerBut);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(backBut);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(messField);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(((JButton)e.getSource()).getText().equals("Zarejestruj")){
            messField.setText(messField.getText() + "XD");
        }
    }
}

class FormPanel extends JPanel implements ActionListener{
    private JTextField[] texts;
    private JTextField[] fields;
    private JPasswordField pass1;
    private JPasswordField pass2;
    private JMenuBar bar1;
    private JMenuBar bar2;
    private JMenu menu1;
    private JMenu menu2;
    private ButtonGroup group1;
    private ButtonGroup group2;
    private JRadioButtonMenuItem[] radios1;
    //private JRadioButtonMenuItem[] radios2;
    private ArrayList<JRadioButtonMenuItem> radios2;
    FormPanel(Window parentWindow){
        setLayout(new GridLayout(11, 2, 4, 6));

        texts = new JTextField[11];
        texts[0] = new JTextField("Imię");
        texts[1] = new JTextField("Nazwisko");
        texts[2] = new JTextField("Nr telefonu");
        texts[3] = new JTextField("Nr rejestracyjny auta");
        texts[4] = new JTextField("Rodzaj auta");
        texts[5] = new JTextField("Dzielnica zamieszkania");
        texts[6] = new JTextField("Nr karty płatniczej");
        texts[7] = new JTextField("CVV");
        texts[8] = new JTextField("Data wygaśnięcia karty");
        texts[9] = new JTextField("Hasło");
        texts[10] = new JTextField("Powtórz hasło");

        for(JTextField t : texts){
            t.setEditable(false);
            t.setBorder(null);
            t.setHorizontalAlignment(JTextField.RIGHT);
        }

        fields = new JTextField[7];
        for(int f = 0; f < 7; f++){
            fields[f] = new JTextField();
        }
        /*for(JTextField f : fields){
            f = new JTextField();
        }*/
        pass1 = new JPasswordField();
        pass2 = new JPasswordField();

        bar1 = new JMenuBar();
        bar2 = new JMenuBar();
        menu1 = new JMenu("Wybierz rodzaj");
        menu2 = new JMenu("Wybierz strefę");

        //take list of zones from database
        ResultSet zones = parentWindow.connection.zoneList();
        group2 = new ButtonGroup();
        radios2 = new ArrayList<JRadioButtonMenuItem>();
        try{
            while(zones.next()){
                radios2.add(new JRadioButtonMenuItem(zones.getString("ZONE.Name")));
                radios2.get(radios2.size()-1).addActionListener(this);
                group2.add(radios2.get(radios2.size()-1));
                menu2.add(radios2.get(radios2.size()-1));
            }
        }catch (Exception e){

        }

        //pobierz rodzaje z bazy
        group1 = new ButtonGroup();
        radios1 = new JRadioButtonMenuItem[4];
        //group2 = new ButtonGroup();
        //radios2 = new JRadioButtonMenuItem[4];
        for(int i = 0; i < 4; i++){
            radios1[i] = new JRadioButtonMenuItem("Typ " + i);
            radios1[i].addActionListener(this);
            //radios2[i] = new JRadioButtonMenuItem("Strefa " + i);
            //radios2[i].addActionListener(this);
            group1.add(radios1[i]);
            menu1.add(radios1[i]);
            //group2.add(radios2[i]);
            //menu2.add(radios2[i]);
        }
        bar1.add(menu1);
        bar2.add(menu2);

        add(texts[0]);
        add(fields[0]);
        add(texts[1]);
        add(fields[1]);
        add(texts[2]);
        add(fields[2]);
        add(texts[3]);
        add(fields[3]);
        add(texts[4]);
        add(bar1);
        add(texts[5]);
        add(bar2);
        add(texts[6]);
        add(fields[4]);
        add(texts[7]);
        add(fields[5]);
        add(texts[8]);
        add(fields[6]);
        add(texts[9]);
        add(pass1);
        add(texts[10]);
        add(pass2);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        for(JRadioButtonMenuItem radio : radios1){
            if(e.getSource() == radio){
                //System.out.println(radio.getText());
                menu1.setText(radio.getText());
                return;
            }
        }
        for(JRadioButtonMenuItem radio : radios2){
            if(e.getSource() == radio){
                menu2.setText(radio.getText());
                return;
            }
        }
    }
}
