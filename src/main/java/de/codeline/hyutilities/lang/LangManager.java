package de.codeline.hyutilities.lang;

import com.google.gson.JsonArray;
import com.hypixel.hytale.server.core.Message;
import com.membercat.issuelib.api.wrapper.ConfigElement;
import com.membercat.issuelib.api.wrapper.ConfigLoader;
import com.membercat.issuelib.api.wrapper.ConfigSection;
import com.membercat.issuelib.api.wrapper.exception.ConfigLoadException;
import de.codeline.hyutilities.HyUtilities;
import fi.sulku.hytale.TinyMsg;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class used to manage the plugin's multi-language functionality
 */
public class LangManager {

    private final Map<String, String> translations;

    private final ConfigLoader<?> loader;

    private final Path langFolderPath;

    public LangManager(ConfigLoader<?> loader, Path langFolderPath) {
         this.translations = new HashMap<>();
         this.loader = loader;
         this.langFolderPath = langFolderPath;
    }

    /**
     * Reload the language files contents
     * @param language Language key of the language that should be reloaded and loaded as the current language
     */
    public void reload(String language) {
        ConfigSection section = this.loadFile(language);
        if (section == null) throw new RuntimeException("There was an error loading the language file for language %s!".formatted(language));
        translations.clear();
        for (String key : section.getKeys()) translations.put(key, Objects.requireNonNull(section.get(key)).toString());
    }

    /**
     * Loads the contents of the specified language file into a {@link ConfigSection} object
     * @param language Language key of the language that will be loaded into the {@link ConfigSection} object
     * @return Returns either {@link ConfigSection} object containing the language or null, if language is not found
     */
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

    /**
     * Method used to receive a specific language key's value for the currently loaded language
     * @param key Language key defined in the loaded language file
     * @return Language key's value or language key itself it language key's value couldn't be found
     */
    public Message get(String key, Object... placeholderContent) {
        return TinyMsg.parse(translations.getOrDefault(key, key)
                .formatted(placeholderContent)
                .replace("<prefix>", translations.getOrDefault("general.prefix", "<prefix>"))); // returns key if translation not found
    }

    public String getString(String key) {
        return get(key).toString();
    }

}
