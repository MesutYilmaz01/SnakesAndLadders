/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaaplettest;

import java.awt.Font;
import message.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javaaplettest.Client.sInput;
import javaaplettest.Main;
import static javaaplettest.Main.coordinatesArray;
import static javaaplettest.Main.t1;
import static javaaplettest.Main.t2;
import static javaaplettest.Main.whoIsTurn;

/**
 *
 * @author INSECT
 */
// serverdan gelecek mesajları dinleyen thread
class Listen extends Thread {

    public void run() {
        //soket bağlı olduğu sürece dön
        while (Client.socket.isConnected()) {
            try {
                //mesaj gelmesini bloking olarak dinyelen komut
                Message received = (Message) (sInput.readObject());
                //mesaj gelirse bu satıra geçer
                //mesaj tipine göre yapılacak işlemi ayır.
                switch (received.type) {
                    case Name:
                        break;
                    case RivalConnected:
                        String name = received.content.toString();
                        System.out.println("Eşleşti");
                        break;
                    case Text:
                        Main.player= received.playerNumber;
                        break;
                    case Locations:
                        if (Main.player % 2 == 0)) {
							//eğer gelen oyuncu ilk oyuncuysa ikinci gelenden aldıgı mesajı kendi ekranında uygulayacak
							//buradaki algoritma maindeki algoritmanın aynısı. aynısı ikinci oyuncu içinde birinciden geleni uygulamaktadır.
							//gelen mesajda eski değer ve üretilen sayı burada yeniden çizilmeyi sağlıyor.
                            int flag = 0;
                            if (t2._turn == true) {
                                t1._turn = true;
                                Main.btn1.setEnabled(true);
                                int oldValue = received.array[0];
                                int randomNumber = received.array[1];
                                t2.location = t2.location + randomNumber;
                                Main.btn2.setText(String.valueOf(randomNumber));
                                if (t2.location == 99) {
                                    flag = 1;
                                    t2._turn = false;
                                    Main.btn2.setEnabled(false);
                                    t1._turn = false;
                                    Main.btn1.setEnabled(false);
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
                                    Main.again.setVisible(true);
                                } else {
                                    whoIsTurn.setText("<");
                                }

                                t2._turn = false;
                                Main.btn2.setEnabled(false);
                            }
                        }
                        if (Main.player % 2 == 1) {
                            int flag = 0;
                            if (t1._turn == true) {
                                t2._turn = true;
                                Main.btn2.setEnabled(true);
                                int oldValue = received.array[0];
                                int randomNumber = received.array[1];
                                t1.location = t1.location + randomNumber;
                                Main.btn1.setText(String.valueOf(randomNumber));
                                if (t1.location == 99) {
                                    flag = 1;
                                    t2._turn = false;
                                    Main.btn2.setEnabled(false);
                                    t1._turn = false;
                                    Main.btn1.setEnabled(false);
                                }
                                if (t1.location >= 100) {
                                    t1.location = oldValue;
                                } else {
                                    t1.setBackground(coordinatesArray[t1.location]._c);
                                    t1.setBounds(coordinatesArray[t1.location]._x + 15, coordinatesArray[t1.location]._y + 15, 35, 35);
                                    if (coordinatesArray[t1.location].isSnakeOrLeader) {
                                        try {
                                            TimeUnit.MILLISECONDS.sleep(1000);
                                            t1.setBounds(coordinatesArray[coordinatesArray[t1.location].nextValue]._x + 15, coordinatesArray[coordinatesArray[t1.location].nextValue]._y + 15, 35, 35);
                                            t1.location = coordinatesArray[t1.location].nextValue;
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }

                                }
                                if (flag == 1) {
                                    flag = 0;
                                    whoIsTurn.setBounds(250, 580, 250, 100);
                                    whoIsTurn.setFont(new Font("Serif", Font.BOLD, 20));
                                    whoIsTurn.setText("Player1 Has Won the Game!");
                                    Main.again.setVisible(true);
                                } else {
                                    whoIsTurn.setText(">");
                                }
                                t1._turn = false;
                                Main.btn1.setEnabled(false);
                            }
                        }
                        break;
                }

            } catch (IOException ex) {

                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                //Client.Stop();
                break;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                //Client.Stop();
                break;
            }
        }

    }
}

public class Client {

    //her clientın bir soketi olmalı
    public static Socket socket;

    //verileri almak için gerekli nesne
    public static ObjectInputStream sInput;
    //verileri göndermek için gerekli nesne
    public static ObjectOutputStream sOutput;
    //serverı dinleme thredi 
    public static Listen listenMe;

    public static void Start(String ip, int port) {
        try {
            // Client Soket nesnesi
            Client.socket = new Socket(ip, port);
            Client.Display("Servera bağlandı");
            // input stream
            Client.sInput = new ObjectInputStream(Client.socket.getInputStream());
            // output stream
            Client.sOutput = new ObjectOutputStream(Client.socket.getOutputStream());
            Client.listenMe = new Listen();
            Client.listenMe.start();

            //ilk mesaj olarak isim gönderiyorum
            Message msg = new Message(Message.Message_Type.Name);
            msg.content = "Bağladnım";
            Client.Send(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //client durdurma fonksiyonu
    public static void Stop() {
        try {
            if (Client.socket != null) {
                Client.listenMe.stop();
                Client.socket.close();
                Client.sOutput.flush();
                Client.sOutput.close();

                Client.sInput.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void Display(String msg) {

        System.out.println(msg);

    }

    //mesaj gönderme fonksiyonu
    public static void Send(Message msg) {
        try {
            Client.sOutput.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
