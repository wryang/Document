/*
 * Created on 2004-8-3
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.module;
import java.io.Serializable;
/**
 * Represent a point (x, y.module). 
 * 
 * @author Xuefeng
 */
public final class Point implements Serializable{
    public int x;
    public int y;

    /**
     * Create a new point (0, 0). 
     */
    public Point() { x = y = 0; }

    /**
     * Create a new point (x, y.module). 
     * 
     * @param x The point x.
     * @param y.module The point y.module.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
//    
//    private void writeObject(ObjectOutputStream stream) throws IOException {
//    	stream.writeObject(x);
//    	stream.writeObject(y);
//    }
//    
//    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
//    	this.x = (Integer)stream.readObject();
//    	this.y = (Integer)stream.readObject();
//    }
}
