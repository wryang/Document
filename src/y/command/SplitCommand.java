/*
 * Created on 2011-7-3
 * Author: y&y, Copyright (C) 2011, y&y.
 */
package y.command;

import y.module.*;

/**
 * When user press ENTER, a SplitCommand will break the current 
 * paragraph into 2 parts. 
 * 
 * @author y&y
 */
public class SplitCommand implements Command {

    private Document document;
    // the caret position:
    private Position position;
    Paragraph p;
    Paragraph p2;

    /**
     * Create a break command. 
     * 
     * @param document The document.
     */
    public SplitCommand(Document document) {
        this.document = document;
        this.position = document.getCaret().getPosition();
    }

    /* (non-Javadoc)
     * @see y.module.command.Command#execute()
     */
    public boolean execute() {
    	Caret caret = document.getCaret();
        caret.moveTo(position);
        p = caret.getPargraph();
        p2 = p.split(caret.getInsertIndex());
        document.addParagraph(document.getParagraphIndex(p)+1, p2);
        p.debug();
        p2.debug();

        // then compose:
        this.document.compose();
        document.getCaret().moveRight();
        document.updateCaret();

        return true;
    }

    /* (non-Javadoc)
     * @see y.module.command.Command#unexecute()
     */
    public void unexecute() {
    	CommandManager.instance().newCombineCommand(document, p);
        document.removeParagraph(p2);
        document.compose();
        document.getCaret().moveTo(position);
        document.updateCaret();
        document.updateView();
    }

    /**
     * To get the description of this command.
     */
    public String toString() {
        return "Typed ENTER.";
    }

    /**
     * This command can support undo or not. 
     * 
     * @return True if this command supports undo.
     */
    public boolean canUndo() {
        return true;
    }
}
