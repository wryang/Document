/*
 * Created on 2004-7-18
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.module;

import java.io.Serializable;
/**
 * Picture represent an image.
 * 
 * @author Xuefeng
 */
public abstract class Picture implements Glyph, Serializable {

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "[Pic "+ width() +"x"+ height() +"]";
    }

}
