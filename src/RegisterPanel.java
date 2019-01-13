import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RegisterPanel extends JPanel implements ActionListener {
    private JTextField titleField;
    private Window parent;
    private FormPanel formPanel;
    private JButton registerBut;
    private JButton backBut;
    private JTextArea messField;
    RegisterPanel(Window parentWindow){
        parent = parentWindow;
        formPanel = new FormPanel(parentWindow);

        titleField = new JTextField("Zarejestruj się");
        titleField.setEditable(false);
        titleField.setBorder(null);
        titleField.setFont(new Font("Comic Sans MS",Font.PLAIN, 18));
        titleField.setHorizontalAlignment(JTextField.CENTER);

        registerBut = new JButton("Zarejestruj");
        registerBut.addActionListener(this);

        backBut = new JButton("Powrót");
        backBut.addActionListener(parentWindow);

        messField = new JTextArea();
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
            //check if input is legal
            boolean legal = true;
            messField.setText("");
            //check imie
            if(formPanel.fields[0].getText().length() <= 0 ||
                    !formPanel.fields[0].getText().chars().allMatch(Character::isLetter)){
                messField.setText(messField.getText() + "Imie jest niepoprawne!\n");
                legal = false;
            }
            //check nazwisko
            if(formPanel.fields[1].getText().length() <= 0 ||
                    !formPanel.fields[1].getText().chars().allMatch(Character::isLetter)){
                messField.setText(messField.getText() + "Nazwisko jest niepoprawne!\n");
                legal = false;
            }
            //check nr tel
            if(formPanel.fields[2].getText().length() <= 8 || formPanel.fields[2].getText().length() >= 15 ||
                    !formPanel.fields[2].getText().chars().allMatch(Character::isDigit)){
                messField.setText(messField.getText() + "Numer telefonu jest niepoprawny!\n");
                legal = false;
            }
            //check nr rej
            if(formPanel.fields[3].getText().length() <= 4 || formPanel.fields[3].getText().length() >= 8){
                messField.setText(messField.getText() + "Numer rejestracyjny jest niepoprawny!\n");
                legal = false;
            }
            //check rodzaj
            JRadioButtonMenuItem type = formPanel.getChosenRadio(formPanel.radios1);
            if(type == null){
                messField.setText(messField.getText() + "Nie wybrano typu pojazdu!\n");
                legal = false;
            }
            //check rodzaj
            JRadioButtonMenuItem zone = formPanel.getChosenRadio(formPanel.radios2);
            if(zone == null){
                messField.setText(messField.getText() + "Nie wybrano dzielnicy!\n");
                legal = false;
            }
            //check nr karty
            if(formPanel.fields[4].getText().length() != 16 ||
                    !formPanel.fields[4].getText().chars().allMatch(Character::isDigit)){
                messField.setText(messField.getText() + "Numer karty jest niepoprawny!\n");
                legal = false;
            }
            //check nr CVV
            if(formPanel.fields[5].getText().length() != 3 ||
                    !formPanel.fields[5].getText().chars().allMatch(Character::isDigit)){
                messField.setText(messField.getText() + "Numer CVV jest niepoprawny!\n");
                legal = false;
            }
            //check data
            if(formPanel.fields[6].getText().length() != 5 || formPanel.fields[6].getText().charAt(2) != '/' ||
            !(new StringBuilder(formPanel.fields[6].getText())).deleteCharAt(2).toString().chars().allMatch(Character::isDigit)){
                messField.setText(messField.getText() + "Data wygaśnięcia jest niepoprawna!\n");
                legal = false;
            }
            //check pass
            if(!(new String(formPanel.pass1.getPassword())).equals(new String(formPanel.pass2.getPassword()))){
                messField.setText(messField.getText() + "Hasła nie są takie same!\n");
                legal = false;
            }
            if(new String(formPanel.pass1.getPassword()).length() < 8){
                messField.setText(messField.getText() + "Hasło jest za krótkie!\n");
                legal = false;
            }
            if(legal){
                //register new user
                String formData[] = new String[10];
                formData[0] = formPanel.fields[0].getText();
                formData[1] = formPanel.fields[1].getText();
                formData[2] = formPanel.fields[2].getText();
                formData[3] = formPanel.fields[3].getText();
                formData[4] = type.getText();
                formData[5] = zone.getText();
                formData[6] = formPanel.fields[4].getText();
                formData[7] = formPanel.fields[5].getText();
                //formData[8] = formPanel.fields[6].getText();
                //formData[8] = (new StringBuilder(formPanel.fields[6].getText())).deleteCharAt(2)).toString();
                String date = "20" + formPanel.fields[6].getText().substring(3,5) + "-" +
                        formPanel.fields[6].getText().substring(0,2) + "-01";
                System.out.println(date);
                formData[8] = date;
                //formData[9] = String.valueOf(new String (formPanel.pass1.getPassword()).hashCode());
                formData[9] = parent.hashPass(formPanel.pass1.getPassword());
                parent.connection.addUser(formData);
            }
        }
    }
}

