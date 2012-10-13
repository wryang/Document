/*
 * Created on 2011-7-4
 * Author: y&y, Copyright (C) 2011, y&y.
 */
package y.command;

import java.util.ArrayList;
import java.util.List;

import y.module.*;

/**
 * User combine the two paragraph if needed. 
 * 
 * @author y&y
 */
public class CombineCommand implements Command{

	private Document document;
	private Paragraph para;
	private List preStringFormats = new ArrayList(5);
	
	public CombineCommand(Document document, Paragraph para) {
		this.document = document;
		this.para = para;
	}
	
	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public boolean execute() {
        Paragraph next = document.nextParagraph(para);
        if(next==null) {
            System.out.println("No next paragraph.");
        }
        else {
            System.out.println("Found the next paragraph.");
            para.combine(next, preStringFormats); 
            document.removeParagraph(next);
            para.debug();
            document.compose();
            document.updateView();
        }
		return false;
	}

	@Override
	public void unexecute() {
		if(!canUndo()) {
			return;
		}
		para.decompose(para, preStringFormats);
	}

}
