/*
 * Created on 2011-7-1
 * Author: y&y, Copyright (C) 2011, y&y.
 */
package y.module;

import java.io.Serializable;

/**
 * Compositor interface defines how and when to format a composition 
 * such as 'Paragraph' and 'document'. 
 * 
 * @author y&y
 */
public interface Compositor extends Composition, Serializable{

	/**
	 * To format a composition. (When to format)
	 */
	void compose();

	/**
	 * To set the composition. (What to format)
	 * 
     * @param composition The composition to be formatted such as "Paragraph".
	 */
    void setComposition(Composition composition);
}
