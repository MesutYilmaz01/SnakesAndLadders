/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaaplettest;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author mesut
 */
public class Tile extends JPanel{

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(700,700);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int number = 0;
        int x = 6, y = 546;
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                x = 6;
                for (int j = 0; j < 10; j++) {
                    number++;
                    if (j % 2 == 0) {
                        g.setColor(Color.red);
                    } else {
                        g.setColor(Color.white);
                    }
                    g.fillRect(x, y, 70, 60);
                    x += 70;
                }
            } else {
                x = 636;
                for (int j = 0; j < 10; j++) {
                    number++;
                    if (j % 2 == 0) {
                        g.setColor(Color.red);
                    } else {
                        g.setColor(Color.white);
                    }
                    g.fillRect(x, y, 70, 60);
                    x -= 70;
                }
            }
            y -= 60;
        }
    }

}
