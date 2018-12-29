import javax.swing.*;
import java.awt.*;

public class NewCarPanel extends JPanel {
    private JTextField titleField;
    private FPanel fPanel;
    private JButton addBut;
    private JTextArea messField;
    NewCarPanel(Window parentWindow){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleField = new JTextField("Dodaj nowe auto");
        titleField.setEditable(false);
        titleField.setBorder(null);
        titleField.setFont(new Font("Comic Sans MS",Font.PLAIN, 30));
        titleField.setHorizontalAlignment(JTextField.CENTER);

        fPanel = new FPanel();

        addBut = new JButton("Dodaj auto");

        messField = new JTextArea("TUTAJ BEDZIE INFO CO EWENTUALNIE ZLE WPISAL");
        messField.setEditable(false);
        messField.setBorder(null);
        messField.setOpaque(false);
        messField.setLineWrap(true);
        messField.setWrapStyleWord(true);

        add(titleField);
        add(Box.createRigidArea(new Dimension(0, 150)));
        add(fPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(addBut);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(messField);
    }
}

class FPanel extends JPanel{
    private JTextField text1;
    private JTextField text2;
    private JTextField field;

    private JMenuBar bar;
    private JMenu menu;
    private JRadioButtonMenuItem[] radios;
    private ButtonGroup group;
    FPanel(){
        setLayout(new GridLayout(2,2,4,10));

        text1 = new JTextField("Nr rejestracyjny auta");
        text1.setEditable(false);
        text1.setBorder(null);
        text1.setHorizontalAlignment(JTextField.RIGHT);

        field = new JTextField();

        text2 = new JTextField("Rodzaj auta");
        text2.setEditable(false);
        text2.setBorder(null);
        text2.setHorizontalAlignment(JTextField.RIGHT);

        bar = new JMenuBar();
        menu = new JMenu("Wybierz rodzaj auta");
        //pobierz rodzaje z bazy
        radios = new JRadioButtonMenuItem[4];
        group = new ButtonGroup();
        for(int i = 0; i < 4; i++){
            radios[i] = new JRadioButtonMenuItem("Type "+i);
            group.add(radios[i]);
            menu.add(radios[i]);
        }
        bar.add(menu);

        add(text1);
        add(field);
        add(text2);
        add(bar);
    }
}
