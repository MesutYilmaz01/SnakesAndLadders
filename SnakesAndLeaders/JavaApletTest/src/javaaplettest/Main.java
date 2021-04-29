/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaaplettest;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import message.Message;

/**
 *
 * @author mesut
 */
public class Main {

    public static Main ThisGame;
    public static int player = -1;	//oyuncuya atanacak player numarası
    //karşı tarafın seçimi seçim -1 deyse seçilmemiş
    public int RivalSelection = -1;
    //benim seçimim seçim -1 deyse seçilmemiş
    public int myselection = -1;
	//karelerin tutulduğu array ve diğer comopnentler
    public static Coordinate[] coordinatesArray = new Coordinate[100];	
    final static Tile_Object t1 = new Tile_Object(Color.GREEN, true);
    final static Tile_Object t2 = new Tile_Object(Color.BLUE, false);
    final static JButton btn1 = new JButton("READY");
    final static JButton btn2 = new JButton("READY");
    final static JButton again = new JButton("Play Again?");
    final static JButton connect = new JButton("Sunucuya Bağlan");
    final static JLabel player1 = new JLabel("PLAYER1");
    final static JLabel player2 = new JLabel("PLAYER2");
    final static JLabel whoIsTurn = new JLabel("");

    public Main() {
        ThisGame = this;
    }

