/*
// * Created on 2004-7-24
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.view;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.Externalizable;

import org.eclipse.swt.graphics.Font;

import y.module.Paragraph;
/**
 * SWTFont is a platform-dependent class and extends 
 * the abstract Font class. It has a real font resource 
 * and manage its life cycle. 
 * 
 * @author y&y
 */
public class yFont implements Externalizable{

    // store the real font resource:
    private org.eclipse.swt.graphics.Font font = null;

    // font attributes:
    private String name;
    private int size;
    private boolean bold;
    private boolean italic;
    private boolean underlined;

    // some static const:
    public static final int SIZE_6 = 6;
    public static final int SIZE_7 = 7;
    public static final int SIZE_8 = 8;
    public static final int SIZE_9 = 9;
    public static final int SIZE_10 = 10;
    public static final int SIZE_11 = 11;
    public static final int SIZE_12 = 12;
    public static final int SIZE_14 = 14;
    public static final int SIZE_16 = 16;
    public static final int SIZE_18 = 18;
    public static final int SIZE_20 = 20;
    public static final int SIZE_22 = 22;
    public static final int SIZE_24 = 24;
    public static final int SIZE_26 = 26;
    public static final int SIZE_28 = 28;
    public static final int SIZE_36 = 36;
    public static final int SIZE_48 = 48;
    public static final int SIZE_72 = 72;

    private int height; // font's height.

    private int refCount = 0; // reference count.

    private String m_toString = null; // cache "toString()"

    // store the defaultGraphics:
    private yGraphics g = null;

    yFont(String name, int size, boolean bold, boolean italic, boolean underlined,
        org.eclipse.swt.graphics.Font font)
    {
        // font attributes:
        this.name = name;
        this.size = size;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        // real resource:
        this.font = font;

        // try to measure the font height:
        if(g==null)
            g = (yGraphics)y.view.Application.instance().getFrame().getDefaultGraphics();
        // store the original font:
        org.eclipse.swt.graphics.Font orgFont = g.gc.getFont();
        // set new font:
        g.gc.setFont(font);
        // get the font height:
        this.height = g.gc.getFontMetrics().getHeight();
        // ok, restore the original font:
        g.gc.setFont(orgFont);

        // once it created, it has one reference:
        addRef();
    }

    public yFont() {
    	
    }
    
    private void resortGraphics() {
        if(g==null)
            g = (yGraphics)y.view.Application.instance().getFrame().getDefaultGraphics();
        // store the original font:
        org.eclipse.swt.graphics.Font orgFont = g.gc.getFont();
        // set new font:
        g.gc.setFont(font);
        // get the font height:
        this.height = g.gc.getFontMetrics().getHeight();
        // ok, restore the original font:
        g.gc.setFont(orgFont);
    }
    /**
     * @return Returns the font name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the font size.
     */
    public int getSize() {
        return size;
    }

    /**
     * @return Returns the bold.
     */
    public boolean getBold() {
        return bold;
    }

    /**
     * @return Returns the italic.
     */
    public boolean getItalic() {
        return italic;
    }

    /**
     * @return Returns the underlined.
     */
    public boolean getUnderlined() {
        return underlined;
    }

    /**
     * Get the font height. 
     * 
     * @return The font height (pixel).
     */
    public int height() {
        return this.height;
    }

    /**
     * The font only equals when the name, the size, the bold... 
     * are exactly equals. 
     * 
     * @return True if all attributes equals.
     */
    public boolean equals(Object o) {
        if(o instanceof yFont) {
            yFont f = (yFont)o;
            return this.toString().equals(f.toString());
        }
        return false;
    }

    /**
     * Get the hash code. 
     * 
     * @return The hash code of this font.
     */
    public int hashCode() {
        return this.toString().hashCode();
    }
    
    /**
     * This is used to create the corresponding key of the font object.
     * 
     * @return A String like "name-size-bold-italic-underlined".
     */
    public String toString() {
        if(m_toString==null) {
            m_toString = toKey(name, size, bold, italic, underlined);
        }
        return m_toString;
    }

    /**
     * This static function is to identify a font. 
     */
    public static String toKey(String name, int size, boolean bold, boolean italic, boolean underlined) {
        return name + "_" + size + "_" + 
            (bold ? "B" : "b") + 
            (italic ? "I" : "i") + 
            (underlined ? "U" : "u");
    }

    /**
     * Distroy the font if no other object referenced it. 
     * 
     * @see y.module.ui.Font#dispose()
     */
    public void dispose() {
        this.removeRef();
        if(this.refCount()>0)
            return;
        // if no other object which referenced this font, 
        // then distroy it:
        font.dispose();
        ((yFontFactory)yFontFactory.instance()).remove(this);
    }

    /**
     * Get the native font resource. 
     */
    public org.eclipse.swt.graphics.Font nativeFont() {
        return this.font;
    }

    protected void addRef() {
        this.refCount ++;
    }

    private void removeRef() {
        this.refCount --;
    }

    // check if some one is referenced this font:
    public int refCount() {
        return refCount;
    }

    public void debug() {
        System.out.println("  [font info] " + toString() + ", height=" + height + ", ref=" + refCount);
    }
    
    private void writeObject(ObjectOutputStream stream) throws IOException {
    	stream.writeObject(name);
    	stream.writeObject(size);
    	stream.writeObject(bold);
    	stream.writeObject(italic);
    	stream.writeObject(underlined);
    	stream.writeObject(height);
//    	stream.writeObject(g);
    	
    }
    
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    	this.name = (String)stream.readObject();
    	this.size = (Integer)stream.readObject();
    	this.bold = (Boolean)stream.readObject();
    	this.italic = (Boolean)stream.readObject();
    	this.underlined = (Boolean)stream.readObject();
    	this.height = (Integer)stream.readObject();
//    	this.g = (yGraphics)stream.readObject();
    }

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.font = (Font) in.readObject();
		this.name = (String) in.readObject();
		this.size = (Integer) in.readObject();
		this.bold = (Boolean) in.readObject();
		this.italic = (Boolean) in.readObject();
		this.underlined = (Boolean) in.readObject();
		this.height = (Integer) in.readObject();
		this.refCount = (Integer) in.readObject();
		this.m_toString = (String) in.readObject();
//		this.g =(yGraphics) in.readObject();
		resortGraphics();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(font);
		out.writeObject(name);
		out.writeObject(size);
		out.writeObject(bold);
		out.writeObject(italic);
		out.writeObject(underlined);
		out.writeObject(height);
		out.writeObject(refCount);
		out.writeObject(m_toString);
//		out.writeObject(g);
	}
}