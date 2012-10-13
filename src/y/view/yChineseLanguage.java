package y.view;

public class yChineseLanguage implements yLanguage {

	@Override
	public String getAboutLabel() {
		return "关于 y and y ...";
	}

	@Override
	public String getAboutViewContent() {
		return "关于 y and y";
	}

	@Override
	public String getAboutViewTitle() {
		return "关于 y and y";
	}

	@Override
	public String getCloseLabel() {
		return "关闭";
	}

	@Override
	public String[] getColorNames() {
		String[] colorNames = {
	            "黑色",  "栗色",  "绿色", "橄榄色",
	            "藏青色",   "紫色",  "凫蓝",  "灰色",
	            "银色", "红色",     "淡黄绿色",  "黄色",
	            "蓝色",   "深紫红色", "水绿色",  "白色"
	        };
		return colorNames;
	}

	@Override
	public String getContactAuthorLabel() {
		return "联系作者";
	}

	@Override
	public String getDeleteLabel() {
		return "删除";
	}

	@Override
	public String getEditLabel() {
		return "编辑";
	}

	@Override
	public String getExitLabel() {
		return "退出\tAlt+F4";
	}

	@Override
	public String getFileLabel() {
		return "文件";
	}

	@Override
	public String getFontLabel() {
		return "字体...";
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
		return "帮助";
	}

	@Override
	public String getInsertLabel() {
		return "插入";
	}

	@Override
	public String getInsertPictureLabel() {
		return "插入图片";
	}

	@Override
	public String getNewDocumentLabel() {
		return "创建";
	}

	@Override
	public String getNewToolTip() {
		return "创建新的文件";
	}

	@Override
	public String getOpenLabel() {
		return "打开";
	}

	@Override
	public String getOpenToolTip() {
		return "打开新的文件";
	}

	@Override
	public String getPictureFromFileLabel() {
		return "从...打开\tCtrl+O";
	}

	@Override
	public String[] getFilterNames() {
		return new String [] {"图片文件", "所有文件(*.*)"};
	}
	
	@Override
	public String[] getExtensionsFilterNames() {
		return new String[] {"存储文件(*.yy)"};
	}
	
	@Override
	public String getRedoLabel() {
		return "重做\tCtrl+Y";
	}

	@Override
	public String getRedoToolTip() {
		return "重做前一次操作";
	}

	@Override
	public String getSaveAsLabel() {
		return "另存为...";
	}

	@Override
	public String getSaveDocumentLabel() {
		return "保存\tCtrl+S";
	}

	@Override
	public String getSaveToolTip() {
		return "保存当前文件";
	}

	@Override
	public String getUndoLabel() {
		return "撤销\tCtrl+Z";
	}

	@Override
	public String getUndoToolTip() {
		return "撤销最后一次操作";
	}

	@Override
	public String getTitle() {
		return "高级文本编辑器";
	}

	@Override
	public String getSavedFileLabel() {
		return "文件名 ";
	}

	@Override
	public String getSavedFileTitle() {
		return "另存为 ";
	}
	
	public String getTitleFormatALabel(){
		return "标题一";
	}
	
	public String getTitleFormatBLabel(){
		return "标题二";
	}
	
	public String getEngLanguageLabel(){
		return "英语";
	}
	
	public String getCHLanguageLabel(){
		return "中文";
	}
	@Override
	public String getNextPage() {
		// TODO Auto-generated method stub
		return "下一页";
	}

	@Override
	public String getPrePage() {
		// TODO Auto-generated method stub
		return "上一页";
	}
}
