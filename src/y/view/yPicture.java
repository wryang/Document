/*
 * Created on 2004-8-13
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.view;

import java.io.*;


import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import y.module.Picture;


/**
 * yPicture is the implementation of jexi.core.Picture, 
 * it is a Glyph. 
 * 
 * @author Xuefeng
 */
public class yPicture extends Picture implements Serializable {

    private Image image;
    private int width;
    private int height;

    yPicture(String filename) throws IOException {
        try {
            image = new Image(Display.getCurrent(), filename);
            Rectangle r = image.getBounds();
            this.width = r.width;
            this.height = r.height;
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    /* (non-Javadoc)
     * @see jexi.core.Glyph#draw(jexi.ui.Graphics)
     */
    public void draw(yGraphics g) {
        yGraphics swtg = (yGraphics)g;
        swtg.gc.drawImage(image, g.getCurrentX(), g.getCurrentY());
    }

    /* (non-Javadoc)
     * @see jexi.core.Glyph#width()
     */
    public int width() {
        return this.width;
    }

    /* (non-Javadoc)
     * @see jexi.core.Glyph#height()
     */
    public int height() {
        return this.height;
    }

}