    public static void main(String[] args) {
        getCoordinates();	//karelerin koordinatlaı ayarlanıyor
		// oyun çerçevesi olusturuluyor
        JFrame obj = new JFrame();
        Tile tile = new Tile();
        obj.setLayout(null);
        obj.setBounds(250, 10, 705, 740);
        obj.setTitle("Snakes And Ladders");

		//Oyundaki kareler tile olarak panele ekleniyor.
        JPanel panel1 = new JPanel();
        panel1.setBounds(0, 0, 700, 700);
		//Alt kısımdaki panel
        JPanel panel2 = new JPanel();
        panel2.setBounds(0, 610, 700, 99);
        panel2.setBackground(Color.decode("#6aab7b"));

        panel1.add(tile);
		//oyuncuları temsil eden kareler ilk olarak birinci kareye yerleştiriliyor.
        t1.setBackground(coordinatesArray[0]._c);
        t1.setBounds(coordinatesArray[0]._x + 15, coordinatesArray[0]._y + 20, 35, 35);
		//ardından çerçeveye ekleniyor.
        obj.add(t1);

        t2.setBackground(coordinatesArray[0]._c);
        t2.setBounds(coordinatesArray[0]._x + 15, coordinatesArray[0]._y + 20, 35, 35);
        obj.add(t2);
		//oyuncunun tıklayacağı buton ayarlanıp çerçeveye ekleniyor
        player1.setBounds(75, 610, 100, 50);
        obj.add(player1);
        btn1.setBounds(50, 650, 100, 50);
		//başlangıçta birinci oyuncudan başlayacağı için sıra ona verilip ikinci buton kapatılıyor.
        if (t1._turn == true) {
            btn2.setEnabled(false);
        }
        obj.add(btn1);
        btn1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int flag = 0;	//oyunun bitip bitmediğini kontrol için tutulan flag
                if (t1._turn == true) {	//birinci oyunncu butona tıkladığı için ikinci buton açılıyor.
                    t2._turn = true;
                    btn2.setEnabled(true);
                    int oldValue = t1.location;	//playerin ilk  konumu snake yada ladder gelme ihtimalien karşın korunuyor
                    Random rnd = new Random();
                    int randomNumber = rnd.nextInt(6 - 1 + 1) + 1;	//zar atılıyor
                    t1.location = t1.location + randomNumber;	//lokasyona yeni gelen değer eklenyot
                    btn1.setText(String.valueOf(randomNumber));	//butonda attığu zar yazılıyor
                    if (t1.location == 99) {	//eğer son kareye geldiyse kazandı demektir. flag 1 yapılıyor
                        flag = 1;				//butonları açma kapama gibi işlemler
                        t2._turn = false;
                        btn2.setEnabled(false);
                        t1._turn = false;
                        btn1.setEnabled(false);
                    }
                    if (t1.location >= 100) {	// atılan zar 99dan büyükse eski değerinde kalması sağlanıyor.
                        t1.location = oldValue;
                    } else {
                        t1.setBackground(coordinatesArray[t1.location]._c);	//sınır aşma durumu yoksa kordinatı değiştiriliyor.
                        t1.setBounds(coordinatesArray[t1.location]._x + 15, coordinatesArray[t1.location]._y + 15, 35, 35);
                        if (coordinatesArray[t1.location].isSnakeOrLeader) {	//geldiği koordinatta snake veya ladder varsa ordaki nextvalue konumuna gönderiliyor.
                            try {
                                TimeUnit.MILLISECONDS.sleep(1000);
                                t1.setBounds(coordinatesArray[coordinatesArray[t1.location].nextValue]._x + 15, coordinatesArray[coordinatesArray[t1.location].nextValue]._y + 15, 35, 35);
                                t1.location = coordinatesArray[t1.location].nextValue;
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    if (flag == 1) {	//flag 1 ise oyun bittiği için ekrana kimin kazandığı yazılıyor.
                        flag = 0;
                        whoIsTurn.setBounds(250, 580, 250, 100);
                        whoIsTurn.setFont(new Font("Serif", Font.BOLD, 20));
                        whoIsTurn.setText("Player1 Has Won the Game!");
                        again.setVisible(true);	//tekrar oynamak ister misiniz butonu açılıyor
                    } else {
                        whoIsTurn.setText(">");	//sıranın kimde olduğunu gösteren işaret yer değişiyor.
                    }

                    t1._turn = false;		//sıranın geçtiği belirtliyor ve kendi butonu tıklanamaz hale geliyor.
                    btn1.setEnabled(false);
                    Message msg = new Message(Message.Message_Type.Locations);	//aynı değişikliklerin karşı tarafta da olması için gerekli bilgiler gönderiliyor
                    int[] array = {oldValue, randomNumber};
                    msg.array = array;
                    Client.Send(msg);
					//player 2 içinde aynı işlemler yapıldığı için onlar için tekrardan aynı şeyleri yazmadım.
                }
            }
        });

        player2.setBounds(575, 610, 100, 50);
        obj.add(player2);
        btn2.setBounds(550, 650, 100, 50);
        if (t2._turn == true) {
            btn1.setEnabled(false);
        }
        obj.add(btn2);
        btn2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int flag = 0;
                if (t2._turn == true) {
                    t1._turn = true;
                    btn1.setEnabled(true);
                    int oldValue = t2.location;
                    Random rnd = new Random();
                    int randomNumber = rnd.nextInt(6 - 1 + 1) + 1;
                    t2.location = t2.location + randomNumber;
                    btn2.setText(String.valueOf(randomNumber));
                    if (t2.location == 99) {
                        flag = 1;
                        t2._turn = false;
                        btn2.setEnabled(false);
                        t1._turn = false;
                        btn1.setEnabled(false);
                    }
                    if (t2.location >= 100) {
                        t2.location = oldValue;
                    } else {
                        t2.setBackground(coordinatesArray[t2.location]._c);
                        t2.setBounds(coordinatesArray[t2.location]._x + 15, coordinatesArray[t2.location]._y + 15, 35, 35);
                        if (coordinatesArray[t2.location].isSnakeOrLeader) {
                            try {
                                TimeUnit.MILLISECONDS.sleep(1000);
                                t2.setBounds(coordinatesArray[coordinatesArray[t2.location].nextValue]._x + 15, coordinatesArray[coordinatesArray[t2.location].nextValue]._y + 15, 35, 35);
                                t2.location = coordinatesArray[t2.location].nextValue;
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    if (flag == 1) {
                        flag = 0;
                        whoIsTurn.setBounds(250, 580, 250, 100);
                        whoIsTurn.setFont(new Font("Serif", Font.BOLD, 20));
                        whoIsTurn.setText("Player2 Has Won the Game!");
                        again.setVisible(true);
                    } else {
                        whoIsTurn.setText("<");
                    }

                    t2._turn = false;
                    btn2.setEnabled(false);
                    Message msg = new Message(Message.Message_Type.Locations);
                    int[] array = {oldValue, randomNumber};
                    msg.array = array;
                    Client.Send(msg);
                }
            }
        });

        whoIsTurn.setBounds(350, 580, 100, 100);	//kimin sırasının geldiğini gösteren işaret burda başlıyor ve default olarak player1 gösteriliyor.
        whoIsTurn.setFont(new Font("Serif", Font.BOLD, 40));
        whoIsTurn.setText("<");
        obj.add(whoIsTurn);

        try {
			//Arka plandaki resim burada ekleniyor.
            BufferedImage mypic = ImageIO.read(new File("C:\\Users\\mesut\\Documents\\NetBeansProjects\\JavaApletTest\\src\\javaaplettest\\background.jpg"));
            JLabel picLabel = new JLabel(new ImageIcon(mypic));
            picLabel.setBounds(0, -40, 700, 700);
            obj.add(picLabel);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

		//tekrar oynamakla ilgili buton burada ekleniyor ve başlangıçta gizli. oyun sonunda ortaya çıkıyor.
        again.setBounds(300, 650, 100, 50);
        again.setVisible(false);
        obj.add(again);
        again.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //oyun tekrar oynanmaya hazır hale getirliyor. player tile larının koordnatlarının ayarlanması, sıralar vs.
                again.setVisible(false);
                t1.setBounds(coordinatesArray[0]._x + 15, coordinatesArray[0]._y + 15, 35, 35);
                t1.location = 0;
                t2.setBounds(coordinatesArray[0]._x + 15, coordinatesArray[0]._y + 15, 35, 35);
                t2.location = 0;
                player1.setText("READY");
                player2.setText("READY");
                t1._turn = true;
                btn1.setEnabled(true);
                t2._turn = false;
                btn2.setEnabled(false);
                whoIsTurn.setBounds(350, 580, 100, 100);
                whoIsTurn.setFont(new Font("Serif", Font.BOLD, 40));
                whoIsTurn.setText("<");
            }
        });

        connect.setBounds(300, 650, 100, 50);	//sunucuya bağlanma butonu burda ayarlanıyor
        connect.setVisible(true);
        obj.add(connect);
        connect.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
				//sunucuya bağlanan oyuncu sadece kendi butonunu görüyor.
                Client.Start("127.0.0.1", 2000);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Player numaram " + player);
                if (player % 2 == 0) {	//sunucuya bağlanan ilk oyuncuysa
                    btn1.setVisible(true);
                    btn2.setVisible(false);
                } else if (player % 2 == 1) {	//ikinc oyuncuysa
                    btn2.setVisible(true);
                    btn1.setVisible(false);
                } else {
                    btn1.setVisible(false);
                    btn2.setVisible(false);
                }
                connect.setVisible(false);
            }
        });
		//tüm paneller çerçeveye ekleniyor
        obj.add(panel2);
        obj.add(panel1);

        obj.setResizable(false);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.setVisible(true);
    }

    public static void getCoordinates() {
		//burada belli bir algoritmaya göre oyun tahtasındaki kareler ayarlanıyor.
        int number = 0;
        int x = 6, y = 546;
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                x = 6;
                for (int j = 0; j < 10; j++) {
                    number++;
                    Coordinate c;
                    if (j % 2 == 0) {
                        c = new Coordinate(x, y, Color.yellow);
                    } else {
                        c = new Coordinate(x, y, Color.white);
                    }
                    coordinatesArray[number - 1] = c;
                    x += 70;
                }
            } else {
                x = 636;
                for (int j = 0; j < 10; j++) {
                    number++;
                    Coordinate c;
                    if (j % 2 == 0) {
                        c = new Coordinate(x, y, Color.yellow);
                    } else {
                        c = new Coordinate(x, y, Color.white);
                    }
                    coordinatesArray[number - 1] = c;
                    x -= 70;
                }
            }
            y -= 60;
        }
		//snakeler ve ladderların kkordinatları ve buraya geldikten sonra gidilecek koordinatlar.
        coordinatesArray[2].isSnakeOrLeader = true;
        coordinatesArray[2].nextValue = 38;
        coordinatesArray[9].isSnakeOrLeader = true;
        coordinatesArray[9].nextValue = 11;
        coordinatesArray[15].isSnakeOrLeader = true;
        coordinatesArray[15].nextValue = 12;
        coordinatesArray[26].isSnakeOrLeader = true;
        coordinatesArray[26].nextValue = 52;
        coordinatesArray[30].isSnakeOrLeader = true;
        coordinatesArray[30].nextValue = 3;
        coordinatesArray[46].isSnakeOrLeader = true;
        coordinatesArray[46].nextValue = 24;
        coordinatesArray[55].isSnakeOrLeader = true;
        coordinatesArray[55].nextValue = 83;
        coordinatesArray[60].isSnakeOrLeader = true;
        coordinatesArray[60].nextValue = 98;
        coordinatesArray[62].isSnakeOrLeader = true;
        coordinatesArray[62].nextValue = 59;
        coordinatesArray[65].isSnakeOrLeader = true;
        coordinatesArray[65].nextValue = 51;
        coordinatesArray[71].isSnakeOrLeader = true;
        coordinatesArray[71].nextValue = 89;
        coordinatesArray[96].isSnakeOrLeader = true;
        coordinatesArray[96].nextValue = 74;
    }
}
