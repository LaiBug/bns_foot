package org.bns.pojo;

import javax.swing.*;

public class PickPOJO {
    private JLabel jCol;
    Integer R, G, B;
    Integer X, Y;
    private JLabel xy;
    private JButton button;

    private int flag;//1需要展示RGB,2不需要
    public PickPOJO(String name) {
        this.button = new JButton(name);
        this.jCol = new JLabel("■■■■■■");
        this.xy = new JLabel("(0,0)");
        this.R=255;
        this.G=255;
        this.B=255;
        this.X=0;
        this.Y=0;
    }
    public PickPOJO(String name,int flag) {
        this.button = new JButton(name);
        this.jCol = new JLabel("■■■■■■");
        this.xy = new JLabel("(0,0)");
        this.R=255;
        this.G=255;
        this.B=255;
        this.X=0;
        this.Y=0;
        this.flag=flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
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
