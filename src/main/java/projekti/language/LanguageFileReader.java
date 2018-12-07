package projekti.language;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class LanguageFileReader {

    private final File languageFile;

    public LanguageFileReader(File languageFile) {
        this.languageFile = languageFile;
    }

    public Map<String, String> readLanguageMap() throws IOException {
        try (
                FileInputStream in = new FileInputStream(languageFile);
                InputStreamReader reader = new InputStreamReader(in)
        ) {
            return new Gson().fromJson(reader,
                    new TypeToken<Map<String, String>>() {
                    }.getType());
        }
    }
}
