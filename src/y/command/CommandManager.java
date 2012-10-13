/*
 * Created on 2011-7-4
 * Author: y&y, Copyright (C) 2011, y&y.
 */
package y.command;

import java.util.*;

import y.module.*;
import y.view.*;



/**
 * Command manager manage all commands that can undo and redo. 
 * 
 * @author y&y
 */
public final class CommandManager {

    private static CommandManager instance = new CommandManager();

    // store the command list:
    private List<Command> commands = new ArrayList<Command>();
    // store the position of the current command (just executed):
    private int current = (-1);
    
    //used to indicate where undo has been done
    private boolean hasBeenUndo = false;

    // prevent the client to create instance:
    private CommandManager() {}

    protected void clear() {
    	commands.clear();
    	current = -1;
    	hasBeenUndo = false;
    }
    /**
     * Get the only instance of the CommandManager. 
     * 
     * @return The instance of the CommandManager.
     */
    public static CommandManager instance() { return instance; }

    // add the command that just executed:
    private void addToCommandList(Command cmd) {
        Assert.checkTrue(cmd.canUndo()); // must support undo.

        if(hasBeenUndo) {
        	int size = commands.size() - (current + 1);
        	for(int i = 0; i < size; i++) {
        		commands.remove(current + 1);
        	}
//        	
//        	commands.retainAll(commands.subList(0, current + 1));
//            hasBeenUndo = false;
        }
        commands.add(cmd);
        current++;
        System.out.println("added a new command: "+cmd.toString());
    }

    /**
     * Undo the last command. 
     */
    public void undo() {
        Assert.checkTrue(canUndo());
        if(!canUndo()) {
        	return;
        }
        Command currentCommand = commands.get(current);
        if(currentCommand.canUndo()) {
        	currentCommand.unexecute();
        	current--;
        }
        hasBeenUndo = true;
        System.out.println(current);
    }

    /**
     * Can redo the last undo command? 
     * 
     * @return True if can redo.
     */
    public boolean canRedo() {
        return current<commands.size()-1;
    }

    /**
     * Redo the last cancelled command. 
     */
    public void redo() {
        Assert.checkTrue(canRedo());
        if(!canRedo()) {
        	return;
        }
        Command currentCommand = commands.get(current + 1);
    	currentCommand.execute();
    	current++;
        System.out.println(current);
    }

    /**
     * Can undo the last command? 
     * 
     * @return True if can undo.
     */
    public boolean canUndo() {
        return current>=0;
    }

    /**
     * Create a new insert command and execute it. 
     * 
     * @param doc The document object.
     * @param c The char of the key.
     */
    public void newInsertCommand(Document doc, char c) {
        Command cmd = new InsertCommand(doc, c);
        if(cmd.execute() && cmd.canUndo()) {
            addToCommandList(cmd);
        }
    }

    /**
     * Create a new insert picture command and execute it. 
     * 
     * @param doc The document object.
     * @param filename The picture file name.
     */
    public void newInsertPictureCommand(Document doc, String filename) {
        Command cmd = new InsertPictureCommand(doc, filename);
        if(cmd.execute() && cmd.canUndo()) {
            addToCommandList(cmd);
        }
    }

    /**
     * Create a new format command and execute it. 
     * 
     * @param doc The document object.
     * @param fontName The font name, or null if ignore.
     * @param fontSize The font size, or null if ignore.
     * @param bold The bold attribute, or null if ignore.
     * @param italic The italic attribute, or null if ignore.
     * @param underlined The underlined attribute, or null if ignore.
     * @param color The color, or null if ignore.
     */
    public void newFormatCommand(Document doc, String fontName, Integer fontSize, Boolean bold, Boolean italic, Boolean underlined, yColor color) {
        Command cmd = new FormatCommand(doc, fontName, fontSize, bold, italic, underlined, color);
        if(cmd.execute() && cmd.canUndo()) {
            addToCommandList(cmd);
        }
    }

    /**
     * Create a new delete command and execute it. 
     * 
     * @param doc The document object.
     */
    public void newDeleteCommand(Document doc) {
        Command cmd = new DeleteCommand(doc);
        if(cmd.execute() && cmd.canUndo()) {
            addToCommandList(cmd);
        }
    }

    /**
     * Create a new break command and execute it. 
     * 
     * @param doc The document object.
     */
    public void newSplitCommand(Document doc) {
        Command cmd = new SplitCommand(doc);
        if(cmd.execute() && cmd.canUndo()) {
            addToCommandList(cmd);
        }
    }
    
    /**
     * Create a new combine command and execute it. 
     * 
     * @param doc The document object.
     * @param para The paragraph object.
     */
    public void newCombineCommand(Document doc, Paragraph para) {
        Command cmd = new CombineCommand(doc, para);
        if(cmd.execute() && cmd.canUndo()) {
            addToCommandList(cmd);
        }
    }
    
    /**
     * Create a new save command and execute it. 
     * 
     * @param doc The document object.
     * @param filePath The path.
     */
    public void newSaveCommand(Document doc) {
        Command cmd = new SaveCommand(doc);
        if(cmd.execute() && cmd.canUndo()) {
            addToCommandList(cmd);
        }
    }
    
    /**
     * Create a new save command and execute it. 
     * 
     * @param doc The document object.
     * @param filePath The path.
     */
    public void newSaveAsCommand(Document doc, String filePath) {
        Command cmd = new SaveAsCommand(doc, filePath);
        if(cmd.execute() && cmd.canUndo()) {
            addToCommandList(cmd);
        }
    }
    
    /**
     * Create a new open command and execute it. 
     * 
     * @param doc current document
     * @param filePath The path.
     */
    public void newOpenCommand(yView view, String filePath) {
        Command cmd = new OpenCommand(view, filePath);
        if(cmd.execute() && cmd.canUndo()) {
            addToCommandList(cmd);
        }
    }
    
    /**
     * Create a new change language command and execute it. 
     * 
     * @param doc current document
     * @param filePath The path.
     */
    public void newChangeLanguageCommand(yFrame frame, yLanguage language) {
        Command cmd = new ChangeLanguageCommand(frame, language);
        if(cmd.execute() && cmd.canUndo()) {
            addToCommandList(cmd);
        }
    }
    
    /**
     * Create a new document command and execute it. 
     * 
     * @param doc current document
     * @param filePath The path.
     */
    public void newNewCommand(yView view) {
        Command cmd = new NewCommand(view, this);
        if(cmd.execute() && cmd.canUndo()) {
            addToCommandList(cmd);
        }
    }
}
