/*
 * Created on 2004-7-24
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.view;

import java.io.Serializable;
/**
 * The implementation of jexi.ui.Graphics, this is an adapter between 
 * <b>jexi.ui.Graphics</b> and <b>org.eclipse.swt.graphics.GC</b>.
 * 
 * @author Xuefeng
 */
public final class yGraphics implements Serializable{




    // a reference of org.eclipse.swt.graphics.GC:
    protected final org.eclipse.swt.graphics.GC gc;

    // a font reference:
    private yFont font = null;

    private int current_x;
    private int current_y;

    public yGraphics(org.eclipse.swt.graphics.GC gc) {
        this.gc = gc;
    }

    public void drawImage() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see jexi.ui.Graphics#drawLine(int, int, int, int)
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        gc.drawLine(x1, y1, x2, y2);
    }

    /* (non-Javadoc)
     * @see jexi.ui.Graphics#drawRectangle(int, int)
     */
    public void drawRectangle(int width, int height) {
        gc.drawRectangle(current_x, current_y, width, height);
    }

    /* (non-Javadoc)
     * @see jexi.ui.Graphics#drawRectangle(int, int, int, int)
     */
    public void drawRectangle(int x, int y, int width, int height) {
        gc.drawRectangle(x, y, width, height);
    }

    /* (non-Javadoc)
     * @see jexi.ui.Graphics#drawChar(char)
     */
    public void drawChar(char c) {
        gc.drawString(Character.toString(c), current_x, current_y);
    }

    /* (non-Javadoc)
     * @see jexi.ui.Graphics#drawString(java.lang.String)
     */
    public void drawString(String s) {
        gc.drawString(s, current_x, current_y);
    }

    /* (non-Javadoc)
     * @see jexi.ui.Graphics#fillRect(int, int)
     */
    public void fillRect(int width, int height) {
        org.eclipse.swt.graphics.Color backColor = gc.getBackground();
        gc.setBackground(gc.getForeground());
        gc.fillRectangle(current_x, current_y, width, height);
        gc.setBackground(backColor);
    }

    /* (non-Javadoc)
     * @see jexi.ui.Graphics#fillRect(int, int, int, int)
     */
    public void fillRect(int x, int y, int width, int height) {
        org.eclipse.swt.graphics.Color backColor = gc.getBackground();
        gc.setBackground(gc.getForeground());
        gc.fillRectangle(x, y, width, height);
        gc.setBackground(backColor);
    }

    /* (non-Javadoc)
     * @see jexi.ui.Graphics#moveTo(int, int)
     */
    public void moveTo(int x, int y) {
        this.current_x = x;
        this.current_y = y;
    }

    public int getCurrentX() {
        return this.current_x;
    }

    public int getCurrentY() {
        return this.current_y;
    }

    /**
     * Set the font of the graphics.
     * 
     * @param font The font object.
     */
    public void setFont(yFont font) {
        y.module.Assert.checkNull(font);

        if(this.font==font) return;
        this.font = font;
        gc.setFont(((yFont)font).nativeFont());
    }

    /**
     * Set the fore color of the graphics. 
     * 
     * @param color The fore color.
     */
    public void setForecolor(yColor color) {
        org.eclipse.swt.graphics.Color c = ((yColor)color).nativeColor();
        gc.setForeground(c);
    }

    /**
     * Set the back color of the graphics. 
     * 
     * @param color The back color.
     */
    public void setBackcolor(yColor color) {
        org.eclipse.swt.graphics.Color c = ((yColor)color).nativeColor();
        gc.setBackground(c);
    }

    /**
     * Get the width of the char represented by the Glyph.
     * 
     * @param c The Char.
     * @return Points of the char occupied.
     */
    public int getCharWidth(char c) {
        if(c=='\r')
            return 0;
        return gc.getAdvanceWidth(c);
    }

    /**
     * Get the height of the current font. setFont() should be 
     * called before this call.
     * 
     * @return Points of the char occupied.
     */
    public int getCharHeight() {
        return gc.getFontMetrics().getHeight();
    }

    /**
     * Destroy the graphics and release all resources.
     */
    public void dispose() {
        this.gc.dispose();
    }

}
