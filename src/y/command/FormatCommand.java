/*
 * Created on 2011-7-4
 * Author: y&y, Copyright (C) 2011, y&y.
 */
package y.command;

import java.util.ArrayList;

import y.format.StringFormat;
import y.module.*;
import y.view.*;



/**
 * Format the selected text. 
 * 
 * @author y&y
 */
public class FormatCommand implements Command {

	class Couple {
		Paragraph p;
		ArrayList<StringFormat> sf;
		
		public Couple(Paragraph p, ArrayList<StringFormat> sf) {
			this.p = p;
			this.sf = sf;
		}
	}
	enum TYPE {
		SWITCH, MODIFY
	};
	
	// store the type of format action
	TYPE type;
	
	// store previous string format
	StringFormat sf;
	
    // store the document reference:
    private Document document;

    // store the selection:
    private Selection selection = null;

    // store changed paragraph
    ArrayList<Couple> changedCouple = new ArrayList<Couple>();
    
    // the format:
    private String fontName;
    private Integer fontSize;
    private Boolean bold;
    private Boolean italic;
    private Boolean underlined;
    private yColor color;
    
    // 
    int firstIndex;
    int lastIndex;
    Paragraph pFirst;
    Paragraph pLast;
    Position startPosition;
    Position endPosition;

    //
    boolean hasUndo = false;
    /**
     * Create a new FormatCommand. 
     * 
     * @param doc The document object.
     * @param fontName The font name, or null if ignore.
     * @param fontSize The font size, or null if ignore.
     * @param bold The bold attribute, or null if ignore.
     * @param italic The italic attribute, or null if ignore.
     * @param underlined The underlined attribute, or null if ignore.
     * @param color The color, or null if ignore.
     */
    protected FormatCommand(Document document, String fontName, Integer fontSize, Boolean bold, Boolean italic, Boolean underlined, yColor color) {
        this.document = document;
        this.selection = document.getSelection();
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.color = color;
    }

    /* (non-Javadoc)
     * @see jexi.core.command.Command#execute()
     */
    public boolean execute() {
    	if(hasUndo) {
            boolean bFirst = true;
            boolean bLast = false;
    		Paragraph p = pFirst;
            do {
            	ArrayList<StringFormat> changedSF = new ArrayList<StringFormat>(3);
                if(p==pLast) bLast = true;
                // ok, for this paragraph, we changed the format of its selected text:
                int start = bFirst?firstIndex:0;
                int end = bLast?lastIndex-1:p.getGlyphsCount()-1;
                if(end>=0) {
                	changedSF = p.format(start, end, fontName, fontSize, bold, italic, underlined, color);
                }
                // point to the next paragraph:
                changedCouple.add(new Couple(p, changedSF));
                p = document.nextParagraph(p);
                bFirst = false;
            }while(!bLast);
            document.compose();

            // when composed, need adjust the selection:
            document.getSelection().select(startPosition, endPosition);

            document.updateCaret();
            document.updateView();
            return true;
    	}
    	
    	type = TYPE.MODIFY;
    	Paragraph currentP = document.getCaret().getPargraph();
        if(selection==null) {
        	return false;
        }
        if(!selection.isSelected()) {
    		type = TYPE.SWITCH;
    		sf = currentP.changeCurrentStringFormat(fontName, fontSize, bold, italic, underlined, color);
        	return false;
        }

        // store the end position:
        AbsPosition endAbsPos = new AbsPosition(document, selection.getEndPosition());

        // find out all para graphs that is in the selection:
        pFirst = selection.getFirstSelectedParagraph();
        pLast = selection.getLastSelectedParagraph();
        boolean bFirst = true;
        boolean bLast = false;
        firstIndex = selection.getFirstSelectedIndex();
        lastIndex = selection.getLastSelectedIndex();
        Paragraph p = pFirst;
        do {
        	ArrayList<StringFormat> changedSF = new ArrayList<StringFormat>(3);
            if(p==pLast) bLast = true;
            // ok, for this paragraph, we changed the format of its selected text:
            int start = bFirst?firstIndex:0;
            int end = bLast?lastIndex-1:p.getGlyphsCount()-1;
            if(end>=0) {
            	changedSF = p.format(start, end, fontName, fontSize, bold, italic, underlined, color);
            }
            // point to the next paragraph:
            changedCouple.add(new Couple(p, changedSF));
            p = document.nextParagraph(p);
            bFirst = false;
        }while(!bLast);
        document.compose();

        // when composed, need adjust the selection:
        startPosition = selection.getStartPosition();
        endPosition = new Position(document, endAbsPos);
        selection.select(startPosition, endPosition);

        document.updateCaret();
        document.updateView();
        return true;
    }

    /* (non-Javadoc)
     * @see jexi.core.command.Command#unexecute()
     */
    public void unexecute() {
    	if(type == TYPE.SWITCH) {
    		Paragraph currentP = document.getCaret().getPargraph();
    		yFont font = sf.getFont();
    		currentP.changeCurrentStringFormat(font.getName(), font.getSize(), font.getBold(), font.getItalic(), font.getUnderlined(), sf.getColor());
    		return;
    	}
    	for(Couple couple : changedCouple) {
    		if(!couple.sf.isEmpty()) {
    			couple.p.resetStringFormats(couple.sf);
    		}
    	}
    	document.compose(); 
    	document.updateCaret();
    	document.updateView();
    	hasUndo = true;
    }

    /**
     * To get the description of this command.
     */
    public String toString() {
        return "Format the selected text.";
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
