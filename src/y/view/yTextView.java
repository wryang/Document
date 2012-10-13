/*
 * Created on 2004-7-24
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

import y.command.*;
import y.module.Document;
import y.module.Page;
import y.module.PersistentDocument;
import y.module.Position;



/**
 * The actual view displayed on the screen. 
 * 
 * @author Xuefeng
 */
public final class yTextView implements yView {

    private static final int KEY_BACK   = 8;
    private static final int KEY_RETURN = 13;
    private static final int KEY_DELETE = 127;

    private static final int KEY_UP    = 16777217;
    private static final int KEY_DOWN  = 16777218;
    private static final int KEY_LEFT  = 16777219;
    private static final int KEY_RIGHT = 16777220;

    private static final int KEY_PGUP = 16777221;
    private static final int KEY_PGDN = 16777222;
    private static final int KEY_HOME = 16777223;
    private static final int KEY_END  = 16777224;

    // store the document:
    protected Document document;
    
    // view control used to display:
    private Canvas canvas;
    // image buffer:
    private Image backBuffer = null;
    // cursor:
    private Cursor cursor = new Cursor(Display.getCurrent(), SWT.CURSOR_IBEAM);

    // store the size of the view:
    private int width;
    private int height;

    // store the size of the document:
    private int doc_width;
    private int doc_height;

    // IMPORTANT!!!
    // this point (x, y.module) is the position of the document's relative to view:
    private int offset_x = 0;
    private int offset_y = 0;

    // if the cursor is in the edit region:
    private boolean bEdit = false;
    // and the left button is pressed?
    private boolean bLButtonDown = false;

    // store the start position:
    Position startPosition = null;

    private yFrame frame = null;
    
    public yTextView(Canvas canvas, yFrame frame) {
        this.canvas = canvas;
        this.frame = frame;
        this.width = canvas.getBounds().width;
        this.height = canvas.getBounds().height;
        this.backBuffer = new Image(Display.getCurrent(),1, 1);
    }

    /**
     * Init the view.
     */
    public void init(y.module.Document document) {
        this.document = document;
        onDocumentSizeChanged();
        this.document.updateCaret();
    }

    /**
     * Get the actual height. 
     * 
     * @see y.view.yView.View#getHeight()
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Get the actual width. 
     * 
     * @see y.view.yView.View#getWidth()
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * dispose view. 
     * 
     * @see y.view.yView.View#dispose()
     */
    public void dispose() {
        if(this.backBuffer!=null)
            this.backBuffer.dispose();
    }

    /**
     * To ensure the caret is visible. 
     * 
     * @see y.view.yView.View#ensureCaretVisible()
     */
    public void ensureCaretVisible() {
        // TODO Auto-generated method stub

    }

    /**
     * Update view. 
     * 
     * @see y.view.yView.View#update()
     */
    public void update() {
        // the drawing start point is (offset_x, offset_y):
        offset_x = getOffsetX();
        offset_y = getOffsetY();

        // ok, now we get (x, y.module) and know from where to draw the document,
        // create a yGraphics:
        yGraphics g = new yGraphics(new org.eclipse.swt.graphics.GC(this.backBuffer));
        //yGraphics g2 = new yGraphics(new org.eclipse.swt.graphics.GC(this.canvas));

        // store the orginal color:
        org.eclipse.swt.graphics.Color org_foreColor = g.gc.getForeground();
        org.eclipse.swt.graphics.Color org_backColor = g.gc.getBackground();

        // fill the view's background:
        g.gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
        g.fillRect(0, 0, this.width, this.height);

        // set the back color to WHITE:
        g.gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

        // locate the start point where to draw:
        g.moveTo(offset_x, offset_y);

        // draw the document: (offset_x, offset_y) is the document point, 
        // and (width, height) is the size of view.
        document.draw(g, offset_x, offset_y, this.width, this.height);

        if(frame.isPageChanged()){
        	frame.changePageNavigation();
        }
        // copy to canvas:
        GC g2 = new GC(this.canvas);
        g2.drawImage(this.backBuffer, 0, 0);
        g2.dispose();

        // release the yGraphics:
        g.gc.setForeground(org_foreColor);
        g.gc.setBackground(org_backColor);
        g.dispose();

    }

