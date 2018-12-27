import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoginPanel extends JPanel implements ActionListener {
    private TopPanel topPanel;
    private ButtonPanel buttonPanel;
    public LoginPanel(Window parentWindow){
        //parent = parentWindow;

        setLayout(new GridLayout(2,1,0,50));
        topPanel = new TopPanel(parentWindow);
        //topPanel.setPreferredSize(new Dimension(100,300));
        add(topPanel);
        buttonPanel = new ButtonPanel(parentWindow);
        //buttonPanel.setPreferredSize(new Dimension(100,40));
        add(buttonPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e){

    }
}

class TopPanel extends JPanel{
    //private JFrame parent;

    private JTextField titleField;
    private BufferedImage carImage;
    private JLabel carLabel;
    private JTextField numberPrompt;
    protected JTextField numberField;
    private JTextField passPrompt;
    protected JTextField passField;

    protected TopPanel(Window parentWindow){
        //parent = parentWindow;

        titleField = new JTextField("Witaj w WarZone!");
        titleField.setEditable(false);
        titleField.setBorder(null);
        titleField.setFont(new Font("Comic Sans MS",Font.PLAIN, 30));


        try {
            carImage = ImageIO.read(new File("car.png"));
        } catch (IOException e){}
        carLabel = new JLabel(new ImageIcon(carImage.getScaledInstance(100,50, Image.SCALE_SMOOTH)));

        numberPrompt = new JTextField("Numer telefonu:");
        numberPrompt.setEditable(false);
        numberPrompt.setBorder(null);
        numberField = new JTextField();

        passPrompt = new JTextField("Hasło:");
        passPrompt.setEditable(false);
        passPrompt.setBorder(null);
        passField = new JTextField();

        setLayout(new GridLayout(6,1,0,0));

        add(titleField);
        add(carLabel);
        add(numberPrompt);
        add(numberField);
        add(passPrompt);
        add(passField);
    }
}

class ButtonPanel extends JPanel{
    //private JFrame parent;

    private JButton loginBut;
    private JButton registerBut;

    protected ButtonPanel(Window parentWindow){
        //parent = parentWindow;

        //setLayout(new FlowLayout());
        //setPreferredSize(new Dimension(100,500));

        loginBut = new JButton("Zaloguj");
        loginBut.setMaximumSize(new Dimension(80,30));
        loginBut.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBut.addActionListener(parentWindow);

        registerBut = new JButton("Zarejestruj");
        registerBut.setEnabled(false);
        registerBut.setPreferredSize(new Dimension(100,30));
        registerBut.setAlignmentX(Component.CENTER_ALIGNMENT);

        //setLayout(new GridLayout(2,1,20,20));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(loginBut);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(registerBut);
    }

}
