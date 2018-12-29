import javax.swing.*;
import java.awt.*;

public class MPanel extends JPanel {
    private JTextField titleField;
    private JButton raportBut;
    private JButton addCarBut;
    MPanel(Window parentWindow){
        titleField = new JTextField("Miłego dnia!");
        titleField.setEditable(false);
        titleField.setBorder(null);
        titleField.setFont(new Font("Comic Sans MS",Font.PLAIN, 30));
        titleField.setHorizontalAlignment(JTextField.CENTER);

        raportBut = new JButton("Zobacz raporty");
        raportBut.addActionListener(parentWindow);
        raportBut.setAlignmentX(Component.CENTER_ALIGNMENT);

        addCarBut = new JButton("Dodaj nowe auto");
        addCarBut.addActionListener(parentWindow);
        addCarBut.setAlignmentX(Component.CENTER_ALIGNMENT);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(titleField);
        add(Box.createRigidArea(new Dimension(0,150)));
        add(raportBut);
        add(Box.createRigidArea(new Dimension(0,20)));
        add(addCarBut);
    }
}
