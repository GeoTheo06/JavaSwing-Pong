/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author user
 */
    
public class pongTraining extends JPanel implements KeyListener, ActionListener, MouseListener {
    public void run(){
            JFrame frame = new JFrame();
        frame.setBounds(0, 0, 1919, 1079);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("TRAINING");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        pongTraining pongtraining = new pongTraining();
        frame.add(pongtraining);
        frame.setVisible(true);
}
    boolean play = true; // οταν ειναι true σημαινει οτι οι παικτες παιζουν ακομη και η μπαλα κυλαει
    int score1;
    int score2;
    Timer timer;
    int delay = 3;
    Timer timer2 = new Timer(3000, this);
    String color;
    boolean game = true;
    boolean pause = false;
    Clip Clip2;
    boolean UP, DOWN;
    
    int previousBalldirX;
    int previousBalldirY;
    
    // η θεση της μπαλας
    int ballposX = 960;
    int ballposY = 517;
    
    // η κατευθυνση της μπαλας
    int balldirX; 
    int balldirY;
    
    // η θεση Y του παικτη 1
    int player1posY1 = 464;
    int player1posY2 = 130;
    
    // η θεση του παικτη 2
    int player2posY1 = 0;
    int player2posY2 = 1919;
    
    public pongTraining() {
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
        
        Random random1 = new Random();
        Random random2 = new Random();
        
        // αν η κατευθυνση της μπαλας ειναι 0, η μπαλα θα μενει στασιμη, οποτε  για να κινειται πρεπει να την θεσουμε σε εναν αλλο αριθμο
        if (balldirX == 0 || balldirY == 0) {
            
            balldirX = random1.nextInt(2 + 5) - 5;
            balldirY = random2.nextInt(2 + 5) - 5;
        }
    }
    public void paint (Graphics g) {
        super.paint(g); // (σε συνδιασμο με το repaint(); το οποίο 
        // βρισκεται πιο κατω μαζι με περιγραφη της λειτουργιιας του)ανανεωνει 
        // τα γραφικα ωστε να φαινεται οτι τα αντικειμενα μετακινουνται
        
        g.setColor(Color.black); //σεταρει το χρωμα σε μαυρο
        g.fillRect(0, 0, 1920,1079); // ζωγραφιζει ενα τετραγωνο στις 
        // συντεταγμενες: 0, 0, 1919,1079. Σε αυτην την περιπτωση εφτιαξα
        // το background
        
        //player1
        g.setColor(Color.blue);
        g.fillRect(50, player1posY1, 25, player1posY2);
        
        //player2
        g.setColor(Color.red);
        g.fillRect(1854, player2posY1, 100, player2posY2);
        
        
        //player Movement
        if (UP == true) {
            if (player1posY1 <= 0) { // αν το pad του 1ου παικτη εχει φτασει τα τερμα πανω pixles
                player1posY1 = 0; // πηγαινε τον στο τερμα των πανω pixels
            } else  {
                moveUp();
            }
        }
        if (DOWN == true) {
            if (player1posY1 >= 926) {
                player1posY1 = 926;
            } else {
                moveDown();
            }
        }
        
        //Blue Line
        g.setColor(Color.blue);
        g.fillRect(955, 130, 5, 1050);
        
        //Red Line
        g.setColor(Color.red);
        g.fillRect(961, 130, 5, 1050);
        
        //ball
            if (ballposX > 960) {
                g.setColor(Color.red);
                g.fillOval(ballposX, ballposY, 30, 30);
            } else if (ballposX < 945) {
                g.setColor(Color.blue);
                g.fillOval(ballposX, ballposY, 30, 30);
            } // σχεδιασα την μπαλα ωστε οταν περναει στα δεξια να γινεται κοκκινη και οταν περναει στα αριστερα να γινεται μπλε
        
        if (game) {
            //back
            ImageIcon image2 = new ImageIcon(getClass().getClassLoader().getResource("back.png"));
            image2.paintIcon(this, g, 840, 35);
            
            if (pause) {
        
                //pause
                ImageIcon image1 = new ImageIcon(getClass().getClassLoader().getResource("resume.png"));
                image1.paintIcon(this, g, 940, 45);
                
                balldirX = 0;
                balldirY = 0;
                
                //stopped
                ImageIcon image4 = new ImageIcon(getClass().getClassLoader().getResource("stopped.png"));
                image4.paintIcon(this, g, 650, 200);
            } else if (!pause) {
                //resume
                ImageIcon image3 = new ImageIcon(getClass().getClassLoader().getResource("pause.png"));
                image3.paintIcon(this, g, 924, 35);
            }
        
            //restart
            ImageIcon image4 = new ImageIcon(getClass().getClassLoader().getResource("restart.png"));
            image4.paintIcon(this, g, 1003, 35);
        }
        
        if (ballposX <= 0) { // οταν η μπαλα ξεπερασει το τερμα των αριστερων pixels στον αξονα χ
            play = false; //σταματαει το παιχνιδι
            
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 70));
            g.drawString("ΕΧΑΣΕΣ!  ΣΚΟΡ:  " + score1, 680, 400);
            
