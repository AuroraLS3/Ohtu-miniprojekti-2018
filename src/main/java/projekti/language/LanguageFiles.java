/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti.language;

/**
 *
 * @author vililipo
 */
public enum LanguageFiles implements LangFile {
    ENGLISH("json/english.lang.json"),
    SUOMI("json/suomi.lang.json");
    private final String path;
    private LanguageFiles(String path) {
        this.path = path;
    }
    @Override
    public String getResourcePath() {
        return this.path;
    }
    
}
