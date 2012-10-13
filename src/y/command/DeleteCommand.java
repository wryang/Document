/*
 * Created on 2011-7-5
 * Author: y&y, Copyright (C) 2011, y&y.
 */
package y.command;

import java.util.*;

import y.module.*;



/**
 * Delete a glyph. 
 * 
 * @author y&y
 */
public final class DeleteCommand implements Command {
	
	private DeletedItem item;
    private Document document;
    private Position position;
    private boolean isUndo = false;
//    private Position firstDeletePos;
//    private Position lastDeletePos;
//    private ArrayList<Paragraph> deletedParagraph = new ArrayList<Paragraph>(5);
//    private ArrayList<Couple> deletedCouple = new ArrayList<Couple>();
    
    
    /**
     * Create a new delete command. 
     * 
     * @param document The document object.
     */
    public DeleteCommand(Document document) {
        this.document = document;
        this.position = new Position(document.getCaret().getPosition());
    }

    /* (non-Javadoc)
     * @see jexi.core.command.Command#execute()
     */
    public boolean execute() {
        Selection sel = document.getSelection();
        // only delete the current glyph:
        Caret caret = null;
        if(isUndo) {
        	caret = this.document.getCaret();
        	caret.moveTo(position);
        	
        }else {
            caret = this.document.getCaret();
        }
        
        AbsPosition caretAbsPos = new AbsPosition(document, caret.getPosition()); 
        // first find out which paragraph:
        Row row = caret.getRow();
        
        //if it is the first column of the row except the first row, move it to previous row
        if(!row.isFirst() && caret.getPosition().getColumnIndex()==0)
            caret.moveLeft();
        Paragraph p = row.getParagraph();
        
        int index = caret.getInsertIndex();
        if(index==p.getGlyphsCount()-1) {
            // cannot delete '\r'
            // but if there is the following paragraph,
            // should combine the two:
        	CommandManager.instance().newCombineCommand(document, p);
            //  but still return false:
            return false;
        }
        item = p.remove(index);
        this.document.compose();
        caret.moveTo(new Position(document, caretAbsPos));

        document.updateCaret();
        return true;
    }

    /* (non-Javadoc)
     * @see jexi.core.command.Command#unexecute()
     */
    public void unexecute() {
    	Caret caret = this.document.getCaret();
    	caret.moveTo(position);
    	caret.getPargraph().addDeletedItem(item);
        this.document.compose();

    	caret.moveRight();
        document.updateCaret();
        isUndo = true;
        return;
    }

    /**
     * To get the description of this command.
     */
    public String toString() {
        return "Delete.";
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
