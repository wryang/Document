/*
 * Created on 2004-7-17
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.module;

import y.format.ParagraphFormat;
import y.format.StringFormat;
import y.view.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
/**
 * Row represent a row of glyphs. 
 * <b>NOTE</b>: it is a physical glyph used by compositor to 
 * format the document. 
 * 
 * @author Xuefeng
 */
public class Row implements Serializable/*implements Glyph*/ {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8683571968804324080L;

	// Unsupported operation exception description:
	private static final String UNSUPPORTED_OPERATION = 
		"add(), remove() is not supported by Row.";

	// store parent paragraph:
	private Paragraph paragraph;

	// store the glyphs it contains, 
	// use "startIndex" and "endIndex" 
	// to minimize the storage usage:
	private int startIndex;
	private int endIndex;
	// to cache the height:
	private int height = 0;

	/**
	 * Create a new Row object specified by the parameters.
	 * 
	 * @param paragraph The parent paragraph it belongs to.
	 * @param startIndex The start index of the child glyph.
	 * @param endIndex The end index of the child glyph.
	 */
	public Row(Paragraph paragraph, int startIndex, int endIndex) {
		Assert.checkNull(paragraph);
		Assert.checkTrue( startIndex>=0 && startIndex<=endIndex && endIndex<paragraph.getGlyphsCount() );
		this.paragraph = paragraph;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	/**
	 * Physical glyph that used to display do NOT support add(), remove() operations.
	 */
	public void add(int index, Glyph g) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Physical glyph that used to display do NOT support add(), remove() operations.
	 */
	public void remove(Glyph g) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Get the paragraph this row belongs to.
	 * 
	 * @return Paragraph that this row belongs to.
	 */
	public Paragraph getParagraph() {
		return this.paragraph;
	}

	/**
	 * Get the specified glyph in its child list. <br>
	 * 
	 * <b>NOTE</b>: The Row object doesn't store any glyph object 
	 * by itself. Instead it can access the children it contains 
	 * by member variable "paragraph" indirectly.
	 * 
	 * @param index The position of the glyph.
	 * @return The specified glyph.
	 */
	public Glyph child(int index) {
		Assert.checkTrue( index>=startIndex && index<=endIndex );
		return this.paragraph.child(index);
	}

    /**
     * To test the row is the first row in the paragraph. 
     * 
     * @return True if this row is the first row.
     */
    public boolean isFirst() {
        return this.paragraph.getRows().get(0)==this;
    }

    /**
     * To test the row is the last row in the paragraph.
     * 
     * @return True if this row is the last row.
     */
    public boolean isLast() {
        return this.paragraph.getRow(this.paragraph.getRowsCount()-1)==this;
    }

	/**
	 * Get the count of the child glyphs.
	 * 
	 * @return The count of its children.
	 */
	public int size() {
		return endIndex - startIndex + 1;
	}

	/**
	 * A Row's height is important to format page. so we should 
	 * cache the height. 
	 * 
     * @see y.module.Glyph#height()
     */
    public int height() {
        if(this.height==0) {
            int h = 0;
            int max = 0;
            for(int i=startIndex; i<=endIndex; i++) {
                Glyph g = (Glyph)this.paragraph.child(i);
                if( g instanceof Char )
                    h = this.paragraph.getStringFormat(i).getFont().height();
                else
                    h = g.height();
                max = (h > max) ? h : max;
            }
            // cache it:
            this.height = max + this.paragraph.getRowSpace();
        }
        return this.height;
    }

    /**
     * Get the width of the row. The width of the first row may be 
     * differ the other rows, so we pass the parameter 'this' to 
     * let the paragraph to detect if the row is the first row. 
     * 
     * @see y.module.Glyph#width()
     */
    public int width() {
        return this.paragraph.scaleWidth(this);
    }

    /**
     * If this row contains the specified glyph. 
     * 
     * @param index The glyph index.
     * @return True if it contains.
     */
    public boolean contains(int index) {
        Assert.checkTrue(index>=0);
        Assert.checkTrue(index<this.paragraph.getGlyphsCount());

        return (startIndex<=index && index<=endIndex);
    }

    /**
     * Draw a row of glyphs. 
     */
    public void draw(yGraphics g, int selStart, int selEnd) {
        // store the start point:
        int start_x = g.getCurrentX();
        int start_y = g.getCurrentY();

        for(int i=startIndex; i<=endIndex; i++) {
            Glyph glyph = (Glyph)this.paragraph.child(i);
            int space = this.paragraph.getRowSpace() / 2;

            if(glyph instanceof Char) {
                // set the font:
                y.format.StringFormat sf = this.paragraph.getStringFormat(i);
                yFont font = sf.getFont();

                int offset = height() - font.height() - space;
                g.moveTo(start_x, start_y + offset);
                g.setFont(font);
                if(isInRange(selStart+startIndex, selEnd+startIndex, i)) {
                    g.setBackcolor(yColor.BLACK);
                	g.setForecolor(yColor.WHITE);
                }
                else {
                    g.setBackcolor(yColor.WHITE);
                    g.setForecolor(sf.getColor());
                }
                glyph.draw(g);
                // calculate the next position:
                start_x += g.getCharWidth(((Char)glyph).charValue());
            }
            else {
                int offset = height() - space - glyph.height();
                g.moveTo(start_x, start_y + offset);
                glyph.draw(g);
                // calculate the next position:
                start_x += glyph.width();
            }
        }
    }

    // check if the index is in [selStart, selEnd):
    private boolean isInRange(int selStart, int selEnd, int index) {
        return selStart<=index && index<selEnd;
    }

    /**
     * Get the start index of the row. 
     * 
     * @return The start index.
     */
    public int getStartIndex() {
        return this.startIndex;
    }

    /**
     * Get the end index of the row. 
     * 
     * @return The end index.
     */
    public int getEndIndex() {
        return this.endIndex;
    }

    public void debug() {
        System.out.print("  Row " + this.paragraph.getRows().indexOf(this));
        System.out.print(" " + this.size() + " glyphs: <");
        for(int i=startIndex; i<=endIndex; i++) {
            Glyph g = this.paragraph.child(i);
            System.out.print(g.toString());
        }
        System.out.println(">");
    }
    
//    private void writeObject(ObjectOutputStream stream) throws IOException {
//    	stream.writeObject(paragraph);
//    	stream.writeObject(startIndex);
//    	stream.writeObject(endIndex);
//    	stream.writeObject(height);
//    }
//    
//    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
//    	this.paragraph = (Paragraph)stream.readObject();
//    	this.startIndex = (Integer)stream.readObject();
//    	this.endIndex = (Integer)stream.readObject();
//    	this.height = (Integer)stream.readObject();
//    }
}
