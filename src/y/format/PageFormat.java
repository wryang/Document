/*
 * Created on 2004-7-20
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.format;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
/**
 * Page Format, associated by a Page object. 
 * See image/page_style.bmp 
 * 
 * @author Xuefeng
 */
public class PageFormat implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -1855998409379314391L;

	/**
     * The minimum value of the margin.
     */
    public static final transient int MIN_MARGIN = 12;

    /**
     * The minimum value of the available width.
     */
    public static final transient int MIN_BODY_WIDTH = 48;

    /**
     * The minimum value of the available height.
     */
    public static final transient int MIN_BODY_HEIGHT = 50;

    // member variables:
    private int width;
    private int height;

    private int leftMargin;
    private int rightMargin;

    private int topMargin;
    private int bottomMargin;

    /**
     * The default page style: A4 paper.
     */
    public static final transient PageFormat DEFAULT = new PageFormat(Paper.CUSTOM);

    /**
     * Default page style: A4.
     */
    public PageFormat() {
        reset(Paper.A4);
    }

    /**
     * Page style specified by the paper.
     * 
     * @param paper The paper type.
     */
    public PageFormat(Paper paper) {
        reset(paper);
    }

    /**
     * Reset the page style to the default values by specified paper type. 
     * For example, reset(Paper.A4); 
     * 
     * @param paper The paper type.
     */
    public void reset(Paper paper) {
        this.width = paper.width;
        this.height = paper.height;
        this.leftMargin = paper.leftMargin;
        this.rightMargin = paper.rightMargin;
        this.topMargin = paper.topMargin;
        this.bottomMargin = paper.bottomMargin;
    }

    /**
     * Calculate the available width of the page. 
     * 
     * @return The available width.
     */
    public int scaleWidth() {
        return width - leftMargin - rightMargin;
    }

    /**
     * Calculate the available height of the page. 
     * 
     * @return The available height.
     */
    public int scaleHeight() {
        return height - topMargin - bottomMargin;
    }

    /**
     * Get the left margin. 
     * 
     * @return The left margin.
     */
    public int getLeftMargin() {
        return this.leftMargin;
    }

    /**
     * Get the top margin. 
     * 
     * @return The top margin.
     */
    public int getTopMargin() {
        return this.topMargin;
    }

    /**
     * @return Returns the height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height The height to set.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return Returns the width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width The width to set.
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
    @Override
    public String toString() {
    	String dataS = "";
    	
    	dataS += this.height + " " + this.width + " " + this.topMargin + " " + this.bottomMargin + " " + this.leftMargin + " "
    	+ this.rightMargin;
    	
    	return dataS;
    }
    
    public void reset(String dataS) {
    	String[] data = dataS.split(" ");
    	
    	this.height = Integer.parseInt(data[0]);
    	this.width = Integer.parseInt(data[1]);
    	this.topMargin = Integer.parseInt(data[2]);
    	this.bottomMargin = Integer.parseInt(data[3]);
    	this.leftMargin = Integer.parseInt(data[4]);
    	this.rightMargin = Integer.parseInt(data[5]);
    }
    
    private void writeObject(ObjectOutputStream stream) throws IOException {
//    	stream.defaultWriteObject();
    	stream.writeObject(width);
    	stream.writeObject(height);
    	stream.writeObject(leftMargin);
    	stream.writeObject(rightMargin);
    	stream.writeObject(topMargin);
    	stream.writeObject(bottomMargin);
    }
    
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
//    	stream.defaultReadObject();
    	this.width = (Integer)stream.readObject();
    	this.height = (Integer)stream.readObject();
    	this.leftMargin = (Integer)stream.readObject();
    	this.rightMargin = (Integer)stream.readObject();
    	this.topMargin = (Integer)stream.readObject();
    	this.bottomMargin = (Integer)stream.readObject();
    }
}
