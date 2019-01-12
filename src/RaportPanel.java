import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RaportPanel extends JPanel implements ActionListener {
    private TopRaportPanel topRaportPanel;
    private BotRaportPanel botRaportPanel;
    RaportPanel(Window parentWindow){
        topRaportPanel = new TopRaportPanel(parentWindow, 60);
        //topRaportPanel.setPreferredSize(new Dimension(300,600));
        botRaportPanel = new BotRaportPanel(parentWindow, this);

        setLayout(new GridLayout(2,1,0,10));
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(topRaportPanel);
        add(botRaportPanel);

        //TODO get all info and put into table
    }

    @Override
    public void actionPerformed(ActionEvent e){
        //TODO if something changed in bottom panel, do select from database again
    }
}

class TopRaportPanel extends JPanel{
    private JTextField titleField;
    private JScrollPane scrollPane;
    private RaportTable raportTable;
    TopRaportPanel(Window parentWindow, int rows){
        titleField = new JTextField("Twoje przejazdy");
        titleField.setEditable(false);
        titleField.setBorder(null);
        titleField.setFont(new Font("Comic Sans MS",Font.PLAIN, 30));
        //titleField.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleField.setHorizontalAlignment(JTextField.CENTER);

        raportTable = new RaportTable(parentWindow, rows);
        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(400,220));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(raportTable);
        //scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(titleField);
        add(Box.createRigidArea(new Dimension(0,6)));
        add(scrollPane);
    }
}

class RaportTable extends JPanel {
    private JTable table;
    RaportTable(Window parentWindow, int rows){
        table = new JTable(rows, 5);
        table.setEnabled(false);
        table.setValueAt("Nazwa strefy",0,0);
        table.setValueAt("Rozpoczęcie",0,1);
        table.setValueAt("Zakończenie",0,2);
        table.setValueAt("Typ pojazdu",0,3);
        table.setValueAt("Koszt",0,4);
        add(table);
    }
}

class BotRaportPanel extends JPanel{
    protected ButPanel butPanel;
    protected TimePanel timePanel;
    protected CarPanel carPanel;
    protected ZonePanel zonePanel;
    protected JTextArea messField;
    BotRaportPanel(Window parentWindow, RaportPanel parentPanel){
        butPanel = new ButPanel(parentWindow);
        timePanel = new TimePanel(parentWindow, parentPanel);
        carPanel = new CarPanel(parentWindow, parentPanel);
        zonePanel = new ZonePanel(parentWindow, parentPanel);

        messField = new JTextArea("TUTAJ BEDZIE INFO CO EWENTUALNIE ZLE WPISAL");
        messField.setEditable(false);
        messField.setBorder(null);
        messField.setOpaque(false);
        messField.setLineWrap(true);
        messField.setWrapStyleWord(true);

        setLayout(new GridLayout(5,1,0,10));

        add(butPanel);
        add(timePanel);
        add(carPanel);
        add(zonePanel);
        add(messField);
    }
}

class ButPanel extends JPanel{
    private JButton backBut;
    protected JButton exportBut;
    ButPanel(Window parentWindow){
        backBut = new JButton("Powrót do menu");
        backBut.addActionListener(parentWindow);

        exportBut = new JButton("Eksportuj do pliku");

        setLayout(new GridLayout(1,2,10,0));

        add(backBut);
        add(exportBut);
    }
}

class TimePanel extends JPanel{
    protected JTextField name;
    protected JTextField start;
    protected JTextField end;
    TimePanel(Window parentWindow, RaportPanel parentPanel){
        name = new JTextField("Wybierz przedział czasowy");
        name.setEditable(false);
        name.setBorder(null);
        name.setPreferredSize(new Dimension(150,20));

        start = new JTextField();
        start.setPreferredSize(new Dimension(100,20));
        start.addActionListener(parentPanel);

        end = new JTextField();
        end.setPreferredSize(new Dimension(100,20));
        end.addActionListener(parentPanel);

        //setLayout(new GridLayout(1,3,10,0));

        add(name);
        add(start);
        add(end);
    }
}

