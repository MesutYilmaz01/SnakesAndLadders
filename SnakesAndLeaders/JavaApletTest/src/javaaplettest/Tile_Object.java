/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaaplettest;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author mesut
 */
public class Tile_Object extends JPanel {

    int location = 0;
    boolean _turn;
    Color _c;
    public Tile_Object(Color c,boolean turn)
    {
        _c = c;
        _turn = turn;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(35, 35);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(_c);
        g.fillRect(0, 0, 35, 35);
    }
}
