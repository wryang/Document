/*
 * Created on 2011-7-4
 * Author: y&y, Copyright (C) 2011, y&y.
 */

package y.command;

import y.view.yFrame;
import y.view.yLanguage;

/**
 * User change current system language. 
 * 
 * @author y&y
 */
public class ChangeLanguageCommand implements Command {

	private yFrame frame;
	private yLanguage language;
	
	public ChangeLanguageCommand(yFrame frame, yLanguage language) {
		this.frame = frame;
		this.language = language;
	}
	
	@Override
	public boolean canUndo() {
		return false;
	}

	@Override
	public boolean execute() {
		frame.changeLanguage(language);
		return true;
	}

	@Override
	public void unexecute() {
		if(!canUndo()) {
			return;
		}
	}

}
