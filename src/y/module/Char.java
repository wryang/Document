/*
 * Created on 2011-7-1
 * Author: y&y, Copyright (C) 2011, y&y.
 */
package y.module;

import y.format.ParagraphFormat;
import y.format.StringFormat;
import y.view.yGraphics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
/**
 * Char represent a "char" glyph such as "A", "B", "C", 
 * or other Chinese word. Always use GlyphFactory to create 
 * a new Char rather than by "new" keyword. And it is an 
 * immutable class. <br>
 * 
 * <b>NOTE</b> Char is a basic glyph do not contains physical information. 
 * Also it is a "leaf" object that do NOT support add(), remove()... operations.
 * 
 * @see y.module.CharFactory
 * 
 * @author y7y
 */
public class Char implements Glyph,Serializable {

	// Unsupported operation exception description:
	private static final String UNSUPPORTED_OPERATION = 
		"width(), height() operations are not supported by Char.";

	// store the "char" and make it immutable:
    private char c;

    // decide whether to draw "Return":
    public static boolean showReturn = true;

    // some special Char for convenite use:
    public static final Char RETURN = new Char('\r');
    public static final Char TABLE = new Char('\t');
    public static final Char SPACE = new Char(' ');

    // package-private constructer enforce user to 
    // get the instance from GlyphFactory.
    Char(char c) {
        this.c = c;
    }

    /**
     * Get the char value of this object.
     * 
     * @return char value.
     */
	public char charValue() {
        return this.c;
    }

	/**
	 * <b>IMPORTANT</b>: Used in comparation operation.
	 */
	public boolean equals(Object o) {
		if( this == o )
			return true;
		if( o instanceof Char ) {
			Char c = (Char)o;
			return c.c == this.c;
		}
		return false;
	}

	/**
	 * <b>IMPORTANT</b>: Used in hash table operation.
	 */
	public int hashCode() {
		return c;
	}

	/**
	 * Unsupported operation, Char do not need to know its height.
	 * 
     * @see y.module.Glyph#height()
     */
    public int height() {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    /**
	 * Unsupported operation, Char do not need to know its width.
	 * 
     * @see y.module.Glyph#width()
     */
    public int width() {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    /**
     * Draw a char on the display.
     * 
     * @see y.module.Glyph#draw(y.module.yGraphics.Graphics)
     */
    public void draw(yGraphics g) {
        if(c=='\t') // this char does not need to draw. 
            return;
        if(c=='\r') {
            // if(showReturn) { // TODO...
            // drawReturn(); // must draw the RETURN as "_/"
        }
        g.drawChar(c);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if(c=='\r')
            return "[END]";
        if(c=='\t')
            return "[TAB]";
        return "" + c;
    }
//    
//    private void writeObject(ObjectOutputStream stream) throws IOException {
//    	stream.writeObject(c);
//    }
//    
//    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
//    	this.c = (Character)stream.readObject();
//    }

}
