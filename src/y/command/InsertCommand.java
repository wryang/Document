/*
 * Created on 2011-7-4
 * Author: y&y, Copyright (C) 2011, y&y.
 */
package y.command;

import y.module.*;

/**
 * User typed a new character. 
 * 
 * @author y&y
 */
public class InsertCommand implements Command {

	// the chat to be inserted
    private Char c;
    
    // the index of char to be inserted
    private int indexOfChar;
    
    // the position of caret before inserting the char
    private Position positionOfCaret;
    
    // store the document reference:
    private Document document;
    
    // identify whether it means redo
    private boolean isRedo = false;

    protected InsertCommand(Document document, char c) {
        this.document = document;
        this.c = CharFactory.instance().createChar(c);
    }

    /* (non-Javadoc)
     * @see jexi.core.command.Command#execute()
     */
    public boolean execute() {
        Caret caret = this.document.getCaret();
        
    	if(!isRedo) {
            // check if the selection should be removed:
            if(document.getSelection().isSelected()) {
            	document.getSelection().unselect();
//                CommandManager.instance().newDeleteCommand(document);
            }

            // insert the character:
            positionOfCaret = caret.getPosition();
            
            indexOfChar = caret.getInsertIndex();
            caret.getPargraph().add(indexOfChar, this.c);
    	}else {
    		caret.moveTo(positionOfCaret);
    		caret.getPargraph().add(caret.getInsertIndex(), this.c);
    	}
        // store the position:
        AbsPosition caretAbsPos = new AbsPosition(document, positionOfCaret);
        // then compose:
        this.document.compose();

        // move the caret:
        caret.moveTo(new Position(document, caretAbsPos));
        caret.moveRight();

        System.out.println("Move next:");
        Position p = caret.getPosition();
        if(p.getColumnIndex()== 0) // if just move to the next row:
            this.document.getCaret().moveRight();
        // notify view:
        this.document.updateCaret();
        return true;
    }

    /* (non-Javadoc)
     * @see jexi.core.command.Command#unexecute()
     */
    public void unexecute() {

        Caret caret = this.document.getCaret();

        // move the caret:
        caret.moveTo(positionOfCaret);
        
        // delete the character:
        caret.getPargraph().remove(indexOfChar);
        
        // then compose:
        this.document.compose();

        // notify view:
        this.document.updateCaret();
        
        // means it will perform redo if execute again
        isRedo = true;
    }

    /**
     * To get the description of this command.
     */
    public String toString() {
        return "Type " + c;
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
