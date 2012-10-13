/*
 * Created on 2011-7-4
 * Author: y&y, Copyright (C) 2011, y&y.
 */

package y.command;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import y.module.Caret;
import y.module.Document;
import y.module.PersistentDocument;
import y.module.Position;
import y.view.yFrame;
import y.view.yLanguage;
import y.view.yView;

/**
 * User open a new document. 
 * 
 * @author y&y
 */
public class OpenCommand implements Command {

	private String filePath;
	private yView view;
	
	public OpenCommand(yView view, String filePath) {
		this.view = view;
		this.filePath = filePath;
	}
	
	@Override
	public boolean canUndo() {
		return false;
	}

	@Override
	public boolean execute() {
		ObjectInputStream in;
    	if(!(filePath == null || filePath == "")) {
			try {
				in = new ObjectInputStream(new FileInputStream(filePath));
				Document document = (Document)in.readObject();
				document.setView(view);
				document.setFilePath(filePath);
				Caret caret = document.getCaret();
				view.setDocument(document);
				caret.moveTo(new Position(0, 0, 0));
				document.updateCaret();
				document.updateView();
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
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
