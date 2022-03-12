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
import java.util.Random;
import javax.swing.*;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;

/**
 *
 * @author user
 */
public class Pong extends JPanel implements KeyListener, ActionListener, MouseListener {
    public static void main(String[] args )throws Throwable {
        JFrame frame = new JFrame();
        frame.setBounds(0, 0, 1919, 1079);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("PONG");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        Pong pong = new Pong();
        frame.add(pong);
        frame.setVisible(true);
    }

    boolean play = false; // οταν ειναι true σημαινει οτι οι παικτες παιζουν ακομη και η μπαλα κυλαει
    int score1;
    int score2;
    Timer timer;
    int delay = 8;
    Timer timer2 = new Timer(3000, this);
    String color;
    boolean game = false;
    boolean pause = false;
    Clip Clip1;
    Clip Clip2;
    boolean music = true;
    long clipTimePosition;
    int limit = 10;
    boolean UP1, DOWN1, UP2, DOWN2;
    
    int previousBalldirX;
    int previousBalldirY;
    
    // η θεση της μπαλας
    int ballposX = 960;
    int ballposY = 517;
    
    // η κατευθυνση της μπαλας
    int balldirX; 
    int balldirY;
    
    // η θεση του παικτη 1
    int player1posY1 = 464;
    int player1posY2 = 130;
    
    // η θεση του παικτη 2
    int player2posY1 = 464;
    int player2posY2 = 130;
    
    public Pong() {
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
        music();
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
        g.fillRect(1854, player2posY1, 25, player2posY2);
        
        //player Movement
        if (UP1 == true) {
            if (player1posY1 <= 0) { // αν το pad του 1ου παικτη εχει φτασει τα τερμα πανω pixles
                player1posY1 = 0; // πηγαινε τον στο τερμα των πανω pixels
            } else  {
                moveUp1();
            }
        }
        if (DOWN1 == true) {
            if (player1posY1 >= 926) {
                player1posY1 = 926;
            } else {
                moveDown1();
            }
        }
        if (UP2 == true) {
            if (player2posY1 <= 0) {
                player2posY1 = 0;
            } else {
                moveUp2();
            }
        }
        if (DOWN2 == true) {
            if (player2posY1 >= 926) {
                player2posY1 = 926;
            } else {
                moveDown2();
            }
        }
        
        //Blue Line
        g.setColor(Color.blue);
        g.fillRect(955, 1, 5, 1050);
        
        //Red Line
        g.setColor(Color.red);
        g.fillRect(961, 1, 5, 1050);
        
        //menu
        if (!game) {
            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 100));
            g.drawString("PLAY PVP", 720, 400);
            
            g.setFont(new Font("serif", Font.BOLD, 90));
            g.drawString("TRAINING", 730, 550);
            
            g.setFont(new Font("serif", Font.BOLD, 100));
            g.drawString("QUIT", 820, 700);
            //οριο
            g.setColor(Color.white);
            g.drawRect(15, 42, 350, 150);
            
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("ΟΡΙΟ:", 110, 100);
            
