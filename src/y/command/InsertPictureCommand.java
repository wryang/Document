/*
 * Created on 2011-7-4
 * Author: y&y, Copyright (C) 2011, y&y.
 */
package y.command;

import y.module.*;

/**
 * Insert a picture. 
 * 
 * @author y&y
 */
public class InsertPictureCommand implements Command {

    // store the document reference:
    private Document document;

    // store the filename:
    private String filename;
    // store the caret position:
//    private Position position;
    // store the paragraph
    private Paragraph p;
    // store the inserted index of paragraph
    private int insertIndex;

    protected InsertPictureCommand(Document document, String filename) {
        this.document = document;
        this.filename = filename;
//        this.position = document.getCaret().getPosition();
    }

    /* (non-Javadoc)
     * @see jexi.core.command.Command#execute()
     */
    public boolean execute() {
        Picture pic = null;
        try {
            pic = PictureFactory.instance().createPicture(filename);
        }
        catch(java.io.IOException e) {
            // TODO: MessageBox...
            System.out.println("Load picture failed, filename=" + filename);
            return false;
        }

        // check if the selection should be removed:
        if(document.getSelection().isSelected())
            CommandManager.instance().newDeleteCommand(document);
        // insert the picture:
        Caret caret = this.document.getCaret();
        p = caret.getPargraph();
        insertIndex = caret.getInsertIndex();
        p.add(insertIndex, pic);
        // then compose:
        this.document.compose();

        // move the caret:
        this.document.getCaret().moveRight();
        // notify view:
        this.document.updateCaret();
        return true;
    }

    /* (non-Javadoc)
     * @see jexi.core.command.Command#unexecute()
     */
    public void unexecute() {
    	p.removeGlyphs(insertIndex, insertIndex + 1);
    	this.document.compose();
    	this.document.getCaret().moveLeft();
        this.document.updateCaret();
    }

    /**
     * To get the description of this command.
     */
    public String toString() {
        return "Insert picture.";
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
