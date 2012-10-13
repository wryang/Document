/*
 * Created on 2011-7-4
 * Author: y&y, Copyright (C) 2011, y&y.
 */

package y.command;

import y.module.Document;
import y.module.Position;
import y.view.yView;

/**
 * User create a new empty document. 
 * 
 * @author y&y
 */
public class NewCommand implements Command {

	private yView view;
	private CommandManager manager;
	
	public NewCommand(yView view, CommandManager manager) {
		this.view = view;
		this.manager = manager;
	}
	
	@Override
	public boolean canUndo() {
		return false;
	}

	@Override
	public boolean execute() {
		this.view.getDocument().getCaret().moveTo(new Position(0,0,0));
		this.view.getDocument().updateCaret();
		Document document = Document.createEmptyDocument(this.view);
		this.view.setDocument(document);
		this.manager.clear();
		this.view.update();
		return false;
	}

	@Override
	public void unexecute() {
		if(!canUndo()) {
			return;
		}
	}

}
