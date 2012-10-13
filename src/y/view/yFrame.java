/*
 * Created on 2004-7-24
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.view;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.program.*;

import y.command.CommandManager;

/**
 * The implementation of Frame. And this is the real window 
 * displayed on the screen! <br>
 * <b>NOTE</b>: The UI was created by ui-design-tool, the swt-designer, 
 * (see: <a href="www.swt-designer.com">www.swt-designer.com</a>), 
 * so modify carefully! 
 * 
 * @author Xuefeng
 */
public class yFrame{

    // store the view:
    private yView view;

    // the device:
    private Display display = null;
    // the main window:
    private Shell shell = null;

    // the default graphics:
    private  yGraphics defaultGraphics = null;

    private  ToolBar toolBarCommon;
    private  ToolBar toolBarFormat;
    
    private	ToolBar toolBarNavigation;

    private  Combo cmbFontName;
    private  Combo cmbFontSize;

    private  Composite ScrollableView;
    private  Canvas textView;

    private  Slider sliderV;
    private  Slider sliderH;

    // caret:
    private  Caret caret;

    // color-select menu:
    private  Menu mnuColorSelect;
    private  MenuItem[] mnuColor = new MenuItem[16];

    // image factory:
    private  yImageFactory imageFactory;
    
    // language factory
    private  yLanguage language;

    //items
    private  MenuItem mnuFile;
    private  MenuItem mnuFileNew;
    private  MenuItem mnuFileOpen;
//    private  MenuItem mnuFileClose;
    private  MenuItem mnuFileSave;
    private  MenuItem mnuFileSaveAs;
    private  MenuItem mnuFileExit;
    private  MenuItem mnuEdit;
    private  MenuItem mnuEditUndo;
    private  MenuItem mnuEditRedo;
    private  MenuItem mnuEditDelete;
    private  MenuItem mnuInsert;
    private  MenuItem mnuInsertPicture;
    private  MenuItem mnuInsertPictureFromFile;
    private  MenuItem mnuHelp;
    private  MenuItem mnuHelpContactAuthor;
    private  MenuItem mnuHelpAbout;
    
    private  ToolItem tbNew;
    private  ToolItem tbOpen;
    private  ToolItem tbSave;
    private  ToolItem tbUndo;
    private  ToolItem tbRedo;
    private transient ToolItem tbLanguageChange;
    
    private Menu popMenu;
	private MenuItem redoPopBtn = null;
	private MenuItem undoPopBtn = null;
	private MenuItem newFilePopBtn = null; 
	private MenuItem openFilePopBtn = null;
	private MenuItem saveFilePopBtn = null;
	
	private Menu languageMenu;
	private MenuItem engLanguageMenu;
    private MenuItem chLanguageMenu;
    
    private transient ArrayList<ToolItem> navigationPage = new ArrayList<ToolItem>();
    private transient ToolItem pageNext;
    private transient ToolItem pagePre;
//    private transient ToolItem page;
    /**
     * Get the display object. 
     * 
     * @return The display object.
     */
    public Display getDisplay() {
        return this.display;
    }

    /**
     * Get the shell object.
     * 
     * @return The shell object.
     */
    public Shell getShell() {
        return this.shell;
    }

