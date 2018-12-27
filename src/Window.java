import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame implements ActionListener {
    //private JPanel loginPanel;
    private State state;

    public Window(){
        super("Aplikacja");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(600,600);
        setLocation(screenSize.width/2-300,screenSize.height/2-300);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        add(new LoginPanel(this));

        setResizable(false);
        setVisible(true);

        state = State.LOGIN;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        switch (state){
            case LOGIN:
                if(((JButton)e.getSource()).getText().equals("Zaloguj")){
                    System.out.println("KLIKNIETO ZALOGUJ");
                    //
                }
                break;
        }
    }
}
