import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

public class RaportPanel extends JPanel implements ActionListener {
    private TopRaportPanel topRaportPanel;
    private BotRaportPanel botRaportPanel;
    RaportPanel(Window parentWindow){
        botRaportPanel = new BotRaportPanel(parentWindow, this);
        topRaportPanel = new TopRaportPanel(parentWindow, botRaportPanel);

        setLayout(new GridLayout(2,1,0,10));

        add(topRaportPanel);
        add(botRaportPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        topRaportPanel.raportTable.update();
    }
}

class TopRaportPanel extends JPanel{
    private JTextField titleField;
    private JScrollPane scrollPane;
    protected RaportTable raportTable;
    TopRaportPanel(Window parentWindow, BotRaportPanel botRaportPanel){
        titleField = new JTextField("Twoje przejazdy");
        titleField.setEditable(false);
        titleField.setBorder(null);
        titleField.setFont(new Font("Comic Sans MS",Font.PLAIN, 30));
        titleField.setHorizontalAlignment(JTextField.CENTER);

        raportTable = new RaportTable(parentWindow, botRaportPanel);
        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(500,220));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(raportTable);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(titleField);
        add(Box.createRigidArea(new Dimension(0,6)));
        add(scrollPane);
    }
}

class RaportTable extends JPanel {
    private Window parent;
    private BotRaportPanel botRaport;
    private JTable table;
    RaportTable(Window parentWindow, BotRaportPanel botRaportPanel){
        parent = parentWindow;
        botRaport = botRaportPanel;

        table = new JTable(60, 5);
        add(table);
        update();
    }

    protected void update(){
        boolean flag = true;
        botRaport.messField.setText("");
        //prepare parameters
        ArrayList<CustomJCBMenuItem> zonesList = botRaport.zonePanel.getChosenZones();
        CustomJCBMenuItem[] chosenZonesArray = zonesList.toArray(new CustomJCBMenuItem[zonesList.size()]);
        String[] zone = new String[chosenZonesArray.length];
        for (int i = 0; i < chosenZonesArray.length; i++) {
            zone[i] = chosenZonesArray[i].getText();
            System.out.println(zone[i]);
        }

        ArrayList<CustomJCBMenuItem> carsList = botRaport.carPanel.getChosenCars();
        CustomJCBMenuItem[] chosenCarsArray = carsList.toArray(new CustomJCBMenuItem[carsList.size()]);
        String[] vehicle = new String[chosenCarsArray.length];
        for (int i = 0; i < chosenCarsArray.length; i++) {
            vehicle[i] = chosenCarsArray[i].getText();
            System.out.println(vehicle[i]);
        }

        Timestamp start = Timestamp.valueOf("1970-01-01 00:00:01");
        Timestamp end = Timestamp.valueOf("2100-12-30 23:23:59");
        try{
            start = Timestamp.valueOf(botRaport.timePanel.start.getText() + " 00:00:00");
            end = Timestamp.valueOf(botRaport.timePanel.end.getText() + " 23:59:59");
        } catch (IllegalArgumentException e){
            flag = false;
            botRaport.messField.setText("Brak lub błędny przedział czasowy");
        }
        //get table
        if(!flag) {
            start = Timestamp.valueOf("1970-01-01 00:00:01");
            end = Timestamp.valueOf("2100-12-30 23:59:59");
        }
        ResultSet raport = parent.connection.raport(zone, vehicle, start, end);
        //update table
        int rows = 60;
        try{
            raport.last();
            rows = raport.getRow();
            raport.first();
        }
        catch (Exception e){}

        remove(table);
        table = new JTable(rows+1, 5);
        table.setEnabled(false);
        table.setValueAt("Nazwa strefy",0,0);
        table.setValueAt("Rozpoczęcie",0,1);
        table.setValueAt("Zakończenie",0,2);
        table.setValueAt("Rejestracja",0,3);
        table.setValueAt("Koszt",0,4);
        table.getColumnModel().getColumn(1).setPreferredWidth(132);
        table.getColumnModel().getColumn(2).setPreferredWidth(132);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);

