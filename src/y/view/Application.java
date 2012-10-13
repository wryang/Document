/*
 * Created on 2004-7-23
 * Author: Xuefeng, Copyright (C) 2004, Xuefeng.
 */
package y.view;


/**
 * The application object is the process of the jexi editor.
 * 
 * @author y&y
 */
public class Application {

    // store the frame:
    private yFrame frame = null;

    private String author = "y&y";
    private String email = "y&y@gmail.com";
    private String productName = "Jexi";
    private int majorVersion = 1;
    private int minorVersion = 1;
    private String build = "0726";

    // store file argument:
    private String argFile = null;

    /**
     * Get the file name from command line, or null if no file is specified. 
     * 
     * @return The file name or null if not specified.
     */
    public String getFilename() {
        return this.argFile;
    }

    private void productInfo() {
        System.out.println(productName + " version " + majorVersion + "." + minorVersion + " build " + build);
        System.out.println("Author: " + author + ", " + email);
        System.out.println("Copyright by " + author + ", 2011, all rights reserved.\n");
    }

    // the only one instance of application:
    private static Application instance = new Application();
    // prevent client to create an application:
    private Application() {}

    /**
     * Get the singleton instance of Application. 
     * 
     * @return The Application object.
     */
    public static Application instance() {
        return instance;
    }

    /**
     * The start entry of the whole application. 
     * To run the application, type: <br>
     * <code>
     *     java jexi.ui.Application [filename]
     * </code>
     * in the console. Long filename that contains 
     * space should be written as "a long file name". 
     * 
     * @param args Should be a file name or leave it empty.
     */
    public static void main(String[] args) {
        Application app = Application.instance();
        app.productInfo();

        if(args.length>1) {
            System.out.println("Usage:\n  java jexi.ui.Application [filename]");
            System.out.println("Example:\n  java jexi.ui.Application \"c:\\a.txt\"");
            System.out.println("or\n  java jexi.ui.Application");
            return;
        }
        if(args.length==1)
            app.argFile = args[0];
        app.run();
    }

    /**
     * Run the application. 
     */
    public void run() {
        //*********************************************************************
        // first initialize the frame and the view, 
        // NOTE here we specified "jexi.ui.swt.SWTFrame", 
        // but if you use other implementation such as 
        // "SwingFrame", please change the following code 
        // and re-compile again. 
        //*********************************************************************
        this.frame = new yFrame();
        // then create the document:
        // Frame's message loop:
        this.frame.init();
        this.frame.show();
        this.frame.dispose();
    }

    public void debugInitButNotShow() {
        this.frame = new yFrame();
        this.frame.init();
    }

    public void debugDisposeNotShow() {
        this.frame.dispose();
    }

    /**
     * Get the Frame object. 
     * 
     * @return The frame object.
     */
    public yFrame getFrame() {
        return this.frame;
    }
}
