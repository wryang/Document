package y.view;

public class yLanguageFactory {

	private static yLanguageFactory instance = new yLanguageFactory();
	
	private yEnglishLanguage englishLanguage;
	private yChineseLanguage chineseLanguage;
	
	private yLanguageFactory() {
		
	}
	
	public static yLanguageFactory instance() {
		return instance;
	}
	
	public yLanguage createEnglishLanguage() {
		if(englishLanguage != null) {
			return englishLanguage;
		}else {
			englishLanguage = new yEnglishLanguage();
			return englishLanguage;
		}
	}
	
	public yLanguage createChineseLanguage() {
		if(chineseLanguage != null) {
			return chineseLanguage;
		}else {
			chineseLanguage = new yChineseLanguage();
			return chineseLanguage;
		}
	}
}