            g.setFont(new Font("serif",  Font.BOLD, 30));
            g.drawString("ΠΑΤΑ ENTER ΓΙΑ ΝΑ ΣΥΝΕΧΙΣΕΙΣ", 725, 500);
        }
        //score1
        g.setColor(Color.blue);
        g.setFont(new Font("serif", Font.BOLD, 100));
        g.drawString("" + score1, 830, 1020);
        
        g.dispose(); // Δεν ξερω τι κανει (εχει να κανει με την μνημη)
    }
    
    public void keyTyped(KeyEvent e) {
        
    }

    public void keyPressed(KeyEvent e) {
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
        if (!pause) {
            previousBalldirX = balldirX;
            previousBalldirY = balldirY;   
        } else {
            balldirX = previousBalldirX;
            balldirY = previousBalldirY;
        }
          pause = !pause;
        }
        
        if (!play) { // αν το παιχνιδι εχει σταματησει
                play = true; //ξεκινα το και κανε reset τα variables:
                
                score1 = 0;
                
                player1posY1 = 464;
                player1posY2 = 130;
                
                player2posY1 = 0;
                player2posY2 = 1919;
                
                Random rand1 = new Random();
                Random rand2 = new Random();
                balldirX = rand1.nextInt(2 + 5) - 5;
                balldirY = rand2.nextInt(2 + 5) - 5;
                
                // αυτο το εξηγω πιο πανω
                if (balldirX == 0 || balldirY == 0) {
                balldirX = -3;
                balldirY = -2;
                }
                
                ballposX = 960;
                ballposY = 517;
                
                repaint(); // το εξηγω πιο κατω
        }
        
        //Player Movement
        if (e.getKeyCode() == KeyEvent.VK_UP) UP = true;
        if(e.getKeyCode() == KeyEvent.VK_DOWN) DOWN = true;
    }
    
    public void keyReleased(KeyEvent e) {
        //Player Movement
        if (e.getKeyCode() == KeyEvent.VK_UP) UP = false;
        if(e.getKeyCode() == KeyEvent.VK_DOWN) DOWN = false;
    }

    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play && game) {
            if (new Rectangle(ballposX, ballposY, 30, 30).intersects(new Rectangle(50, player1posY1, 25, player1posY2))) { //αν η μπαλα ακουμπησει το πρωτο paddle:
                
                //οταν η μπαλα χτυπαει πανω απο το paddle την στελνει προς τα πανω
                if (new Rectangle(ballposX, ballposY, 30, 30).intersects(new Rectangle(50, player1posY1 - 10, 25, 5))) {
                    ballposY = ballposY - 50;
                    balldirY = -balldirY;
                    balldirX = -2;
                
                //και οταν χτυπαει κατω απο το paddle την στελνει προς τα κατω
                } else if (new Rectangle(ballposX, ballposY, 30, 30).intersects(new Rectangle(50 , player1posY1 + 135, 25, 5))) {
                    ballposY = ballposY + 50;
                    balldirY = -balldirY;
                    balldirX = -2;
                    
                // διαφορεικα, θα εχει χτυπησει στο μπροστινο μερος οποτε θα κανει αυτες τις ενεργειες:
                } else {
                    balldirX = -balldirX; //αλλαζει η πορεια της μπαλας
                    if (balldirX > 0) {
                        balldirX = balldirX + 1;
                        
                    } else if (balldirX < 0) {
                        balldirX = balldirX - 1; // και προσθετει +1 στην κατευθυνση της μπαλας χ ωστε να παει λιγο πιο γρηγορα
                    }
                    
                    
                    //ανεβαζω και το y ωστε να μην πηγαινει η μπαλα σε μια ευθεια και ειναι πολυ ευκολο
                    if (balldirY >= 0 && balldirY < 3) {
                        balldirY = balldirY + 1;
                    } else if (balldirY < 0 && balldirY > -3) {
                        balldirY = balldirY - 1;
                    }
                    music1();
                    score1 = score1 + 1;
                }
            } else if (new Rectangle (ballposX, ballposY, 30, 30).intersects(new Rectangle(1854, player2posY1, 150, player2posY2))) {
                
                balldirX = -balldirX;
                
                if (score1 >= 25) {
                    
                    if (balldirX > 0) {
                        balldirX = balldirX - 1;
                    } else if (balldirX < 0) {
                        balldirX = balldirX + 1;
                    }
                    
                    if (balldirY >= 0 && balldirY < 8) {
                        balldirY = balldirY + 2;
                    } else if (balldirY < 0 && balldirY > -8) {
                        balldirY = balldirY - 2;
                    }
                    
                    } else if (score1 < 25) {
                    if (balldirX > 0) {
                        balldirX = balldirX + 1;
                    } else if (balldirX < 0) {
                        balldirX = balldirX - 1;
                    }

                    if (balldirY >= 0 && balldirY < 3) {
                        balldirY = balldirY + 1;
                    } else if (balldirY < 0 && balldirY > -3) {
                        balldirY = balldirY - 1;
                    }
                }
                music1();
            }
        
            ballposX += balldirX; 
            ballposY += balldirY;
            // προσθετει την κατευθυνση της μπαλας (η οποία είναι τυχαια απο 0 
            //εως 5) στην θεση της μπαλας. Π.Χ. Αν η κατευθηνση της μπαλας ειναι
            // 3, 2 και η θεση της ειναι 500, 600, τότε η μπάλα απο τα pixels
            // 500, 600 θα μεταφερθει στα pixels 503, 602 (επειδη τα variables:
            //ballposX, ballposY χρησιμοποιουνται στην δημιουργια του Oval) 
            // (και θα φαινεται σαν να μετακινειται)
            
            if (ballposY < 0) {
                balldirY = -balldirY;// αν η μπαλα φτασει τα πανω pixels της οθονης, αλλαζει την κατευθυνση της μπαλας ωστε να παει προς τα κατω.
            }
            if (ballposY > 1025) {
                balldirY = -balldirY; // αν η μπαλα φτασει τα κατω pixels της οθονης, αλλαζει την κατευθυνση της μπαλας ωστε να παει προς τα πανω.
            }
            repaint(); //ανανεωνει τα γραφικα... Βαζε το παντα μετα την μετακινηση ενος αντικειμενου, οπως εδω
        }
    }
    public void moveUp() {
        play = true;
        player1posY1 -= 12;// μεταφερει το paddle του πρωτου παικτη 30 pixels πιο πανω
    }
    public void moveDown() {
        play = true;
        player1posY1 += 12; // μεταφερει το paddle του πρωτου παικτη 30 pixels πιο κατω
    }
    
    public void music1() {
        try {
            URL  soundURL = getClass().getClassLoader().getResource("metalicHit.wav");
            Line.Info linfo = new Line.Info (Clip.class);
            Line line = AudioSystem.getLine(linfo);
            Clip2 = (Clip) line;
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
            Clip2.open(ais);
            Clip2.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void mouseClicked(MouseEvent me) {}
    
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX(); // συντεταγμενη Χ του ποντικιου 
        int mouseY = e.getY(); // συντεταγμενη Y του ποντικιου
        
        if (game) {
            //back
            if (mouseX >= 841 && mouseX <= 908 && mouseY >= 46 && mouseY <= 93) {
                System.exit(0);
            //pause
            } else if (mouseX >= 937 && mouseX <= 996 && mouseY >= 43 && mouseY <= 98) {
                if (!pause) {
                    previousBalldirX = balldirX;
                    previousBalldirY = balldirY;   
                } else {
            balldirX = previousBalldirX;
            balldirY = previousBalldirY;
                }
                pause = !pause;
                
            //restart
            } else if (mouseX >=1010 && mouseX <= 1063 && mouseY >= 37 && mouseY <= 98) {
                play = true;
                    
                player1posY1 = 464;
                player1posY2 = 130;
                
                player2posY1 = 0;
                player2posY2 = 1079;
                
                Random rand3 = new Random();
                Random rand4 = new Random();
                balldirX = rand3.nextInt(2 + 5) - 5;
                balldirY = rand4.nextInt(2 + 5) - 5;
                
                if (balldirX == 0 || balldirY == 0) {
                balldirX = -3;
                balldirY = -2;
                }
                
                ballposX = 960;
                ballposY = 517;
                
                score1 = 0;
                score2 = 0;
                repaint();
            }
        }
        
    }
    
    
    
    public void mouseReleased(MouseEvent me) {}

    
    public void mouseEntered(MouseEvent me) {
    }

    
    public void mouseExited(MouseEvent me) {}
}