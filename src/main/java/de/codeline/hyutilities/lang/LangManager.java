package de.codeline.hyutilities.lang;

import com.google.gson.JsonArray;
import com.membercat.issuelib.api.wrapper.ConfigElement;
import com.membercat.issuelib.api.wrapper.ConfigLoader;
import com.membercat.issuelib.api.wrapper.ConfigSection;
import com.membercat.issuelib.api.wrapper.exception.ConfigLoadException;
import de.codeline.hyutilities.HyUtilities;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LangManager {

    private final Map<String, String> translations;

    private final ConfigLoader<?> loader;

    private final Path langFolderPath;

    public LangManager(ConfigLoader<?> loader, Path langFolderPath) {
         this.translations = new HashMap<>();
         this.loader = loader;
         this.langFolderPath = langFolderPath;
    }

    public void reload(String language) {
        ConfigSection section = this.loadFile(language);
        if (section == null) throw new RuntimeException("There was an error loading the language file for language %s!".formatted(language));
        translations.clear();
        for (String key : section.getKeys()) translations.put(key, Objects.requireNonNull(section.get(key)).toString());
    }

    public ConfigSection loadFile(String language) {
        File langFile = langFolderPath.resolve(language + ".yml").toFile();
        if (!langFile.exists()) return null;
        ConfigElement rootElement;
        try {
            rootElement = this.loader.load(langFile);
        } catch (ConfigLoadException e) {
            return null;
        }
        return rootElement instanceof ConfigSection section ? section : null;
    }

    public String get(String key) {
        return translations.getOrDefault(key, key); // returns key if translation not found
    }

}