            //3
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("3", 30, 170);
            //5
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("5", 80, 170);
            //10
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("10", 130, 170);
            //15
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("15", 210, 170);
            //20
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("20", 290, 170);
        }
        
        if (!game) {
            if (music) {
                //music on
                ImageIcon image10 = new ImageIcon(getClass().getClassLoader().getResource("audioON.jpg"));
                image10.paintIcon(this, g, 10, 950);
                repaint();
            } else if (!music) {
                //music off
                ImageIcon image6 = new ImageIcon(getClass().getClassLoader().getResource("audioOFF.png"));
                image6.paintIcon(this, g, 35, 950);
                repaint();
            }
        }
        
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
            image2.paintIcon(this, g, 23, 25);
        
            if (pause) {
                //pause
                ImageIcon image1 = new ImageIcon(getClass().getClassLoader().getResource("resume.png"));
                image1.paintIcon(this, g, 123, 35);
                
                balldirX = 0;
                balldirY = 0;
                
                //stopped
                ImageIcon image4 = new ImageIcon(getClass().getClassLoader().getResource("stopped.png"));
                image4.paintIcon(this, g, 650, 200);
                
            } else if (!pause) {
                //resume
                ImageIcon image3 = new ImageIcon(getClass().getClassLoader().getResource("pause.png"));
                image3.paintIcon(this, g, 103, 25);
                }
        
            //restart
            ImageIcon image4 = new ImageIcon(getClass().getClassLoader().getResource("restart.png"));
            image4.paintIcon(this, g, 183, 25);
        }
        
        if (ballposX >= 1890 && score1 < limit) { // οταν η μπαλα ξεπερασει το τερμα των δεξιων pixels στον αξονα χ
            play = false; //σταματαει το παιχνιδι
            
            score1 = score1 + 1; //αυξανει το score κατα 1
            
            g.setColor(Color.blue);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("ΣΚΟΡΑΡΕ Ο ΜΠΛΕ!   " + score1 + " - " + score2, 750, 420);
            
            g.setFont(new Font("serif",  Font.BOLD, 30));
            g.drawString("ΠΑΤΗΣΤΕ ENTER ΓΙΑ ΝΑ ΣΥΝΕΧΙΣΕΤΕ", 645, 500);
            //νικη 1ου παικτη
            if (ballposX >= 1890 && score1 == limit) {
                g.setColor(Color.black);
                g.fillRect(640, 380, 600, 300);
                g.setColor(Color.blue);
                g.setFont(new Font("serif", Font.BOLD, 30));
                g.drawString("ΣΥΓΧΑΡΗΤΗΡΙΑ! ΝΙΚΗΣΕ Ο ΜΠΛΕ!  " + score1 + " - " + score2, 650, 520);
                play = false;
            }
        }
        if (ballposX <= 0 && score2 < limit) { // οταν η μπαλα ξεπερασει το τερμα των αριστερων pixels στον αξονα χ
            play = false; //σταματαει το παιχνιδι
            
            score2 = score2 + 1; //αυξανει το score κατα 1
            
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("ΣΚΟΡΑΡΕ Ο ΚΟΚΚΙΝΟΣ!   " + score1 + " - " + score2 , 750, 420);
            
            g.setFont(new Font("serif",  Font.BOLD, 30));
            g.drawString("ΠΑΤΗΣΤΕ ENTER ΓΙΑ ΝΑ ΣΥΝΕΧΙΣΕΤΕ", 645, 500);
            
            //νικη 2ου παικτη
            if (ballposX <= 0 && score2 == limit) {
                g.setColor(Color.black);
                g.fillRect(640, 380, 600, 300);
                g.setColor(Color.red);
                g.setFont(new Font("serif", Font.BOLD, 30));
                g.drawString("ΣΥΓΧΑΡΗΤΗΡΙΑ! ΝΙΚΗΣΕ Ο ΚΟΚΚΙΝΟΣ!  " + score1 + " - " + score2, 650, 520);
                play = false;
            }
        }
        
        //score1
        g.setColor(Color.blue);
        g.setFont(new Font("serif", Font.BOLD, 100));
        g.drawString("" + score1, 870, 100);
        
        //score2
        g.setColor(Color.red);
        g.setFont(new Font("serif", Font.BOLD, 100));
        g.drawString("" + score2, 999, 100);
        
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
                    
                player1posY1 = 464;
                player1posY2 = 130;
                
                player2posY1 = 464;
                player2posY2 = 130;
                
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
                
                if (score1 == limit || score2 == limit) {
                    game = false;
                    
                    player1posY1 = 464;
                    player1posY2 = 130;

                    player2posY1 = 464;
                    player2posY2 = 130;

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
        
        //Player Movement
        if (e.getKeyCode() == KeyEvent.VK_W) UP1 = true;
        if(e.getKeyCode() == KeyEvent.VK_S) DOWN1 = true;
        if(e.getKeyCode() == KeyEvent.VK_UP) UP2 = true;
        if(e.getKeyCode() == KeyEvent.VK_DOWN) DOWN2 = true;
    }
    
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) UP1 = false;
        if(e.getKeyCode() == KeyEvent.VK_S) DOWN1 = false;
        if(e.getKeyCode() == KeyEvent.VK_UP) UP2 = false;
        if(e.getKeyCode() == KeyEvent.VK_DOWN) DOWN2 = false;
    }

    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play && game) {
            if (new Rectangle(ballposX, ballposY, 30, 30).intersects(new Rectangle(50, player1posY1, 25, player1posY2))) { //αν η μπαλα ακουμπησει το πρωτο paddle,
                
                
               if (new Rectangle(ballposX, ballposY, 30, 30).intersects(new Rectangle(50, player1posY1 - 10, 15, 5))) {
                    balldirY = -balldirY;
                    balldirX = -2;
                    ballposY = ballposY - 50;
                    
                } else if (new Rectangle(ballposX, ballposY, 30, 30).intersects(new Rectangle(50, player1posY2 + 10, 25, - 5))) {
                    balldirY = -balldirY;
                    balldirX = -2;
                    ballposY = ballposY + 50;
                } else {
                    
                    balldirX = -balldirX; //αλλαζει η πορεια της μπαλας
                    
                    if (balldirX > 0) {
                        balldirX = balldirX + 1;
                    } else if (balldirX < 0) {
                        balldirX = balldirX - 1; // και προσθετει +1 στην κατευθυνση της μπαλας χ ωστε να παει λιγο πιο γρηγορα
                    }
                    
                    if (balldirY >= 0) {
                        balldirY = balldirY + 1;
                    } else if (balldirY < 0) {
                        balldirY = balldirY - 1;
                    }
                    music1();
                }
            }
            
        //collision
        if (new Rectangle (ballposX, ballposY, 30, 30).intersects(new Rectangle(1854, player2posY1, 25, player2posY2))) {
                
            if (new Rectangle(ballposX, ballposY, 30, 30).intersects(new Rectangle(1854, player2posY1 - 10, 25, 5))) {
                    balldirY = -balldirY;
                    balldirX = 2;
                    ballposY = ballposY - 50;
                    
                } else if (new Rectangle(ballposX, ballposY).intersects(new Rectangle(1854, player2posY1, 25, player2posY2))) {
                    balldirY = -balldirY;
                    balldirX = 2;
                    ballposY = ballposY + 50;
                    
                } else {
                    
                    balldirX = -balldirX;
                    
                    if (balldirX > 0) {
                        balldirX = balldirX + 1;
                    } else if (balldirX < 0) {
                        balldirX = balldirX - 1;
                    }
                    
                    if (balldirY >= 0) {
                        balldirY = balldirY + 1;
                    } else if (balldirY < 0) {
                        balldirY = balldirY - 1;
                    }
                    music1();
                }
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
                music1();
            }
            if (ballposY > 1025) {
                balldirY = -balldirY; // αν η μπαλα φτασει τα κατω pixels της οθονης, αλλαζει την κατευθυνση της μπαλας ωστε να παει προς τα πανω.
                music1();
            }
            repaint(); //ανανεωνει τα γραφικα... Βαζε το παντα μετα την μετακινηση ενος αντικειμενου, οπως εδω
        }
    }
    public void moveUp1() {
        play = true;
        player1posY1 -= 11; // μεταφερει το paddle του πρωτου παικτη 25 pixels πιο πανω
    }
    public void moveDown1() {
        play = true;
        player1posY1 += 11; // μεταφερει το paddle του πρωτου παικτη 25 pixels πιο κατω
    }
    
    public void moveUp2() {
        play = true;
        player2posY1 -= 11; // μεταφερει το paddle του πρωτου παικτη 25 pixels πιο πανω
    }
    public void moveDown2() {
        play = true;
        player2posY1 += 11; // μεταφερει το paddle του πρωτου παικτη 25 pixels πιο κατω
    }
    
    public void music() {
        try {
            
            URL  soundURL = getClass().getClassLoader().getResource("song.wav");
            Line.Info linfo = new Line.Info (Clip.class);
            Line line = AudioSystem.getLine(linfo);
            Clip1 = (Clip) line;
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
            Clip1.open(ais);
            Clip1.start();
            Clip1.loop(Clip.LOOP_CONTINUOUSLY);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        
        if (!game) {
        if (mouseX >= 720 && mouseX <= 1200 && mouseY >= 320 && mouseY <= 410) {
            game = true;
            play = true;
        } else if (mouseX >= 570 && mouseX <= 1320 && mouseY >= 480 && mouseY <= 560) {
            pongTraining training = new pongTraining();
            training.run();
        } else if (mouseX >= 810 && mouseX <= 1090 && mouseY >= 620 && mouseY <= 725) {
            System.exit(0);
        }
        
        //μουσικη
        if (mouseX >=7 && mouseX <= 90 && mouseY >= 946 && mouseY <= 1020) {
                music = !music;
                if (music == false) {
                    clipTimePosition = Clip1.getMicrosecondPosition();
                    Clip1.stop();
                }
                if (music == true) {

                    Clip1.setMicrosecondPosition(clipTimePosition);
                    Clip1.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }
        
        //οριο
        //3
        if (mouseX >= 28 && mouseX <= 61 && mouseY >= 127 && mouseY <= 174) {
            limit = 3;
        //5
        } else if (mouseX >= 75 && mouseX <= 115 && mouseY >= 127 && mouseY <= 174) {
            limit = 5;
        //10
        } else if (mouseX >= 127 && mouseX <= 191 && mouseY >= 127 && mouseY <= 174) {
            limit = 10;
        //15
        } else if (mouseX >= 205 && mouseX <= 268 && mouseY >= 127 && mouseY <= 174) {
            limit = 15;
        //20
        } else if (mouseX >= 281 && mouseX <= 350 && mouseY >= 127 && mouseY <= 174) {
            limit = 20;
        }
        }
        
        if (game) {
            //back
            if (mouseX >= 22 && mouseX <= 76 && mouseY >= 35 && mouseY <= 85) {
                game = false;
                play = false;
                repaint();
            //pause
            } else if (mouseX >=116 && mouseX <= 173 && mouseY >= 35 && mouseY <= 86) {
                if (!pause) {
                    previousBalldirX = balldirX;
                    previousBalldirY = balldirY;   
                } else {
            balldirX = previousBalldirX;
            balldirY = previousBalldirY;
                }
                pause = !pause;
            
            //restart
            } else if (mouseX >=191 && mouseX <= 244 && mouseY >= 26 && mouseY <= 88) {
                play = true;
                    
                player1posY1 = 464;
                player1posY2 = 130;
                
                player2posY1 = 464;
                player2posY2 = 130;
                
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
    
    public void mouseEntered(MouseEvent me) {}
    
    public void mouseExited(MouseEvent me) {}
}