class CarPanel extends JPanel implements ActionListener{
    protected JTextField name;
    //private JList<JCheckBox> carList;
    //private JComboBox<JCheckBox> carList;
    protected JMenuBar bar;
    protected JMenu carList;
    protected CustomJCBMenuItem[] boxes;
    //private CarBoxes carBoxes;
    //private JScrollPane scrollPane;
    //private JComboBox comboBox;
    //private JCheckBoxMenuItem menu;
    CarPanel(Window parentWindow, RaportPanel parentPanel){
        name = new JTextField("Wybierz samochody");
        name.setEditable(false);
        name.setBorder(null);
        name.setPreferredSize(new Dimension(150,20));

        //carList = new JList<JCheckBox>(boxes);
        //carList = new JComboBox<JCheckBox>(boxes);
        bar = new JMenuBar();
        carList = new JMenu("Wybierz samochody");
        carList.setPreferredSize(new Dimension(200,20));

        //take list of cars from database
        //ArrayList<String> cars = new ArrayList<String>();
        boxes = new CustomJCBMenuItem[4];
        for(int i = 0; i < 4; i++){
            boxes[i] = new CustomJCBMenuItem("Car " + i);
            boxes[i].addActionListener(this);
            boxes[i].addActionListener(parentPanel);
            carList.add(boxes[i]);
        }
        /*carBoxes = new CarBoxes(cars);
        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(100,30));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(carBoxes);*/
        //comboBox = new JComboBox();
        //comboBox.add(new JCheckBox("Car0"));

        bar.add(carList);
        //bar.is
        //carList = new JList<>();
        //menu = new JCheckBoxMenuItem("menu");

        add(name);
        add(bar);
        //add(menu);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        for(CustomJCBMenuItem checkbox : boxes){
            if(e.getSource() == checkbox){
                String newText = "";
                for(CustomJCBMenuItem c : boxes){
                    if(c.getState()){
                        if(newText.equals("")) newText += c.getText();
                        else newText = newText + "; " + c.getText();
                    }
                }
                if(newText.equals("")) newText = "Wybierz samochody";
                carList.setText(newText);
                return;
            }
        }
    }
}

class ZonePanel extends JPanel implements ActionListener{
    protected JTextField name;
    protected JMenuBar bar;
    protected JMenu zoneList;
    protected ArrayList<CustomJCBMenuItem> boxes;
    ZonePanel(Window parentWindow, RaportPanel parentPanel){
        name = new JTextField("Wybierz strefy");
        name.setEditable(false);
        name.setBorder(null);
        name.setPreferredSize(new Dimension(150,20));

        bar = new JMenuBar();
        zoneList = new JMenu("Wybierz strefy");
        zoneList.setPreferredSize(new Dimension(200,20));

        //take list of zones from database
        ResultSet zones = parentWindow.connection.zoneList();
        boxes = new ArrayList<CustomJCBMenuItem>();
        try{
            while(zones.next()){
                boxes.add(new CustomJCBMenuItem(zones.getString("ZONE.Name")));
                boxes.get(boxes.size()-1).addActionListener(this);
                boxes.get(boxes.size()-1).addActionListener(parentPanel);
                zoneList.add(boxes.get(boxes.size()-1));
            }
        }catch (Exception e){

        }
//        for(int i = 0; i < 10; i++){
//            boxes[i] = new CustomJCBMenuItem("Zone " + i);
//            boxes[i].addActionListener(this);
//            boxes[i].addActionListener(parentPanel);
//            zoneList.add(boxes[i]);
//        }

        bar.add(zoneList);

        add(name);
        add(bar);
    }

    public void actionPerformed(ActionEvent e){
        for(CustomJCBMenuItem checkbox : boxes){
            if(e.getSource() == checkbox){
                String newText = "";
                for(CustomJCBMenuItem c : boxes){
                    if(c.getState()){
                        if(newText.equals("")) newText += c.getText();
                        else newText = newText + "; " + c.getText();
                    }
                }
                if(newText.equals("")) newText = "Wybierz strefy";
                zoneList.setText(newText);
                return;
            }
        }
    }
}

/*class CarBoxes extends JPanel{
    private JCheckBox[] boxes;
    CarBoxes(ArrayList<String> cars){
        boxes = new JCheckBox[cars.size()];
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for(int b = 0; b < cars.size(); b++){
            boxes[b] = new JCheckBox(cars.get(b));
            add(boxes[b]);
        }
    }
}*/

class CustomJCBMenuItem extends JCheckBoxMenuItem{
    public CustomJCBMenuItem(String text) {
        super(text);
    }

    @Override
    protected void processMouseEvent(MouseEvent evt) {
        if (evt.getID() == MouseEvent.MOUSE_RELEASED && contains(evt.getPoint())) {
            doClick();
            setArmed(true);
        } else {
            super.processMouseEvent(evt);
        }
    }
}