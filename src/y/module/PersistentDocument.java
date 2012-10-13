package y.module;

import java.util.List;
import java.util.ArrayList;

import java.io.Serializable;

import y.format.PageFormat;

public class PersistentDocument implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 712734194610859222L;
	
    // store the paragraphs as data structure:
    private List paragraphs = new ArrayList(1);
    // store the formatted pages to display:
    private List pages = new ArrayList(1);
    // store a compositor:
    private Compositor compositor = new DocumentCompositor();
	// page format:
    private PageFormat pageFormat = PageFormat.DEFAULT;
    // identify whether the document has been saved
    private boolean isSaved = false;
    // file path of the document
    private String filePath;
    
    
    /**
     * 
     * @param paragraphs
     * @param pages
     * @param compositor
     * @param pageFormat
     */
    public PersistentDocument(
    		List paragraphs, List pages, Compositor compositor, PageFormat pageFormat,
    		boolean isSaved, String filePath
    ) {
    	this.paragraphs = paragraphs;
    	this.pages = pages;
    	this.compositor = compositor;
    	this.pageFormat = pageFormat;
    	this.isSaved = isSaved;
    	this.filePath = filePath;
    }
    
	public List getParagraphs() {
		return paragraphs;
	}

	public List getPages() {
		return pages;
	}

	public Compositor getCompositor() {
		return compositor;
	}

	public PageFormat getPageFormat() {
		return pageFormat;
	}

	public boolean isSaved() {
		return isSaved;
	}

	public String getFilePath() {
		return filePath;
	}
	
	
    
}
