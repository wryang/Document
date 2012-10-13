/*
 * Created on 2004-8-11
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.view;

import java.io.InputStream;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;


/**
 * Manage all images used in the tool bar. 
 * 
 * @author Xuefeng
 */

import java.util.LinkedHashMap;
import java.util.Map;


public class yImageFactory {
	private static Map<String, Image> imageSet;
	private Display display;
	
	yImageFactory(Display display){
		this.display = display;
	}
	
	public  void initialize(){
		imageSet = new LinkedHashMap<String , Image>();
		imageSet.put("file_openIcon", getImageByUrl("res/opened.png"));
		imageSet.put("file_saveIcon", getImageByUrl("res/save.png"));
		imageSet.put("file_newIcon", getImageByUrl("res/newfile.png"));
		imageSet.put("titleFormatIcon", getImageByUrl("res/titleFormatIcon.png"));
		imageSet.put("pictureIcon", getImageByUrl("res/picture.png"));
		imageSet.put("redoIcon", getImageByUrl("res/redo.png"));
		imageSet.put("undoIcon", getImageByUrl("res/undo.png"));
		imageSet.put("boldIcon", getImageByUrl("res/bold.png"));
		imageSet.put("italicsIcon", getImageByUrl("res/italics.png"));
		imageSet.put("fontIcon", getImageByUrl("res/font.png"));
		imageSet.put("color0", getImageByUrl("res/color0.bmp"));
		imageSet.put("color1", getImageByUrl("res/color1.bmp"));
		imageSet.put("color2", getImageByUrl("res/color2.bmp"));
		imageSet.put("color3", getImageByUrl("res/color3.bmp"));
		imageSet.put("color4", getImageByUrl("res/color4.bmp"));
		imageSet.put("color5", getImageByUrl("res/color5.bmp"));
		imageSet.put("color6", getImageByUrl("res/color6.bmp"));
		imageSet.put("color7", getImageByUrl("res/color7.bmp"));
		imageSet.put("color8", getImageByUrl("res/color8.bmp"));
		imageSet.put("color9", getImageByUrl("res/color9.bmp"));
		imageSet.put("color10", getImageByUrl("res/color10.bmp"));
		imageSet.put("color11", getImageByUrl("res/color11.bmp"));
		imageSet.put("color12", getImageByUrl("res/color12.bmp"));
		imageSet.put("color13", getImageByUrl("res/color13.bmp"));
		imageSet.put("color14", getImageByUrl("res/color14.bmp"));
		imageSet.put("color15", getImageByUrl("res/color15.bmp"));
		
		
	}
	
	private  Image getImageByUrl(String path){
		Class<yImageFactory> classTar = yImageFactory.class;
		String full_path = classTar.getResource(path).toString();
		full_path = full_path.substring(5);
		Image img = new Image(display , full_path);
		return img;
	}
	
	public static Image getImage(String key){
		return imageSet.get(key);
	}
	


}

