package org.bns.pojo;

import javax.swing.*;

public class PickPOJO {
    private JLabel jCol;
    Integer R, G, B;
    Integer X, Y;
    private JLabel xy;
    private JButton button;

    public PickPOJO(String name) {
        this.button = new JButton(name);
        this.jCol = new JLabel("■■■■■■");
        this.xy = new JLabel("(0,0)");
    }

    public JLabel getjCol() {
        return jCol;
    }

    public void setjCol(JLabel jCol) {
        this.jCol = jCol;
    }

    public Integer getR() {
        return R;
    }

    public void setR(Integer r) {
        R = r;
    }

    public Integer getG() {
        return G;
    }

    public void setG(Integer g) {
        G = g;
    }

    public Integer getB() {
        return B;
    }

    public void setB(Integer b) {
        B = b;
    }

    public Integer getX() {
        return X;
    }

    public void setX(Integer x) {
        X = x;
    }

    public Integer getY() {
        return Y;
    }

    public void setY(Integer y) {
        Y = y;
    }

    public JLabel getXy() {
        return xy;
    }

    public void setXy(JLabel xy) {
        this.xy = xy;
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }
}
