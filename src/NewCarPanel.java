import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;

public class NewCarPanel extends JPanel implements ActionListener {
    private Window parent;
    private JTextField titleField;
    private FPanel fPanel;
    private JButton addBut;
    private JButton backBut;
    private JTextArea messField;
    NewCarPanel(Window parentWindow){
        parent = parentWindow;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleField = new JTextField("Dodaj nowe auto");
        titleField.setEditable(false);
        titleField.setBorder(null);
        titleField.setFont(new Font("Comic Sans MS",Font.PLAIN, 30));
        titleField.setHorizontalAlignment(JTextField.CENTER);

        fPanel = new FPanel(parentWindow);

        addBut = new JButton("Dodaj auto");
        addBut.addActionListener(this);

        backBut = new JButton("Powrót do menu");
        backBut.addActionListener(parentWindow);

        messField = new JTextArea();
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
        add(backBut);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(messField);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (((JButton) e.getSource()).getText().equals("Dodaj auto")) {
            boolean legal = true;
            messField.setText("");
            if(fPanel.field.getText().length() <= 4 || fPanel.field.getText().length() >= 8){
                messField.setText(messField.getText() + "Numer rejestracyjny jest niepoprawny!\n");
                legal = false;
            }
            JRadioButtonMenuItem chosenRadio = fPanel.getChosenRadio();
            if(chosenRadio == null){
                messField.setText(messField.getText() + "Nie wybrano rodzaju pojazdu!\n");
                legal = false;
            }
            //proceed query for adding car
            if(legal){
                parent.connection.addVehicle(fPanel.field.getText(), chosenRadio.getText());
            }
        }
    }
}

class FPanel extends JPanel implements ActionListener{
    private JTextField text1;
    private JTextField text2;
    protected JTextField field;

    private JMenuBar bar;
    private JMenu menu;
    //private JRadioButtonMenuItem[] radios;
    private ArrayList<JRadioButtonMenuItem> radios;
    private ButtonGroup group;
    FPanel(Window parentWindow){
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
        ResultSet cars = parentWindow.connection.vehicleList();
        group = new ButtonGroup();
        radios = new ArrayList<JRadioButtonMenuItem>();
        try{
            while(cars.next()){
                radios.add(new JRadioButtonMenuItem(cars.getString("TARIFF_VEHICLE.Type")));
                radios.get(radios.size()-1).addActionListener(this);
                group.add(radios.get(radios.size()-1));
                menu.add(radios.get(radios.size()-1));
            }
        } catch (Exception e) {}

        //radios = new JRadioButtonMenuItem[4];
        //group = new ButtonGroup();
//        for(int i = 0; i < 4; i++){
//            radios[i] = new JRadioButtonMenuItem("Type "+i);
//            group.add(radios[i]);
//            menu.add(radios[i]);
//        }
        bar.add(menu);

        add(text1);
        add(field);
        add(text2);
        add(bar);
    }

    protected JRadioButtonMenuItem getChosenRadio(){
        JRadioButtonMenuItem result = null;
        for(JRadioButtonMenuItem r : radios){
            if(r.isSelected()){
                result = r;
                break;
            }
        }
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        for(JRadioButtonMenuItem radio : radios){
            if(e.getSource() == radio){
                //System.out.println(radio.getText());
                menu.setText(radio.getText());
                return;
            }
        }
    }
}
