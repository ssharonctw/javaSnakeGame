import javax.swing.JFrame;

public class GameFrame extends JFrame {
    //constructor
    GameFrame(){
        /*
        since we don't need to use panel else where, we can simplify the
        GamePanel panel = new GamePanel();
        this.add(panel);
        to the below
        */
        this.add(new GamePanel());

        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();  //helps keep all components we add within the frame
        this.setVisible(true);
        this.setLocationRelativeTo(null); // help initialize the game application in the middle of the screen



    }

}
