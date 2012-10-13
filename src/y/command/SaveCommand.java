/*
 * Created on 2011-7-4
 * Author: y&y, Copyright (C) 2011, y&y.
 */
package y.command;

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

/**
 * User save the current document. 
 * 
 * @author y&y
 */
public class SaveCommand implements Command {

	private Document doc;
	
	/**
	 * @param doc the document to be stored
	 * @param filePath the name of file used to stored document
	 */
	public SaveCommand(Document doc) {
		this.doc = doc;
	}
	
	@Override
	public boolean canUndo() {
		return false;
	}

	@Override
	public boolean execute() {
		try {
            String filePath = doc.getFilePath();
            
			if(filePath != null) {
				filePath = doc.getFilePath();
    			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
    			out.writeObject(doc);
    			out.close();
    			return true;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
