package y.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;

public interface yLanguage {

	public String getTitle();
	
	public String getFileLabel();
	public String getNewDocumentLabel();
	public String getOpenLabel();
	public String getCloseLabel();
	public String getSaveDocumentLabel();
	public String getSaveAsLabel();
	
	public String getSavedFileTitle();
	public String getSavedFileLabel();
	
	public String getExitLabel();
	
	public String getEditLabel();
	public String getUndoLabel();
	public String getRedoLabel();
	public String getDeleteLabel();
	
	public String getInsertLabel();
	public String getInsertPictureLabel();
	public String getPictureFromFileLabel();
	public String[] getFilterNames();
	public String[] getExtensionsFilterNames();
	
	public String getHelpLabel();
	public String getFontLabel();
	public String getContactAuthorLabel();
	
	public String getAboutLabel();
	public String getAboutViewTitle();
	public String getAboutViewContent();
	
	public String getNewToolTip();
	public String getOpenToolTip();
	public String getSaveToolTip();
	public String getUndoToolTip();
	public String getRedoToolTip();
	
	public String[] getColorNames();
	public String[] getFontNames();
	public String getTitleFormatALabel();
	
	public String getTitleFormatBLabel();
	public String getEngLanguageLabel();
	public String getCHLanguageLabel();
	
	public String getNextPage();	
	public String getPrePage();
}
