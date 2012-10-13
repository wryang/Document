package y.module;

import y.format.StringFormat;
import java.io.Serializable;
public class DeletedItem{

	public StringFormat deletedStringFormat;
	public Glyph deletedGryph;
	public boolean hasDeletedStringFormat = false;
	public int indexOfDeletedGlyph = -1;
	public int indexOfDeletedSF = -1;
	
	public DeletedItem(Glyph glyph, int deletedIndex, StringFormat format, int indexOfDeletedSF) {
		this.deletedGryph = glyph;
		this.deletedStringFormat = format;
		this.indexOfDeletedGlyph = deletedIndex;
		this.indexOfDeletedSF = indexOfDeletedSF;
	}

	public DeletedItem() {
		// TODO Auto-generated constructor stub
	}
}
