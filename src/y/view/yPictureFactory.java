/*
 * Created on 2004-8-13
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.view;

import java.io.IOException;

import y.module.Picture;
import y.module.PictureFactory;



/**
 * TODO Description here...
 * 
 * @author Xuefeng
 */
public class yPictureFactory extends PictureFactory {

    /* (non-Javadoc)
     * @see jexi.core.PictureFactory#createPicture(java.lang.String)
     */
    public Picture createPicture(String filename) throws IOException {
        // TODO Auto-generated method stub
        return new yPicture(filename);
    }

}
