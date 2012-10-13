/*
 * Created on 2004-7-31
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.view;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
/**
 * The implementation of Color. 
 * 
 * @author Xuefeng
 */
public final class yColor implements Serializable{


    /**
	 * 
	 */
	private static final long serialVersionUID = -7077301135753872984L;
	
	public static final transient yColor BLACK = yColorFactory.instance().createColor(0);
    public static final transient yColor WHITE = yColorFactory.instance().createColor(0xffffff);
    public static final transient yColor RED   = yColorFactory.instance().createColor(0xff0000);
    public static final transient yColor GEEN = yColorFactory.instance().createColor(0xff00);
    public static final transient yColor BLUE  = yColorFactory.instance().createColor(0xff);

    // store the key:
    private Integer key;
    // store the color resource:
    private org.eclipse.swt.graphics.Color color;

    // the ref count:
    private int refCount = 0;

    // key is used for get from the hash table:
    yColor(Integer key, org.eclipse.swt.graphics.Color color) {
        this.key = key;
        this.color = color;
        addRef();
    }

    public static Integer toKey(int r, int g, int b) {
        return new Integer( (r<<16) | (g<<8) | b );
    }

    /**
     * Get the key that used in hash table. 
     * 
     * @return Integer key.
     */
    public Integer getKey() {
        return this.key;
    }

    /**
     * Compare two objects. 
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if(this==o)
            return true;
        if(o instanceof yColor) {
            return this.key==((yColor)o).key;
        }
        return false;
    }

    /**
     * get the hash code. 
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return key.intValue();
    }

    /**
     * Dispose this color. 
     * 
     * @see y.module.ui.Color#dispose()
     */
    public void dispose() {
        removeRef();
        if(this.refCount>0)
            return;
        this.color.dispose();
        ((yColorFactory)yColorFactory.instance()).remove(this);
    }

    /**
     * Get the native color. 
     */
    public org.eclipse.swt.graphics.Color nativeColor() {
        return this.color;
    }

    protected void addRef() {
        this.refCount++;
    }

    protected void removeRef() {
        this.refCount--;
    }

    protected int refCount() {
        return this.refCount;
    }

    public void debug() {
        System.out.println("  Color=" + key.intValue() + ", ref=" + refCount);
    }
    
    
    private void writeObject(ObjectOutputStream stream) throws IOException {
    	stream.writeObject(key);
    	stream.writeObject(color);
    	
    }
    
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    	this.key = (Integer)stream.readObject();
    	this.color = (org.eclipse.swt.graphics.Color)stream.readObject();
    }
}
