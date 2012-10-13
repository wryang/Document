/*
 * Created on 2004-7-31
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.view;

import java.util.*;

/**
 * The implementation of ColorFactory. 
 * 
 * @author Xuefeng
 */
public class yColorFactory{

    //*************************************************************************
    // NOTE: This ColorFactory is implement by SWTColorFactoy, 
    // if you want to use another factoy such as SwingColorFactory, 
    // change this following code:
    //*************************************************************************
    private static yColorFactory instance = new yColorFactory();

    // cache the color:
    private Hashtable<Integer, yColor> colors = new Hashtable<Integer, yColor>();

    // store the Display object:
    private org.eclipse.swt.widgets.Display display = null;
    
    /**
     * Get the singleton instance of the ColorFactory. 
     * 
     * @return The color factory.
     */
    public static yColorFactory instance() {
        return instance;
    }


    /**
     * Create color specified by the predefined constant. 
     * 
     * @param color_rgb Constant such as BLACK, RED, or 0xff00cc.
     * @return The RGB color.
     */
    public yColor createColor(int color_rgb) {
        int r = (color_rgb & 0xff0000) >> 16;
        int g = (color_rgb & 0xff00) >> 8;
        int b = (color_rgb & 0xff);
        return createColor(r, g, b);
    }


    /**
     * Create new color. 
     * 
     * @param r Red, 0-255.
     * @param g Green, 0-255.
     * @param b Blue, 0-255.
     * @return The RGB color.
     */
    public yColor createColor(int r, int g, int b) {
        // first check if it already cached:
        Integer key = yColor.toKey(r, g, b);
        Object o = colors.get(key);
        if(o!=null) {
            yColor color = (yColor)o;
            color.addRef(); // add a reference!
            return color;
        }

        // if not found, we create a new color:
        if(this.display==null) {
            this.display = ((y.view.yFrame)(y.view.Application.instance().getFrame())).getDisplay();
        }
        yColor newColor = new yColor(key, new org.eclipse.swt.graphics.Color(display, r, g, b));
        // put it to cache:
        colors.put(key, newColor);
        return newColor;
    }

    /**
     * Clear all color resources when application terminated. 
     * 
     * @see y.module.ui.ColorFactory#clearAllColors()
     */
    public void clearAllColors() {
        Collection all_colors = new ArrayList( colors.values() );
        Iterator it = all_colors.iterator();
        while(it.hasNext()) {
            yColor color = (yColor)it.next();
            while(color.refCount()>0) {
                System.out.println("WARNING: Some colors are not released.");
                color.dispose();
            }
        }
        // clear hash table:
        colors.clear();
    }

    /**
     * Get the size of the cache. 
     */
    public int size() {
        return colors.size();
    }

    // remove the color:
    protected void remove(yColor c) {
        colors.remove(c.getKey());
    }

    public void debug() {
        System.out.println("\n[ColorFactory: " + colors.size() + " colors]");
        Iterator it = colors.values().iterator();
        while(it.hasNext()) {
            yColor c = (yColor)it.next();
            c.debug();
        }
        System.out.println("-- END ColorFactory --");
    }
}
