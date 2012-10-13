/*
 * Created on 2004-7-27
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.view;

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;


/**
 * The implementation of FontFactory. 
 * 
 * @author Xuefeng
 */
public class yFontFactory{

    // store all fonts that is used now:
    private Hashtable fonts = new Hashtable();

    // store all fonts' name:
    private String[] fontNames = null;

    // we assume that the Display will always available:
    private Display display = null;

    //*************************************************************************
    // the only instance of the FontFactory. In this case, 
    // FontFactory is yFontFactory. You should change the 
    // class name such as "SwingFontFactory" and re-compile 
    // the project if using Swing or some other GUI. 
    //*************************************************************************
    private static yFontFactory instance = new yFontFactory();

    // to prevent the client to create a new instance:
    protected yFontFactory() {}
    
    /**
     * Get the instance of the FontFactory. 
     * 
     * @return The singleton instance of the FontFactory.
     */
    public static yFontFactory instance() {
        return instance;
    }
    
    /**
     * Create new font. 
     * 
     * @see y.module.ui.FontFactory#createFont(java.lang.String, int, boolean, boolean, boolean)
     */
    public yFont createFont(String name, int size, boolean bold, boolean italic, boolean underlined) {
        // first lookup if it already existed:
        String key = yFont.toKey(name, size, bold, italic, underlined);
        Object obj = fonts.get(key);
        if(obj!=null) {
            // found it!
            yFont _font = (yFont)obj;
            _font.addRef();
            return _font;
        }

        if(display==null) {
            yFrame frame = (yFrame)Application.instance().getFrame();
            display = frame.getDisplay();
        }
        // create real font resource:
        int style = ( bold ? SWT.BOLD : SWT.NORMAL ) | ( italic ? SWT.ITALIC : SWT.NORMAL );
        org.eclipse.swt.graphics.Font f = new org.eclipse.swt.graphics.Font(
            display, name, size, style);
        // wrap as jexi.ui.Font:
        yFont font = new yFont(name, size, bold, italic, underlined, f);
        // cache it:
        fonts.put(font.toString(), font);
        return font;
    }

    // enumerate all fonts installed in the system:
    public String[] enumerateFonts() {
        // TODO Auto-generated method stub
        if(fontNames==null) {
            fontNames = new String[3];
            fontNames[0] = "Arial";
            fontNames[1] = "System";
            fontNames[2] = "Tahoma";
        }
        return fontNames;
    }

    /**
     * dispose all fonts it used now. 
     * 
     * @see y.module.ui.FontFactory#clearAllFonts()
     */
    public void clearAllFonts() {
        Collection all_fonts = new ArrayList( fonts.values() );
        Iterator it = all_fonts.iterator();
        while(it.hasNext()) {
            yFont font = (yFont)it.next();
            while(font.refCount()>0) {
                System.out.println("WARNING: Some fonts are not released.");
                font.dispose();
            }
        }
        // clear hash table:
        fonts.clear();
    }

    /**
     * Remove the font from the cache. 
     * 
     */
    protected void remove(yFont yFont) {
        fonts.remove(yFont.toString());
    }

    /**
     * Get the count of current used fonts. 
     *  
     * @see y.module.ui.FontFactory#fontCount()
     */
    public int fontCount() {
        return fonts.size();
    }

    /**
     * Create the default English font. 
     * 
     * @return The default English font.
     */
    public yFont createDefaultEnglishFont() {
        return createFont("Times New Roman", 14, false, false, false);
    }
    
    public void debug() {
        System.out.println("\n[yFontFactory] fonts=" + fonts.size());
        Collection all_fonts = fonts.values();
        Iterator it = all_fonts.iterator();
        while(it.hasNext()) {
            yFont font = (yFont)it.next();
            font.debug();
        }
    }
}
