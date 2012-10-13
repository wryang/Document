/*
 * Created on 2011-7-4
 * Author: y&y, Copyright (C) 2011, y&y.
 */
package y.command;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import y.module.Document;
import y.module.PersistentDocument;
import y.view.yFrame;
import y.view.yLanguage;

import nu.xom.Element;

/**
 * User save the current document in a new path. 
 * 
 * @author y&y
 */
public class SaveAsCommand implements Command {

	private Document doc;
	private String filePath;
	
	/**
	 * @param doc the document to be stored
	 */
	public SaveAsCommand(Document doc, String filePath) {
		this.doc = doc;
		this.filePath = filePath;
	}
	
	@Override
	public boolean canUndo() {
		return false;
	}

	@Override
	public boolean execute() {
        if(filePath != null) {
        	try {
				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream(filePath));
				out.writeObject(this.doc);
				out.close();
	        	
				doc.setFilePath(filePath);
				doc.setSaved(true);
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return false;
	}

	@Override
	public void unexecute() {
		if(!canUndo()) {
			return;
		}
	}

}