    /**
     * Initialize the frame. 
     * 
     * @see y.view.ui.Frame#init()
     */
    public void init(){
        // create device:
        this.display = new Display();
        this.imageFactory = new yImageFactory(this.display);
        imageFactory.initialize();
        this.language = yLanguageFactory.instance().createChineseLanguage();
        
        // create window:
        this.shell = new Shell(display);
        this.shell.setText(language.getTitle());

        // create default graphics:
        org.eclipse.swt.graphics.GC gc = new org.eclipse.swt.graphics.GC(shell);
        this.defaultGraphics = new y.view.yGraphics(gc);

        // now layout:
        final GridLayout gridLayoutForShell = new GridLayout();
        gridLayoutForShell.verticalSpacing = 1;
        gridLayoutForShell.marginWidth = 0;
        gridLayoutForShell.marginHeight = 0;
        gridLayoutForShell.makeColumnsEqualWidth = true;
        shell.setLayout(gridLayoutForShell);

        // create menu bar:
        createMenu();

        // create pop-up menu of "select color":
        createColorSelectMenu();

        // create tool bar - Common:
        createToolBarCommon();

        // create tool bar - Format:
        createToolBarFormat();

        // init fonts:
//        initFont();

        // create "view":
        createComposite();

        creatMouseRBtnMenu();
        
        createNavigationToolBar();

        ///////////////////////////////////////////////////////////////////////
        // ok, create the view:
        //this.view = new yTextView(this.textView);
        this.view = new yScrollableViewDecorator(new yTextView(this.textView, this),
            this.sliderH, this.sliderV);
        y.module.Document document = y.module.Document.createEmptyDocument(view);
        final yFrame frame = this;
        
        view.setSliderV(sliderV);

        view.init(document);
        //this.document.updateCaret();
        ///////////////////////////////////////////////////////////////////////

        // notify view when size changed:
        textView.addControlListener(new ControlAdapter () {
            public void controlResized(ControlEvent e) {
                // NOTE: the size is textView(Canvas):
                org.eclipse.swt.graphics.Rectangle r = textView.getBounds();
                view.onSizeChanged(r.width, r.height);
            }
        });

        // notify view when need repaint:
        textView.addPaintListener(new PaintListener () {
            public void paintControl(PaintEvent e) {
                view.update();
            }
        });

        // notify view when mouse moved:
        textView.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
                view.onMouseMove(e.x, e.y);
            }
        });

        // nofity view when mouse clicked:
        textView.addMouseListener(new MouseListener() {
            public void mouseDown(MouseEvent e) {
                if(e.button==1)
                    view.onLButtonDown(e.x, e.y);
                if(e.button==3){
                    //	view.onRButtonDown(e.x, e.y);
                		popMenu.setLocation(e.x, e.y+20*5);
                    	popMenu.setVisible(true);
                    }
            }
            public void mouseUp(MouseEvent e) {
                if(e.button==1)
                    view.onLButtonUp(e.x, e.y);
                if(e.button==3) {
                    view.onRButtonUp(e.x, e.y);
                    popMenu.setVisible(false);
                }
            }
            public void mouseDoubleClick(MouseEvent e) {
                if(e.button==1)
                    view.onLButtonDblClick(e.x, e.y);
            }
        });

        // nofity view when key pressed:
        textView.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                System.out.println(e.toString());
                boolean shift = (e.stateMask & SWT.SHIFT) == SWT.SHIFT;
                boolean ctrl = (e.stateMask & SWT.CTRL) == SWT.CTRL;
                boolean alt = (e.stateMask & SWT.ALT) == SWT.ALT;
                if(e.character!='\0') {
                    if(e.character==8 || e.character==13 || e.character==127)
                        view.onFunctionKeyPressed(e.character, shift, ctrl, alt);
                    else if(!ctrl && !alt)
                        view.onKeyPressed(e.character);
                }
                else
                    view.onFunctionKeyPressed(e.keyCode, shift, ctrl, alt);
            }
            public void keyReleased(KeyEvent e) {
                //System.out.println(e.toString());
            }
        });

        // nofity view when select font, size and color:
        cmbFontName.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println(e.toString());
                view.onFormatChanged(cmbFontName.getText(),
                    null, null, null, null, null
                );
            }
        });
        cmbFontSize.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println(e.toString());
                view.onFormatChanged(null,
                    Integer.valueOf(cmbFontSize.getText()),
                    null, null, null, null
                );
            }
        });

        for(int i=0; i<16; i++) {
            mnuColor[i].addSelectionListener(new SelectColorHandler(view, i));
        }
        
        mnuFileNew.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println(e.toString());
                view.onNewButtonClick();
            }
        });
        
        mnuFileSave.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println(e.toString());
                view.onSaveButtonClick();
            }
        });
        
        mnuFileSaveAs.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println(e.toString());
                view.onSaveAsButtonClick();
            }
        });
        
        mnuFileOpen.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println(e.toString());
                view.onOpenButtonClick();
            }
        });
        
        mnuFileExit.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println(e.toString());
                view.onExitButtonClick();
            }
        });
        
        mnuEditUndo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println(e.toString());
                view.onUndoButtonClick();
            }
        });
        
        mnuEditRedo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println(e.toString());
                view.onRedoButtonClick();
            }
        });
        
        engLanguageMenu.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
        		int selection = cmbFontName.getSelectionIndex();
        		language = new yEnglishLanguage();
        		tbLanguageChange.setText(language.getEngLanguageLabel());
        		engLanguageMenu.setText(language.getEngLanguageLabel());
        		chLanguageMenu.setText(language.getCHLanguageLabel());
        		CommandManager.instance().newChangeLanguageCommand(frame, language);
        		cmbFontName.select(selection);
            	//view.onRedoButtonClick();
            }
        });
        
        chLanguageMenu.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
        		int selection = cmbFontName.getSelectionIndex();
        		language = new yChineseLanguage();
        		tbLanguageChange.setText(language.getCHLanguageLabel());
        		engLanguageMenu.setText(language.getEngLanguageLabel());
        		chLanguageMenu.setText(language.getCHLanguageLabel());
        		CommandManager.instance().newChangeLanguageCommand(frame, language);
        		cmbFontName.select(selection);
            	//view.onRedoButtonClick();
            }
        });
        
        tbNew.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println(e.toString());
                view.onNewButtonClick();
            }
        });
        
        tbOpen.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println(e.toString());
                view.onOpenButtonClick();
            }
        });
        
        tbSave.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println(e.toString());
                view.onSaveButtonClick();
            }
        });
        
        tbUndo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println(e.toString());
                view.onUndoButtonClick();
            }
        });

        tbRedo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println(e.toString());
                view.onRedoButtonClick();
            }
        });
        
        redoPopBtn.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	System.out.println("onRedoButtonClick");
            	view.onRedoButtonClick();
            }
        });
        
        undoPopBtn.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	System.out.println("onUndoButtonClick");
            	view.onUndoButtonClick();
            }
        });
        
        newFilePopBtn.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	System.out.println("onNewButtonClick");
            	view.onNewButtonClick();
            }
        });
        
        openFilePopBtn.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	System.out.println("onOpenButtonClick");
            	view.onOpenButtonClick();
            }
        });
        
        saveFilePopBtn.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	System.out.println("onSaveButtonClick");
            	view.onSaveButtonClick();
            }
        });
        
        
    }


    /**
     * Ready to show the window and run message loop. 
     * 
     * @see y.view.ui.Frame#show()
     */
    public void show() {
        shell.pack();
        shell.open();
        this.textView.setFocus();

        while(!shell.isDisposed())
            if(!display.readAndDispatch())
                display.sleep();
    }

    /**
     * Clean up. 
     * 
     * @see y.view.ui.Frame#dispose()
     */
    public void dispose() {
        // dispose fonts & colors:
        yFontFactory.instance().clearAllFonts();
        yColorFactory.instance().clearAllColors();

        // dispose the display:
        this.display.dispose();
    }

    // create menu bar:
    private void createMenu() {
        Menu menubar = new Menu(shell, SWT.BAR);

        // File menu:
        mnuFile = new MenuItem(menubar, SWT.CASCADE);
        mnuFile.setText(language.getFileLabel());

        Menu popupmenu = new Menu(mnuFile);
        mnuFile.setMenu(popupmenu);

        mnuFileNew = new MenuItem(popupmenu, SWT.NONE);
        mnuFileNew.setEnabled(true);
        mnuFileNew.setText(language.getNewDocumentLabel());

        mnuFileOpen = new MenuItem(popupmenu, SWT.NONE);
        mnuFileOpen.setEnabled(true);
        mnuFileOpen.setText(language.getOpenLabel());

//        mnuFileClose = new MenuItem(popupmenu, SWT.NONE);
//        mnuFileClose.setEnabled(true);
//        mnuFileClose.setText(language.getCloseLabel());

        new MenuItem(popupmenu, SWT.SEPARATOR);

        mnuFileSave = new MenuItem(popupmenu, SWT.NONE);
        mnuFileSave.setEnabled(true);
        mnuFileSave.setText(language.getSaveDocumentLabel());

        mnuFileSaveAs = new MenuItem(popupmenu, SWT.NONE);
        mnuFileSaveAs.setEnabled(true);
        mnuFileSaveAs.setText(language.getSaveAsLabel());

        new MenuItem(popupmenu, SWT.SEPARATOR);

        mnuFileExit = new MenuItem(popupmenu, SWT.NONE);
        mnuFileExit.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });
        mnuFileExit.setText(language.getExitLabel());

        // Edit menu:
        mnuEdit = new MenuItem(menubar, SWT.CASCADE);
        mnuEdit.setText(language.getEditLabel());

        Menu popupmenu_1 = new Menu(mnuEdit);
        mnuEdit.setMenu(popupmenu_1);

        mnuEditUndo = new MenuItem(popupmenu_1, SWT.NONE);
        mnuEditUndo.setEnabled(true);
        mnuEditUndo.setText(language.getUndoLabel());

        mnuEditRedo = new MenuItem(popupmenu_1, SWT.NONE);
        mnuEditRedo.setEnabled(true);
        mnuEditRedo.setText(language.getRedoLabel());

        new MenuItem(popupmenu_1, SWT.SEPARATOR);

        mnuEditDelete = new MenuItem(popupmenu_1, SWT.NONE);
        mnuEditDelete.setEnabled(true);
        mnuEditDelete.setText(language.getDeleteLabel());

        // Insert menu:
        mnuInsert = new MenuItem(menubar, SWT.CASCADE);
        mnuInsert.setText(language.getInsertLabel());

        Menu popupmenu_3 = new Menu(mnuInsert);
        mnuInsert.setMenu(popupmenu_3);

        mnuInsertPicture = new MenuItem(popupmenu_3, SWT.CASCADE);
        mnuInsertPicture.setText(language.getInsertPictureLabel());

        Menu popupmenu_6 = new Menu(mnuInsertPicture);
        mnuInsertPicture.setMenu(popupmenu_6);

        mnuInsertPictureFromFile = new MenuItem(popupmenu_6, SWT.NONE);
        mnuInsertPictureFromFile.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog (shell, SWT.OPEN);
            	dialog.setFilterNames (language.getFilterNames());
            	dialog.setFilterExtensions (new String [] {"*.bmp", "*.*"});
            	String filename = dialog.open();
            	if(filename!=null)
            	    view.onInsertPictureFromFile(filename);
            }
        });
        mnuInsertPictureFromFile.setText(language.getPictureFromFileLabel());


        // Help menu:
        mnuHelp = new MenuItem(menubar, SWT.CASCADE);
        mnuHelp.setText(language.getHelpLabel());

        Menu popupmenu_8 = new Menu(mnuHelp);
        mnuHelp.setMenu(popupmenu_8);

        mnuHelpContactAuthor = new MenuItem(popupmenu_8, SWT.NONE);
        mnuHelpContactAuthor.setText(language.getContactAuthorLabel());
        mnuHelpContactAuthor.setEnabled(false);

        new MenuItem(popupmenu_8, SWT.SEPARATOR);

        mnuHelpAbout = new MenuItem(popupmenu_8, SWT.NONE);
        mnuHelpAbout.setText(language.getAboutLabel());
        mnuHelpAbout.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                Shell dialog = new Shell (shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
                dialog.setText (language.getAboutViewTitle());
                dialog.setSize (200, 200);
                dialog.open ();
            }
        });

        shell.setMenuBar(menubar);
    }

    // create tool bar - Common:
    private void createToolBarCommon() {
        Image img = null;

        toolBarCommon = new ToolBar(shell, SWT.FLAT);
        toolBarCommon.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        tbNew = new ToolItem(toolBarCommon, SWT.PUSH);
        tbNew.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println(e.toString());
            }
        });
        tbNew.setToolTipText(language.getNewToolTip());
        img = this.imageFactory.getImage("file_newIcon");
        if(img!=null) tbNew.setImage(img);
        else tbNew.setText("New");

        tbOpen = new ToolItem(toolBarCommon, SWT.PUSH);
        tbOpen.setToolTipText(language.getOpenToolTip());
        img = this.imageFactory.getImage("file_openIcon");
        if(img!=null) tbOpen.setImage(img);
        else tbOpen.setText("Open");

        tbSave = new ToolItem(toolBarCommon, SWT.PUSH);
        tbSave.setToolTipText(language.getSaveToolTip());
        img = this.imageFactory.getImage("file_saveIcon");
        if(img!=null) tbSave.setImage(img);
        else tbSave.setText("Save");

