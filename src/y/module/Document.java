/*
 * Created on 2011-7-1
 * Author: y&y, Copyright (C) 2011, y&y.
 */
package y.module;

import java.io.*;
import java.util.*;

import y.format.PageFormat;
import y.view.*;



/**
 * Document represent the whole structure of the document data.
 * 
 * @author y&y
 */
public class Document implements Composition, Externalizable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -6006491547152531469L;
	
	// store the paragraphs as data structure:
    private List paragraphs = new ArrayList(1);
    
    // store the formatted pages to display:
    private List pages = new ArrayList(1);

	// page format:
    private PageFormat pageFormat = PageFormat.DEFAULT;

    // store the reference of y.module.view:
    private y.view.yView yView;

    // store a caret:
    private Caret caret;

    // store a compositor:
    private Compositor compositor = new DocumentCompositor();

    // store the selection:
    private Selection selection;

    // identify whether the document has been saved
    private boolean isSaved = false;
    
    // the file path of the document;
    private String filePath = null;
    
    public Document() {
    	this.compositor.setComposition(this);
    	this.selection = new Selection(this);
    	this.caret = new Caret(this, 0, 0, 0);
    }
    /**
     * Create an empty document. 
     * 
     * @param yView The yView reference.
     * @return The Document object.
     */
    public static Document createEmptyDocument(yView yView) {
        Document doc = new Document(yView);
        // to make sure there is at least one Paragraph:
        Paragraph p = new Paragraph(doc);
        // TODO: Add some glyphs to paragraph --------------
        //testAddGlyph(p, "Jexi 1.0, by Xuefeng.");
        //p.format(8,12,"Tahoma", null, Boolean.TRUE, null, null, null);
        //p.format(4, 7, f2, Color.BLACK);
        //p.format(8, 8, f, Color.BLUE);
        //p.debug();
        doc.addParagraph(p);
        p.debug();

        doc.compositor.setComposition(doc);
        doc.compose();

        // now set the caret in the first paragraph and the first index:
        doc.caret = new Caret(doc, 0, 0, 0);
        return doc;
    }

    private static void testAddGlyph(Paragraph p, String s) {
        char[] c = s.toCharArray();
        for(int i=0; i<c.length; i++)
            p.add(c[i]);
    }

    /**
     * Create a document from an existing file. 
     * 
     * @param filename The full path of the file.
     * @return The Document object.
     * @throws IOException If there is any IO error.
     * @throws FileNotFoundException If the file is not existing.
     */
    public static Document createDocument(yView yView, String filename)
        throws IOException, FileNotFoundException
    {
        Document doc = createEmptyDocument(yView);
        // TODO...
        return null;
    }

    /**
     * Compose the document. 
     */
    public void compose() {
        this.compositor.compose();
    }

    /**
     * To construct a new Document. 
     */
    private Document(yView yView) {
        this.yView = yView;
        selection = new Selection(this);
    }

    /**
     * Get the yView of this document. 
     * 
     * @return The yView object.
     */
    public yView getView() {
        return this.yView;
    }

    public void setView(yView view) {
    	this.yView = view;
    }
    /**
	 * Get the next paragraph, or null if the current is the last. 
	 * 
	 * @param current The current paragraph.
	 * @return The next paragraph, or null if no next paragraph.
	 */
	public Paragraph nextParagraph(Paragraph current) {
	    Assert.checkNull(current);
	    int n = paragraphs.indexOf(current);
	    Assert.checkTrue(n>=0);
	    n++; // point to next paragraph
	    if(n==paragraphs.size())
	        return null;
	    return (Paragraph)paragraphs.get(n);
	}

	/**
	 * Get the previous paragraph, or null if the current is the first. 
	 * 
	 * @param current The current paragraph.
	 * @return The previous paragraph, or null if no previous paragraph.
	 */
	public Paragraph previousParagraph(Paragraph current) {
	    Assert.checkNull(current);
	    int n = paragraphs.indexOf(current);
	    Assert.checkTrue(n>=0);
	    if(n==0) // current is the first
	        return null;
	    return (Paragraph)paragraphs.get(n-1);
	}

	/**
	 * Get the first paragraph. 
	 * 
	 * @return The first paragraph.
	 */
	public Paragraph firstParagraph() {
	    return (Paragraph)paragraphs.get(0);
	}

	/**
	 * Get the last paragraph. 
	 * 
	 * @return The last paragraph.
	 */
	public Paragraph lastParagraph() {
	    return (Paragraph)paragraphs.get(paragraphs.size()-1);
	}

    /**
     * Get the count of the paragraphs.
     * 
     * @return How many paragraphs.
     */
    public int getParagraphCount() {
        return paragraphs.size();
    }

    /**
     * Get the specified paragraph.
     * 
     * @param index The index of the paragraph.
     * @return The specified paragraph.
     */
    public Paragraph getParagraph(int index) {
        Assert.checkRange(index, paragraphs);
        return (Paragraph) this.paragraphs.get(index);
    }

    /**
     * Get the index of the specified paragraph. 
     * 
     * @param p The specified paragraph.
     * @return The index of this paragraph.
     */
    public int getParagraphIndex(Paragraph p) {
        return this.paragraphs.indexOf(p);
    }

    /**
     * Get all paragraphs. 
     * 
     * @return List that contains all paragraphs.
     */
    public List getParagraphs() {
        return this.paragraphs;
    }

    /**
     * Get the count of the pages. 
     * 
     * @return How many pages.
     */
    public int getPageCount() {
        return pages.size();
    }

    public PageFormat getPageFormat() {
        return this.pageFormat;
    }

    /**
     * Get the specified page.
     * 
     * @param index The index of the page.
     * @return The specified page.
     */
    public Page getPage(int index) {
        Assert.checkRange(index, this.pages);
        return (Page) this.pages.get(index);
    }

    /**
     * Get pages of the document. 
     * 
     * @return The pages array list.
     */
    public List getPages() {
        return this.pages;
    }

    /**
     * Get the last page. 
     * 
     * @return The last page.
     */
    public Page lastPage() {
        Assert.checkTrue(this.pages.size()>0);
        return (Page) this.pages.get(this.pages.size()-1);
    }

    /**
     * Get the first page. 
     * 
     * @return The first page.
     */
    public Page firstPage() {
        return (Page) this.pages.get(0);
    }

    /**
     * Add page to page list. 
     * 
     * @param index Where to insert.
     * @param page The page object.
     */
    public void addPage(int index, Page page) {
        this.pages.add(index, page);
    }

    /**
     * Add page to the end of the page list. 
     * 
     * @param page The page object.
     */
    public void addPage(Page page) {
        this.pages.add(page);
    }

    /**
     * Get the total width of the document. 
     * 
     * @return The total width.
     */
    public int width() {
        return ((Page)this.pages.get(0)).width();
    }

    /**
     * Get the total height of the document. 
     * 
     * @return The total height.
     */
    public int height() {
        return ((Page)this.pages.get(0)).height() * getPageCount();
    }

    /**
     * Clear all pages. 
     */
    public void clearAllPages() {
        this.pages.clear();
    }

    /**
     * Draw the document. This is called by y.module.view. Document should draw from it's (x, y.module). 
     * 
     * @param g The graphics object for drawing.
     * @param x The start position of x.
     * @param y.module The start position of y.module.
     * @param viewWidth The total width of the yView.
     * @param viewHeight The total height of the yView.
     */
    public void draw(yGraphics g, int x, int y, int viewWidth, int viewHeight) {
        Iterator it = this.pages.iterator();
        // the first page to be draw:
        int start_x = g.getCurrentX();
        int start_y = g.getCurrentY();
        while(it.hasNext()) {
            Page page = (Page)it.next();
            //if(page.intersects(x, y.module)) {
                //System.out.println("intersects!");
            //}
            page.draw(g, this.getSelection());
            start_y += page.height();
            g.moveTo(start_x, start_y);
        }
    }

    public boolean isEditRegion(int x, int y) {
        Assert.checkTrue(x>=0 && y>=0);

        int page_height = this.getPage(0).height();
        int index = y / page_height;

        if(index>=this.getPageCount()) return false;

        //System.out.println("Page " + index);
        return this.getPage(index).isEditRegion(x, y);
    }

    /**
     * Get the index of the page. 
     * 
     * @param page The page object.
     * @return The index of this page, or (-1) if no such page.
     */
    int indexOfPage(Page page) {
        return this.pages.indexOf(page);
    }

    /**
     * Get the index of the paragraph. 
     * 
     * @param paragraph The paragraph object.
     * @return The index of this paragraph, or (-1) if no such paragraph.
     */
    int indexOfParagraph(Paragraph paragraph) {
        return this.paragraphs.indexOf(paragraph);
    }

    /**
     * Add a paragraph into the document. 
     */
    public void addParagraph(int index, Paragraph p) {
        Assert.checkNull(p);
        Assert.checkTrue(index>=0 && index<=this.paragraphs.size());

        this.paragraphs.add(index, p);
    }

    /**
     * Remove the specified paragraph. 
     * 
     * @param index The index of the paragraph.
     */
    public void removeParagraph(int index) {
        this.paragraphs.remove(index);
    }

    /**
     * Remove the specified paragraph. 
     * 
     * @param p The paragraph object.
     */
    public void removeParagraph(Paragraph p) {
        this.paragraphs.remove(p);
    }

    /**
     * Add a paragraph into the end of the document. 
     * 
     * @param p The paragraph.
     */
    public void addParagraph(Paragraph p) {
        addParagraph(this.paragraphs.size(), p);
    }

    /* (non-Javadoc)
     * @see jexi.core.Glyph#child(int)
     */
    public Glyph child(int index) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see jexi.core.Glyph#intersects(int, int)
     */
    public boolean intersects(int x, int y) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Update the yView. 
     */
    public void updateView() {
        this.yView.update();
    }

    /**
     * Update the caret, to make the caret in the proprate position. 
     */
    public void updateCaret() {
        if(this.caret!=null) {
            Point p = this.caret.getLocation();
            this.yView.onSetCaret(p.x, p.y, this.caret.getHeight());
        }
    }

    /**
     * Get the caret. 
     * 
     * @return The Caret2.
     */
    public Caret getCaret() {
        return this.caret;
    }

    /**
     * Get the selection. 
     * 
     * @return The selection.
     */
    public Selection getSelection() {
        return this.selection;
    }
    
    public PersistentDocument createPersistentDocuemnt() {
    	return new PersistentDocument(this.paragraphs, this.pages, this.compositor, this.pageFormat, this.isSaved, this.filePath); 
    }
    
//    /**
//     * Restore document
//     * 
//     * @param doc the persistent path of saved document
//     */
//    public void restore(PersistentDocument doc, yView view) {
//    	this.paragraphs = doc.getParagraphs();
//    	this.pages = doc.getPages();
//    	this.compositor.setComposition(this);
//    	this.selection = new Selection(this);
//		this.yView = view;
//      this.caret = new Caret(this, 0, 0, 0);
//    	this.updateView();
//    }

	public boolean isSaved() {
		return isSaved;
	}

	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		paragraphs = (List)in.readObject();
		pages = (List)in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(paragraphs);
		out.writeObject(pages);
	}
	
	public Page getPageCurrent(){
		if(pages.get(0)!=null)
			return (Page)pages.get(0);
		else
			return null;
	}
}