class FormPanel extends JPanel implements ActionListener{
    private JTextField[] texts;
    protected JTextField[] fields;
    protected JPasswordField pass1;
    protected JPasswordField pass2;
    private JMenuBar bar1;
    private JMenuBar bar2;
    protected JMenu menu1;
    private JMenu menu2;
    private ButtonGroup group1;
    private ButtonGroup group2;
    protected ArrayList<JRadioButtonMenuItem> radios1;
    protected ArrayList<JRadioButtonMenuItem> radios2;
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

        pass1 = new JPasswordField();
        pass2 = new JPasswordField();

        bar1 = new JMenuBar();
        bar2 = new JMenuBar();
        menu1 = new JMenu("Wybierz rodzaj");
        menu2 = new JMenu("Wybierz strefę");

        //take list of car types and zones from database
        ResultSet cars = parentWindow.connection.vehicleList();
        group1 = new ButtonGroup();
        radios1 = new ArrayList<JRadioButtonMenuItem>();
        ResultSet zones = parentWindow.connection.zoneList();
        group2 = new ButtonGroup();
        radios2 = new ArrayList<JRadioButtonMenuItem>();
        try{
            while(cars.next()){
                radios1.add(new JRadioButtonMenuItem(cars.getString("TARIFF_VEHICLE.Type")));
                radios1.get(radios1.size()-1).addActionListener(this);
                group1.add(radios1.get(radios1.size()-1));
                menu1.add(radios1.get(radios1.size()-1));
            }
            while(zones.next()){
                radios2.add(new JRadioButtonMenuItem(zones.getString("ZONE.Name")));
                radios2.get(radios2.size()-1).addActionListener(this);
                group2.add(radios2.get(radios2.size()-1));
                menu2.add(radios2.get(radios2.size()-1));
            }
        }catch (Exception e) {

        }

        //pobierz rodzaje z bazy
        //group1 = new ButtonGroup();
        //radios1 = new JRadioButtonMenuItem[4];
        //group2 = new ButtonGroup();
        //radios2 = new JRadioButtonMenuItem[4];
        for(int i = 0; i < 4; i++){
            //radios1[i] = new JRadioButtonMenuItem("Typ " + i);
            //radios1[i].addActionListener(this);
            //radios2[i] = new JRadioButtonMenuItem("Strefa " + i);
            //radios2[i].addActionListener(this);
            //group1.add(radios1[i]);
            //menu1.add(radios1[i]);
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

    protected JRadioButtonMenuItem getChosenRadio(ArrayList<JRadioButtonMenuItem> list){
        JRadioButtonMenuItem result = null;
        for(JRadioButtonMenuItem r : list){
            if(r.isSelected()){
                result = r;
                break;
            }
        }
        return result;
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