//        final ToolItem toolItem_2 = new ToolItem(toolBarCommon, SWT.SEPARATOR);

//        final ToolItem toolItem_5 = new ToolItem(toolBarCommon, SWT.SEPARATOR);

        tbUndo = new ToolItem(toolBarCommon, SWT.PUSH);
        tbUndo.setToolTipText(language.getUndoToolTip());
        img = this.imageFactory.getImage("undoIcon");
        if(img!=null) tbUndo.setImage(img);
        else tbUndo.setText("Undo");

        tbRedo = new ToolItem(toolBarCommon, SWT.PUSH);
        tbRedo.setToolTipText(language.getRedoToolTip());
        img = this.imageFactory.getImage("redoIcon");
        if(img!=null) tbRedo.setImage(img);
        else tbRedo.setText("Redo");
        
        new ToolItem(toolBarCommon, SWT.SEPARATOR);
        
        languageMenu = new Menu (shell, SWT.POP_UP);
        engLanguageMenu = new MenuItem(languageMenu , SWT.PUSH);
        engLanguageMenu.setText(language.getEngLanguageLabel());
        chLanguageMenu = new MenuItem(languageMenu , SWT.PUSH);
        chLanguageMenu.setText(language.getCHLanguageLabel());
        languageMenu.setDefaultItem(chLanguageMenu);
        
        tbLanguageChange = new ToolItem(toolBarCommon , SWT.DROP_DOWN);
        tbLanguageChange.setText(language.getCHLanguageLabel());
        
        tbLanguageChange.addListener (SWT.Selection, new Listener () {
            public void handleEvent (Event event) {
                if (event.detail == SWT.ARROW) {
                    Rectangle rect = tbLanguageChange.getBounds ();
                    Point pt = new Point (rect.x, rect.y + rect.height);
                    pt = toolBarCommon.toDisplay (pt);
                    languageMenu.setLocation (pt.x, pt.y);
                    languageMenu.setVisible (true);
                }
            }
        });
        
        tbLanguageChange.setWidth(50);
   //     toolBarCommon.setLayout(new GridLayout(2,true));
        toolBarCommon.pack();
    }

    // create pop-up menu of "select color":
    private void createColorSelectMenu() {
        String[] color_names = language.getColorNames();

        mnuColorSelect = new Menu (shell, SWT.POP_UP);
        Image image = null;

        for(int i=0; i<16; i++) {
            mnuColor[i] = new MenuItem(mnuColorSelect, SWT.PUSH);
            mnuColor[i].setText(color_names[i]);
            image = imageFactory.getImage("color"+i);
            if(image!=null)
                mnuColor[i].setImage(image);
        }
    }

    // create tool bar - Format:
    private void createToolBarFormat() {
        Image img = null;

        toolBarFormat = new ToolBar(shell, SWT.FLAT);
        toolBarFormat.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final ToolItem tbFontName = new ToolItem(toolBarFormat, SWT.SEPARATOR);

        final ToolItem tbFontSize = new ToolItem(toolBarFormat, SWT.SEPARATOR);

        final ToolItem tbBold = new ToolItem(toolBarFormat, SWT.CHECK);
        tbBold.addListener (SWT.Selection, new Listener () {
            public void handleEvent (Event event) {
                view.onFormatChanged(null, null,
                    tbBold.getSelection()? Boolean.TRUE : Boolean.FALSE,
                    null, null, null);
            }
        });
        img = this.imageFactory.getImage("boldIcon");
        if(img!=null) tbBold.setImage(img);
        else tbBold.setText("B");

        final ToolItem tbItalic = new ToolItem(toolBarFormat, SWT.CHECK);
//        tbItalic.setEnabled(false);
        
        tbItalic.addListener (SWT.Selection, new Listener () {
            public void handleEvent (Event event) {
                view.onFormatChanged(null, null,null,
                	tbItalic.getSelection()? Boolean.TRUE : Boolean.FALSE,
                    null, null);
            }
        });
        img = this.imageFactory.getImage("italicsIcon");
        if(img!=null) tbItalic.setImage(img);
        else tbItalic.setText("I");

//        final ToolItem tbUnderlined = new ToolItem(toolBarFormat, SWT.CHECK);
//        tbUnderlined.setEnabled(false);
//        img = this.imageFactory.loadToolbarImage("underlined");
//        if(img!=null) tbUnderlined.setImage(img);
//        else tbUnderlined.setText("U");
//
//        new ToolItem(toolBarFormat, SWT.SEPARATOR);
//
//        final ToolItem tbAlignLeft = new ToolItem(toolBarFormat, SWT.RADIO);
//        tbAlignLeft.setSelection(true);
//        img = this.imageFactory.loadToolbarImage("left");
//        if(img!=null) tbAlignLeft.setImage(img);
//        else tbAlignLeft.setText("left");
//
//        final ToolItem tbAlignCenter = new ToolItem(toolBarFormat, SWT.RADIO);
//        tbAlignCenter.setEnabled(false);
//        img = this.imageFactory.loadToolbarImage("center");
//        if(img!=null) tbAlignCenter.setImage(img);
//        else tbAlignCenter.setText("center");
//
//        final ToolItem tbAlignRight = new ToolItem(toolBarFormat, SWT.RADIO);
//        tbAlignRight.setEnabled(false);
//        img = this.imageFactory.loadToolbarImage("right");
//        if(img!=null) tbAlignRight.setImage(img);
//        else tbAlignRight.setText("right");
//
//        new ToolItem(toolBarFormat, SWT.SEPARATOR);
//
//        final ToolItem tbIndentLeft = new ToolItem(toolBarFormat, SWT.PUSH);
//        tbIndentLeft.setEnabled(false);
//        img = this.imageFactory.loadToolbarImage("indent_left");
//        if(img!=null) tbIndentLeft.setImage(img);
//        else tbIndentLeft.setText("<-");
//
//        final ToolItem tbIndentRight = new ToolItem(toolBarFormat, SWT.PUSH);
//        tbIndentRight.setEnabled(false);
//        img = this.imageFactory.loadToolbarImage("indent_right");
//        if(img!=null) tbIndentRight.setImage(img);
//        else tbIndentRight.setText("->");

        new ToolItem(toolBarFormat, SWT.SEPARATOR);

        final ToolItem tbColor = new ToolItem(toolBarFormat, SWT.DROP_DOWN);
        img = this.imageFactory.getImage("fontIcon");
        if(img!=null) tbColor.setImage(img);
        else tbColor.setText("Color");
        tbColor.addListener (SWT.Selection, new Listener () {
            public void handleEvent (Event event) {
                if (event.detail == SWT.ARROW) {
                    Rectangle rect = tbColor.getBounds ();
                    Point pt = new Point (rect.x, rect.y + rect.height);
                    pt = toolBarFormat.toDisplay (pt);
                    mnuColorSelect.setLocation (pt.x, pt.y);
                    mnuColorSelect.setVisible (true);
                }
            }
        });
        new ToolItem(toolBarFormat, SWT.SEPARATOR);
        
        final ToolItem tbTitleFormatA = new ToolItem(toolBarFormat, SWT.PUSH);
        tbTitleFormatA.addListener (SWT.Selection, new Listener () {
            public void handleEvent (Event event) {
                view.onFormatChanged(null, 48,
                    true,
                    null, null, null);
            }
        });
        
        img = this.imageFactory.getImage("titleFormatIcon");
        if(img!=null) tbTitleFormatA.setImage(img);
        else tbTitleFormatA.setText("T");
        
new ToolItem(toolBarFormat, SWT.SEPARATOR);
        
        final ToolItem tbInsertPicture = new ToolItem(toolBarFormat, SWT.PUSH);
        tbInsertPicture.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog (shell, SWT.OPEN);
            	dialog.setFilterNames (language.getFilterNames());
            	dialog.setFilterExtensions (new String [] {"*.bmp", "*.*"});
            	String filename = dialog.open();
            	if(filename!=null)
            	    view.onInsertPictureFromFile(filename);
            }
        });
        
        img = this.imageFactory.getImage("pictureIcon");
        if(img!=null) tbInsertPicture.setImage(img);
        else tbInsertPicture.setText("P");
        
        // font name list:
        cmbFontName = new Combo(toolBarFormat, SWT.READ_ONLY);
        initFont();
        // place it on the tool bar:
        cmbFontName.pack();
        cmbFontName.select(0);
        tbFontName.setWidth(140);
        tbFontName.setControl(cmbFontName);

        // font size list:
        cmbFontSize = new Combo(toolBarFormat, SWT.READ_ONLY);
        cmbFontSize.setItems(new String[] { "6", "8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72" });
        cmbFontSize.select(5);

        // place it on the tool bar:
        cmbFontSize.pack();
        tbFontSize.setWidth(50);
        tbFontSize.setControl(cmbFontSize);

        toolBarFormat.pack();
    }

    // enumerate all fonts:
    private void initFont() {
        String[] fonts = language.getFontNames();
        cmbFontName.setItems(fonts);
    }

    // create "view":
    private void createComposite() {
        // scrollable view:
        ScrollableView = new Composite(shell, SWT.NONE);
        final GridLayout gridLayoutForScrollableView = new GridLayout();
        gridLayoutForScrollableView.horizontalSpacing = 0;
        gridLayoutForScrollableView.verticalSpacing = 0;
        gridLayoutForScrollableView.marginWidth = 0;
        gridLayoutForScrollableView.marginHeight = 0;
        gridLayoutForScrollableView.numColumns = 2;
        ScrollableView.setLayout(gridLayoutForScrollableView);
        ScrollableView.setLayoutData(new GridData(GridData.FILL_BOTH));

        // text view:
        textView = new Canvas(ScrollableView, SWT.NONE);
        textView.setLayoutData(new GridData(GridData.FILL_BOTH));
        textView.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));

        sliderV = new Slider(ScrollableView, SWT.VERTICAL);
        sliderV.setLayoutData(new GridData(GridData.FILL_VERTICAL));

        sliderH = new Slider(ScrollableView, SWT.NONE);
        sliderH.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));

        // IMPORTANT:
        // add listener:
        textView.addListener(SWT.KeyDown, new Listener () {
            public void handleEvent(Event event) {
                //
            }
        });
        textView.addListener (SWT.MouseDown, new Listener () {
            public void handleEvent(Event event) {
            }
        });

        // and set caret:
        caret = new Caret(textView, 0);
        caret.setBounds(1,1,2,24); // TODO:
        textView.setCaret(caret);
    }
    public void createNavigationToolBar(){
    	toolBarNavigation = new ToolBar(shell, SWT.FLAT);
        toolBarNavigation.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
     
        
        pagePre = new ToolItem(toolBarNavigation , SWT.NORMAL);
        pagePre.addListener (SWT.Selection, new Listener () {
            public void handleEvent (Event event) {
            	int currentPage = view.getPageCurrent();
            	if(currentPage >= 1){
            		view.setPageNavigation(currentPage-1);
            	}
            }
        });
        pagePre.setText(language.getPrePage());
        
        pageNext = new ToolItem(toolBarNavigation , SWT.NORMAL);
        pageNext.addListener (SWT.Selection, new Listener () {
            public void handleEvent (Event event) {
            	int currentPage = view.getPageCurrent();
            	if(currentPage <= navigationPage.size()-2){
            		view.setPageNavigation(currentPage+1);
            	}
                
            }
        });
        pageNext.setText(language.getNextPage());

        
        navigationPage.add(pageNext);
        navigationPage.add(pagePre);
    }
    
    /**
     * Get the default graphics. DO NOT dispose it after use!!! 
     * 
     * @see y.view.ui.Frame#getDefaultGraphics()
     */
    public yGraphics getDefaultGraphics() {
        return this.defaultGraphics;
    }

    /**
     * Get the view. 
     * 
     * @see y.view.ui.Frame#getView()
     */
    public y.view.yView getView() {
        return this.view;
    }
    
    public yLanguage getLanguage() {
    	return this.language;
    }
    /**
     * change the language of view
     */
    public void changeLanguage(yLanguage language) {
    	shell.setText(language.getTitle());
    	mnuFile.setText(language.getFileLabel());
    	mnuFileNew.setText(language.getFileLabel());
    	mnuFileOpen.setText(language.getOpenLabel());
//    	mnuFileClose.setText(language.getCloseLabel());
    	mnuFileSave.setText(language.getSaveDocumentLabel());
    	mnuFileSaveAs.setText(language.getSaveAsLabel());
    	mnuFileExit.setText(language.getExitLabel());
    	mnuEdit.setText(language.getEditLabel());
    	mnuEditUndo.setText(language.getUndoLabel());
    	mnuEditRedo.setText(language.getRedoLabel());
    	mnuEditDelete.setText(language.getDeleteLabel());
    	mnuInsert.setText(language.getInsertLabel());
    	mnuInsertPicture.setText(language.getInsertPictureLabel());
    	mnuInsertPictureFromFile.setText(language.getPictureFromFileLabel());

        mnuHelp.setText(language.getHelpLabel());
        mnuHelpContactAuthor.setText(language.getContactAuthorLabel());
        mnuHelpAbout.setText(language.getAboutLabel());
        
    	String[] color_names = language.getColorNames();
    	for(int i=0; i<16; i++) {
    	    mnuColor[i].setText(color_names[i]);
    	}
    	
    	String[] fonts = language.getFontNames();
    	cmbFontName.setItems(fonts);
    }
    
    public void creatMouseRBtnMenu(){
    	popMenu = new Menu(shell , SWT.POP_UP);
    	redoPopBtn = new MenuItem(popMenu , SWT.PUSH);
    	redoPopBtn.setText(language.getRedoLabel());
    	undoPopBtn = new MenuItem(popMenu , SWT.PUSH);
    	undoPopBtn.setText(language.getUndoLabel());
    	newFilePopBtn = new MenuItem(popMenu , SWT.PUSH);
    	newFilePopBtn.setText(language.getNewDocumentLabel());
    	openFilePopBtn = new MenuItem(popMenu , SWT.PUSH);
    	openFilePopBtn.setText(language.getOpenLabel());
    	saveFilePopBtn = new MenuItem(popMenu , SWT.PUSH);
    	saveFilePopBtn.setText(language.getSaveDocumentLabel());
    }
    
    public void changePageNavigation(){
    	
    	int pageCounts = view.pageChangedNotif()+2;
    	
    	if(navigationPage.size() >= pageCounts){
    		for(int i=navigationPage.size()-1 ; i>=pageCounts ; i--){
    			navigationPage.remove(i);
    		}
    	}else{
    		for(int i=navigationPage.size(); i<pageCounts ; i++){
    			final ToolItem _page = new ToolItem(toolBarNavigation , SWT.NORMAL);
    	    	_page.addListener (SWT.Selection, new Listener () {
    	            public void handleEvent (Event event) {
    	                view.setPageNavigation(Integer.parseInt(_page.getText())-1);
    	            }
    	        });
    	    	_page.setText((i-1+""));
    			navigationPage.add(_page);
    		}
    	}
    }
    
    public boolean isPageChanged(){
    	return !(navigationPage.size() == (view.pageChangedNotif()+ 2));
    }
}

class SelectColorHandler extends SelectionAdapter {

    private static int[] COLORS = {
        0x000000, 0x800000, 0x008000, 0x808000,
        0x000080, 0x800080, 0x008080, 0x808080,
        0xc0c0c0, 0xff0000, 0x00ff00, 0xffff00,
        0x0000ff, 0xff00ff, 0x00ffff, 0xffffff
    };

    private yView view;
    private int index;
    private yColor color;

    public SelectColorHandler(y.view.yView yView, int index) {
        this.view = yView;
        this.index = index;
        this.color = yColorFactory.instance().createColor(COLORS[index]);
    }

    // when select color:
    public void widgetSelected(SelectionEvent e) {
        view.onFormatChanged(null, null, null, null, null, color);
    }
}
