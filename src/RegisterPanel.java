import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//TODO maskowanie hasla
public class RegisterPanel extends JPanel implements ActionListener {
    private JTextField titleField;
    private FormPanel formPanel;
    private JButton registerBut;
    private JTextArea messField;
    RegisterPanel(Window parentWindow){
        formPanel = new FormPanel();

        titleField = new JTextField("Zarejestruj się");
        titleField.setEditable(false);
        titleField.setBorder(null);
        titleField.setFont(new Font("Comic Sans MS",Font.PLAIN, 30));
        titleField.setHorizontalAlignment(JTextField.CENTER);

        registerBut = new JButton("Zarejestruj");
        registerBut.addActionListener(this);

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
        add(messField);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(((JButton)e.getSource()).getText().equals("Zarejestruj")){
            messField.setText(messField.getText() + "XD");
        }
    }
}

class FormPanel extends JPanel{
    private JTextField[] texts;
    private JTextField[] fields;
    private JMenuBar bar1;
    private JMenuBar bar2;
    private JMenu menu1;
    private JMenu menu2;
    private ButtonGroup group1;
    private ButtonGroup group2;
    private JRadioButtonMenuItem[] radios1;
    private JRadioButtonMenuItem[] radios2;
    FormPanel(){
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

        fields = new JTextField[9];
        for(int f = 0; f < 9; f++){
            fields[f] = new JTextField();
        }
        /*for(JTextField f : fields){
            f = new JTextField();
        }*/

        bar1 = new JMenuBar();
        bar2 = new JMenuBar();
        menu1 = new JMenu("Wybierz rodzaj");
        menu2 = new JMenu("Wybierz strefę");
        //pobierz rodzaje z bazy
        group1 = new ButtonGroup();
        radios1 = new JRadioButtonMenuItem[4];
        group2 = new ButtonGroup();
        radios2 = new JRadioButtonMenuItem[4];
        for(int i = 0; i < 4; i++){
            radios1[i] = new JRadioButtonMenuItem("Typ " + i);
            radios2[i] = new JRadioButtonMenuItem("Strefa " + i);
            group1.add(radios1[i]);
            menu1.add(radios1[i]);
            group2.add(radios2[i]);
            menu2.add(radios2[i]);
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
        add(fields[7]);
        add(texts[10]);
        add(fields[8]);
    }
}