    // if x is in the range [min, max], x is returned, 
    // else the closest value (min or max) is returned.
    private int ensureInRange(int min, int max, int v) {
        if(v<min) return min;
        if(v>max) return max;
        return v;
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#onSizeChanged(int, int)
     */
    public void onSizeChanged(int width, int height) {
        if(width<=0 || height<=0) return;

        System.out.println("yTextView::onSizeChanged() width=" + width + " height=" + height);
        this.width = width;
        this.height = height;

        // reset back buffer:
        this.backBuffer.dispose();
        this.backBuffer = new Image(Display.getCurrent(),width, height);

        setOffsetX(getOffsetX());
        setOffsetY(getOffsetY());
        this.document.updateCaret();
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#getOffsetX()
     */
    public int getOffsetX() {
        return this.offset_x;
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#getOffsetY()
     */
    public int getOffsetY() {
        return this.offset_y;
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#onDocumentSizeChanged()
     */
    public void onDocumentSizeChanged() {
        if(this.document!=null) {
            doc_width = this.document.width();
            doc_height = this.document.height();
            setOffsetX(getOffsetX());
            setOffsetY(getOffsetY());
        }
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#setOffsetX(int)
     */
    public void setOffsetX(int x) {
        this.offset_x = x;
        if(doc_width<=this.width)
            offset_x = ( this.width - doc_width ) / 2;
        else
            offset_x = ensureInRange( this.width-doc_width, 0, offset_x);
        }

    /* (non-Javadoc)
     * @see jexi.ui.View#setOffsetY(int)
     */
    public void setOffsetY(int y) {
        this.offset_y = y;
        if(doc_height<=this.height)
            offset_y = ( this.height - doc_height ) / 2;
        else
            offset_y = ensureInRange(this.height-doc_height, 0, offset_y);
    }

    /**
     * Translate the point of view to point of document. 
     * 
     * @param x The point x.
     * @return The point of document.
     */
    public int transX(int x) {
        return x - offset_x;
    }

    /**
     * Translate the point of view to point of document. 
     * 
     * @param y.module The point y.module.
     * @return The point of document.
     */
    public int transY(int y) {
        return y - offset_y;
    }

    public void onMouseMove(int x, int y) {
        //System.out.println("[event]MouseMove");
        int dx = transX(x);
        int dy = transY(y);
        if(dx>=0 && dy>=0) {
            bEdit = this.document.isEditRegion(dx, dy);
            if(bEdit) {
                //System.out.println("I");
                this.canvas.setCursor(cursor);
                if(bLButtonDown) {
                    document.getCaret().setLocation(dx, dy);
                    document.getSelection().select(startPosition, document.getCaret().getPosition());
                    document.getSelection().debug();
                    update();
                }
            }
            else
                this.canvas.setCursor(null);
        }
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#onLButtonDown(int, int)
     */
    public void onLButtonDown(int x, int y) {
        System.out.println("[event]LButtonDown");
        // TODO Auto-generated method stub
        bLButtonDown = true;
        if(bEdit) {
            int dx = transX(x);
            int dy = transY(y);
            if(dx>=0 && dy>=0) {
                this.document.getSelection().unselect();
                this.document.getCaret().setLocation(dx, dy);
                this.startPosition = this.document.getCaret().getPosition();
            }
        }

    }

    /* (non-Javadoc)
     * @see jexi.ui.View#onLButtonUp(int, int)
     */
    public void onLButtonUp(int x, int y) {
        System.out.println("[event]LButtonUp");
        bLButtonDown = false;
        if(bEdit) {
            int dx = transX(x);
            int dy = transY(y);
            if(dx>=0 && dy>=0) {
                document.getCaret().setLocation(dx, dy);
                document.getSelection().select(startPosition, document.getCaret().getPosition());
                document.getSelection().debug();
                update();
            }
        }
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#onRButtonDown(int, int)
     */
    public void onRButtonDown(int x, int y) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#onRButtonUp(int, int)
     */
    public void onRButtonUp(int x, int y) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#onLButtonDblClick(int, int)
     */
    public void onLButtonDblClick(int x, int y) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#onSetCaret(int, int, int)
     */
    public void onSetCaret(int x, int y, int height) {
        System.out.println("set caret at x=" + x + " y.module=" + y + " height=" + height);
        Caret c = this.canvas.getCaret();
        c.setBounds(x+offset_x, y+offset_y, 2, height);
        this.canvas.setCaret(c);
        update();
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#onKeyPressed(char)
     */
    public void onKeyPressed(char c) {
        CommandManager.instance().newInsertCommand(this.document, c);
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#onFunctionKeyPressed(int, boolean, boolean, boolean)
     */
    public void onFunctionKeyPressed(int keycode, boolean shift, boolean ctrl, boolean alt) {
        // TODO Auto-generated method stub
        System.out.println("keycode=" + keycode + " shift=" + shift + " ctrl=" + ctrl + " alt=" + alt);
        switch(keycode) {
        case KEY_RETURN:
            CommandManager.instance().newSplitCommand(document);
            break;

        case KEY_DELETE:
            CommandManager.instance().newDeleteCommand(document);
            break;

        case KEY_BACK:
            // in fact the back command is just like the delete:
            if(document.getCaret().moveLeft()) {
                CommandManager.instance().newDeleteCommand(document);
                document.updateCaret();
            }
            break;

        case KEY_LEFT:
            this.document.getSelection().unselect();
            this.document.getCaret().moveLeft();
            this.document.updateCaret();
            break;

        case KEY_RIGHT:
            this.document.getSelection().unselect();
            this.document.getCaret().moveRight();
            this.document.updateCaret();
            break;

        case KEY_UP:
            // TODO: this.document.getCaret().moveUp();
            break;

        case KEY_DOWN:
            // TODO: this.document.getCaret().moveDown();
            break;
        }
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#onFormatChanged(java.lang.String, java.lang.Integer, java.lang.Boolean, java.lang.Boolean, java.lang.Boolean, jexi.ui.Color)
     */
    public void onFormatChanged(String fontName, Integer fontSize,
        Boolean bold, Boolean italic, Boolean underlined, yColor color)
    {
        CommandManager.instance().newFormatCommand(document, fontName, fontSize, bold, italic, underlined, color);
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#onInsertPictureFromFile(java.lang.String)
     */
    public void onInsertPictureFromFile(String filename) {
        CommandManager.instance().newInsertPictureCommand(document, filename);
    }


    /**
     * Get the document. 
     * 
     * @see y.view.yView.View#getDocument()
     */
    public Document getDocument() {
        return this.document;
    }

    /**
     * Set the document. 
     * 
     * @see y.view.yView.View#setDocument(y.module.Document)
     */
    public void setDocument(Document document) {
        this.document = document;
        onDocumentSizeChanged();
    }
    
    public void onSaveButtonClick() {
    	if(document.getFilePath() != null) {
        	CommandManager.instance().newSaveCommand(document);
    	}else {
    		this.onSaveAsButtonClick();
    	}
    }
    
    public void onSaveAsButtonClick() {
    	yLanguage language = frame.getLanguage();
    	Shell shell = frame.getShell();
    	String filePath = null;
    	String fileName = null;
    	
        FileDialog dialog = new FileDialog (shell, SWT.SAVE);
        dialog.setText(language.getSavedFileTitle());
        dialog.setFileName(language.getSavedFileLabel());
        dialog.setFilterExtensions(language.getExtensionsFilterNames());
        dialog.open();
    	filePath = dialog.getFilterPath();
    	fileName = dialog.getFileName();
    	fileName = fileName.substring(0, fileName.length() - 1);
    	filePath += "\\" + fileName;
    	
        if(filePath != null) {
        	CommandManager.instance().newSaveAsCommand(this.document, filePath);
        }
    }
    
	@Override
    public void onOpenButtonClick() {
		ObjectInputStream in;
		String filePath = null;
		
		Shell shell = frame.getShell();
		yLanguage language = frame.getLanguage();
		
        FileDialog dialog = new FileDialog (shell, SWT.OPEN);
    	dialog.setFilterNames (language.getExtensionsFilterNames());
    	dialog.setFilterExtensions (new String [] {"*.yy"});
    	filePath = dialog.open();
    	
    	if(!(filePath == null || filePath == "")) {
    		CommandManager.instance().newOpenCommand(this, filePath);
    	}
    }

	public void onRedoButtonClick() {
		CommandManager.instance().redo();
	}
	
	public void onUndoButtonClick() {
		CommandManager.instance().undo();
	}
	
	public void onNewButtonClick() {
		Document document = Document.createEmptyDocument(this);
		this.setDocument(document);
		this.update();
		CommandManager.instance().newNewCommand(this);
	}
	
	public void onExitButtonClick() {
		System.exit(0);
	}
	
	   public void setSliderV(Slider sliderV){
	    	//this.sliderV = sliderV;
	    	
	    }

		@Override
		public void setPageNavigation(int pageIndex) {
			// TODO Auto-generated method stub
			Page page = document.getPageCurrent();
	    	setOffsetY(-page.height()*pageIndex);
	    	update();
		}
		
		//!_____________________________________START
		public int pageChangedNotif(){
			int pageCounts = document.getPageCount();
			return pageCounts;
		}
		//!_____________________________________END

		@Override
		public int getPageCurrent() {
			// TODO Auto-generated method stub
			Page page = document.getPageCurrent();
			return Math.abs(getOffsetY()/page.height());
		}
}
