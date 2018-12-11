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
public enum LanguageKeys implements Lang {
    NAMEOFTHELANG("_langname"),
    GREET("greetMessage"),
    MAINCOMMANDS("mainCommands"),
    COMMAND("command"),
    QUIT("exitMessage"),
    NONSUP("nonSupportedCommand"),
    NOREC("noRecommendationFound"),
    SELECTEDCOMMANDS("selectedCommands"),
    TYPELIST("typeListings"),
    RECADDED("recommendationAdded"),
    RECNOTADDED("recommendationNotAdded"),
    SELECTIDQUERY("selectIdQuery"),
    NONVALIDID("nonValidID"),
    CONFIRMFAIL("invalidInput"),
    DELETECONFIRM("deleteConfirmation"),
    DELSUCCESS("deleteSuccess"),
    DELCANCEL("deleteCancellation"),
    ENTERNEW("enterNew"),
    ORLEAVE("orLeave"),
    NOTUP("notUpdated"),
    UPDATECONFIR("updateConfirmation"),
    UPDATEFAIL("updateFail"),
    UPDATECANCEL("updateCancel"),
    UPDATESUCCES("updateSuccess"),
    NOTFOUND("notFound"),
	NORETDEF("noRetrieveDef");

    private final String key;


    private LanguageKeys(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

}
