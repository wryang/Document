package y.view;

public class yEnglishLanguage implements yLanguage {

	@Override
	public String getAboutLabel() {
		return "&About y and y...";
	}

	@Override
	public String getAboutViewContent() {
		return "About y and y";
	}

	@Override
	public String getAboutViewTitle() {
		return "About y and y";
	}

	@Override
	public String getCloseLabel() {
		return "&Close";
	}

	@Override
	public String[] getColorNames() {
		String[] colorNames = {
	            "Black",  "Maroon",  "Green", "Olive",
	            "Navy",   "Purple",  "Teal",  "Gray",
	            "Silver", "Red",     "Lime",  "Yellow",
	            "Blue",   "Fuchsia", "Aqua",  "White"
	        };
		return colorNames;
	}

	@Override
	public String getContactAuthorLabel() {
		return "&Contact Author...";
	}

	@Override
	public String getDeleteLabel() {
		return "&Delete";
	}

	@Override
	public String getEditLabel() {
		return "&Edit";
	}

	@Override
	public String getExitLabel() {
		return "E&xit\tAlt+F4";
	}

	@Override
	public String getFileLabel() {
		return "&File";
	}

	@Override
	public String getFontLabel() {
		return "&Font...";
	}

	@Override
	public String[] getFontNames() {
		String[] fontNames = {
				"Arial", "Comic Sans MS", "Courier New", "MS Sans Serif"
				,"Tahoma", "Times New Roman"
		};
		return fontNames;
	}

	@Override
	public String getHelpLabel() {
		return "&Help";
	}

	@Override
	public String getInsertLabel() {
		return "&Insert";
	}

	@Override
	public String getInsertPictureLabel() {
		return "&Picture";
	}

	@Override
	public String getNewDocumentLabel() {
		return "&New";
	}

	@Override
	public String getNewToolTip() {
		return "Create a new document.";
	}

	@Override
	public String getOpenLabel() {
		return "&Open...\tCtrl+O";
	}

	@Override
	public String getOpenToolTip() {
		return "Open an existing document.";
	}

	@Override
	public String getPictureFromFileLabel() {
		return "From &File...";
	}

	@Override
	public String[] getFilterNames() {
		return new String [] {"Picture Files", "All Files (*.*)"};
	}
	
	@Override
	public String getRedoLabel() {
		return "&Redo\tCtrl+Y";
	}

	@Override
	public String getRedoToolTip() {
		return "Redo the previous command.";
	}

	@Override
	public String getSaveAsLabel() {
		return "Save &As...";
	}

	@Override
	public String getSaveDocumentLabel() {
		return "&Save\tCtrl+S";
	}

	@Override
	public String getSaveToolTip() {
		return "Save current document.";
	}

	@Override
	public String getUndoLabel() {
		return "&Undo\tCtrl+Z";
	}

	@Override
	public String getUndoToolTip() {
		return "Undo the last command.";
	}

	@Override
	public String[] getExtensionsFilterNames() {
		return new String[] {"file(*.yy)"};
	}

	@Override
	public String getTitle() {
		return "Word Editor";
	}

	@Override
	public String getSavedFileLabel() {
		return "file name ";
	}

	@Override
	public String getSavedFileTitle() {
		return "Save As";
	}
	public String getTitleFormatALabel(){
		return "Title A";
	}
	
	public String getTitleFormatBLabel(){
		return "Title B";
	}
	
	public String getEngLanguageLabel(){
		return "English";
	}
	
	public String getCHLanguageLabel(){
		return "Chinese";
	}	

	@Override
	public String getNextPage() {
		// TODO Auto-generated method stub
		return "Next";
	}

	@Override
	public String getPrePage() {
		// TODO Auto-generated method stub
		return "Pre";
	}
}
