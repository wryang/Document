/*
 * Created on 2004-8-1
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.view;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

import y.module.Document;


/**
 * ScrollableView implementation. 
 * 
 * @author Xuefeng
 */
public final class yScrollableViewDecorator extends yViewDecorator {

    private final int PAGE_STEP = 48;
    private final int CLICK_STEP = 32;

    private final Slider sliderH;
    private final Slider sliderV;

    /**
     * Constructor. 
     * 
     * @param component The component to be decorated.
     */
    
    public yScrollableViewDecorator(yView yView, Slider sliderH, Slider sliderV) {
        super(yView);
        this.sliderH = sliderH;
        this.sliderV = sliderV;
        //setOffsetX(0);
        //setOffsetY(0);
        this.sliderH.setIncrement(CLICK_STEP);
        this.sliderV.setIncrement(CLICK_STEP);
        this.sliderH.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                onHScrollChanged();
                update();
            }
        });
        this.sliderV.addListener (SWT.Selection, new Listener () {
            public void handleEvent (Event e) {
                onVScrollChanged();
                update();
            }
        });
    }

    private void onVScrollChanged() {
        System.out.println(sliderV.getSelection());
        setOffsetY(-sliderV.getSelection());
        this.component.getDocument().updateCaret();
    }

    private void onHScrollChanged() {
        setOffsetX(-sliderH.getSelection());
        this.component.getDocument().updateCaret();
    }

    public void resetVScroll(Slider slideV) {
    	setOffsetY(slideV.getSelection());
    	this.component.getDocument().updateCaret();
    }
    
    /* (non-Javadoc)
     * @see jexi.ui.View#update()
     */
    public void update() {
        this.component.update();
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#setDocument(jexi.core.Document)
     */
    public void setDocument(Document document) {
        this.component.setDocument(document);
        onDocumentSizeChanged();
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#onSizeChanged(int, int)
     */
    public void onSizeChanged(int width, int height) {
        // NOTE: The size is the control textView (Canvas):
        this.component.onSizeChanged(width, height);
        resetScroll();
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#ensureCaretVisible()
     */
    public void ensureCaretVisible() {
        this.component.ensureCaretVisible();
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#onDocumentSizeChanged()
     */
    public void onDocumentSizeChanged() {
        System.out.println("Document size changed!!!!!!!!!!!!!!!!!");
        this.component.onDocumentSizeChanged();
        resetScroll();
    }

    private void resetScroll() {
        Document doc = this.component.getDocument();
        if(doc!=null) {
            sliderV.setMinimum(0);
            int max = doc.height() - getHeight();
            if(max<=0) {
                sliderV.setEnabled(false);
            }
            else {
                sliderV.setEnabled(true);
                //max = max > 0 ? max : 0;
                System.out.println("vscroll max=" + max);
                sliderV.setMaximum(max + sliderV.getThumb());
                sliderV.setPageIncrement( getHeight() - PAGE_STEP > 0 ? getHeight() - PAGE_STEP : 0 );
            }

            sliderH.setMinimum(0);
            max = doc.width() - getWidth();
            if(max<=0) {
                sliderH.setEnabled(false);
            }
            else {
                sliderH.setEnabled(true);
                System.out.println("hscroll max=" + max);
                sliderH.setMaximum(max + sliderH.getThumb());
                sliderH.setPageIncrement( getWidth() - PAGE_STEP > 0 ? getWidth() - PAGE_STEP : 0 );
            }
        }
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#setOffsetX(int)
     */
    public void setOffsetX(int x) {
        this.component.setOffsetX(x);
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#setOffsetY(int)
     */
    public void setOffsetY(int y) {
        this.component.setOffsetY(y);
    }
    


    /* (non-Javadoc)
     * @see jexi.ui.View#dispose()
     */
    public void dispose() {
        this.component.dispose();
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#getDocument()
     */
    public Document getDocument() {
        return this.component.getDocument();
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#getHeight()
     */
    public int getHeight() {
        return this.component.getHeight();
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#getOffsetX()
     */
    public int getOffsetX() {
        return this.component.getOffsetX();
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#getOffsetY()
     */
    public int getOffsetY() {
        return this.component.getOffsetY();
    }

    /* (non-Javadoc)
     * @see jexi.ui.View#getWidth()
     */
    public int getWidth() {
        return this.component.getWidth();
    }

    /*
     * @see jexi.ui.View#onMouseMove(int, int)
     */
    public void onMouseMove(int x, int y) {
        this.component.onMouseMove(x, y);
    }

	@Override
	public void onOpenButtonClick() {
		this.component.onOpenButtonClick();
	}

	@Override
	public void onSaveAsButtonClick() {
		this.component.onSaveAsButtonClick();
	}

	@Override
	public void onSaveButtonClick() {
		this.component.onSaveButtonClick();
	}

	@Override
	public void onExitButtonClick() {
		this.component.onExitButtonClick();
	}

	@Override
	public void onNewButtonClick() {
		this.component.onNewButtonClick();
	}

	@Override
	public void onRedoButtonClick() {
		this.component.onRedoButtonClick();
	}

	@Override
	public void onUndoButtonClick() {
		this.component.onUndoButtonClick();
	}
	
	@Override
	public void setSliderV(Slider sliderV) {
		this.component.setSliderV(sliderV);
	}

	@Override
	public void setPageNavigation(int pageIndex) {
		int value = (this.sliderV.getMaximum() / this.component.getDocument().getPageCount()) * pageIndex;
		sliderV.setSelection(value);
		this.resetVScroll(sliderV);
		this.component.setPageNavigation(pageIndex);
	}
	
	@Override
	public int pageChangedNotif() {
		return this.component.pageChangedNotif();
	}

	@Override
	public int getPageCurrent() {
		return this.component.getPageCurrent();
	}
}
