import javax.swing.*;
import java.awt.*;

public class RaportPanel extends JPanel {
    private TopRaportPanel topRaportPanel;
    private BotRaportPanel botRaportPanel;
    RaportPanel(Window parentWindow){
        topRaportPanel = new TopRaportPanel(parentWindow, 6);
        botRaportPanel = new BotRaportPanel(parentWindow);

        setLayout(new GridLayout(2,1,0,10));

        add(topRaportPanel);
        add(botRaportPanel);
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

        raportTable = new RaportTable(parentWindow, 60);
        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
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
    BotRaportPanel(Window parentWindow){

    }
}