        try{
            do{
                System.out.println(raport.getRow());
               table.setValueAt(raport.getString("ZONE.name"),raport.getRow(),0);
               table.setValueAt(raport.getTimestamp("PRESENCE.Start_date"),raport.getRow(),1);
               table.setValueAt(raport.getTimestamp("PRESENCE.End_date"),raport.getRow(),2);
               table.setValueAt(raport.getString("PRESENCE.Registration_number"),raport.getRow(),3);
               float zonePrice = parent.connection.getZonePrice(raport.getString("ZONE.name"));
               float typeFactor = parent.connection.getVehicleFactor(raport.getString("VEHICLE.Type"));
               float cost = parent.countCost(raport.getTimestamp("PRESENCE.End_date"),
                       raport.getTimestamp("PRESENCE.Start_date"), zonePrice, typeFactor);
               table.setValueAt(cost,raport.getRow(),4);
               parent.connection.addPayment(cost, raport.getInt("PRESENCE.id"));
            } while(raport.next());
        } catch(Exception e){}
        add(table);
        repaint();
        revalidate();
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

        messField = new JTextArea("");
        messField.setEditable(false);
        messField.setBorder(null);
        messField.setOpaque(false);
        messField.setLineWrap(true);
        messField.setWrapStyleWord(true);

        setLayout(new GridLayout(5,1,0,10));

        add(butPanel);
        add(timePanel);
        add(messField);
        add(carPanel);
        add(zonePanel);
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
    protected JTextField from;
    protected JTextField start;
    protected JTextField to;
    protected JTextField end;
    TimePanel(Window parentWindow, RaportPanel parentPanel){
        name = new JTextField("Wybierz przedział czasowy (rrrr-mm-dd)");
        name.setEditable(false);
        name.setBorder(null);
        name.setPreferredSize(new Dimension(250,20));

        from = new JTextField("Od:");
        from.setEditable(false);
        from.setBorder(null);
        from.setPreferredSize(new Dimension(20,20));
        start = new JTextField();
        start.setPreferredSize(new Dimension(100,20));
        start.addActionListener(parentPanel);

        to = new JTextField("Do:");
        to.setEditable(false);
        to.setBorder(null);
        to.setPreferredSize(new Dimension(20,20));
        end = new JTextField();
        end.setPreferredSize(new Dimension(100,20));
        end.addActionListener(parentPanel);

        add(name);
        add(from);
        add(start);
        add(to);
        add(end);
    }
}

class CarPanel extends JPanel implements ActionListener{
    protected JTextField name;
    protected JMenuBar bar;
    protected JMenu carList;
    protected ArrayList<CustomJCBMenuItem> boxes;
    CarPanel(Window parentWindow, RaportPanel parentPanel){
        name = new JTextField("Wybierz pojazdy");
        name.setEditable(false);
        name.setBorder(null);
        name.setPreferredSize(new Dimension(150,20));

        bar = new JMenuBar();
        carList = new JMenu("Wybierz pojazdy");
        carList.setPreferredSize(new Dimension(200,20));

        //take list of cars from database
        ResultSet cars = parentWindow.connection.RegistrationList();
        boxes = new ArrayList<CustomJCBMenuItem>();
        try{
            while(cars.next()){
                boxes.add(new CustomJCBMenuItem(cars.getString("VEHICLE.Registration_number")));
                boxes.get(boxes.size()-1).addActionListener(this);
                boxes.get(boxes.size()-1).addActionListener(parentPanel);
                carList.add(boxes.get(boxes.size()-1));
            }
        }catch (Exception e){

        }

        bar.add(carList);

        add(name);
        add(bar);
    }

    protected ArrayList<CustomJCBMenuItem> getChosenCars(){
        ArrayList<CustomJCBMenuItem> result = null;
        for(CustomJCBMenuItem r : boxes){
            if(r.isSelected()){
                if(result == null) result = new ArrayList<CustomJCBMenuItem>();
                result.add(r);
            }
        }
        if(result == null) return boxes;
        return result;
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

        bar.add(zoneList);

        add(name);
        add(bar);
    }

    protected ArrayList<CustomJCBMenuItem> getChosenZones(){
        ArrayList<CustomJCBMenuItem> result = null;
        for(CustomJCBMenuItem r : boxes){
            if(r.isSelected()){
                if(result == null) result = new ArrayList<CustomJCBMenuItem>();
                result.add(r);
            }
        }
        if(result == null) return boxes;
        return result;
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