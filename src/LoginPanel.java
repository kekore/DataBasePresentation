import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoginPanel extends JPanel {
    protected TopLoginPanel topPanel;
    private ButtonLoginPanel buttonPanel;
    public LoginPanel(Window parentWindow){
        setLayout(new GridLayout(2,1,0,50));
        topPanel = new TopLoginPanel(parentWindow);
        add(topPanel);
        buttonPanel = new ButtonLoginPanel(parentWindow);
        add(buttonPanel);
    }
}

class TopLoginPanel extends JPanel{
    private JTextField titleField;
    private BufferedImage carImage;
    private JLabel carLabel;
    private JTextField numberPrompt;
    protected JTextField numberField;
    private JTextField passPrompt;
    protected JPasswordField passField;
    protected JTextField messField;

    protected TopLoginPanel(Window parentWindow){
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
        passField = new JPasswordField();

        messField = new JTextField();
        messField.setEditable(false);
        messField.setBorder(null);
        messField.setPreferredSize(new Dimension(100,20));

        setLayout(new GridLayout(7,1,0,0));

        add(titleField);
        add(carLabel);
        add(numberPrompt);
        add(numberField);
        add(passPrompt);
        add(passField);
        add(messField);
    }
}

class ButtonLoginPanel extends JPanel{
    private JButton loginBut;
    private JButton registerBut;
    private JButton exitBut;

    protected ButtonLoginPanel(Window parentWindow){
        loginBut = new JButton("Zaloguj");
        loginBut.setMaximumSize(new Dimension(80,30));
        loginBut.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBut.addActionListener(parentWindow);

        registerBut = new JButton("Zarejestruj się");
        registerBut.setPreferredSize(new Dimension(100,30));
        registerBut.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBut.addActionListener(parentWindow);

        exitBut = new JButton("Zamknij aplikację");
        exitBut.setPreferredSize(new Dimension(100,30));
        exitBut.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBut.addActionListener(parentWindow);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(loginBut);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(registerBut);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(exitBut);
    }
}
