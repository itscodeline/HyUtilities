package de.codeline.hyutilities;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.membercat.issuelib.IssueLib;
import com.membercat.issuelib.api.config.ConfigurationHolder;
import com.membercat.issuelib.api.config.InitializationException;
import com.membercat.issuelib.api.config.IssuesFoundException;
import de.codeline.hyutilities.config.Namespace;
import de.codeline.hyutilities.config.PluginConfig;
import de.codeline.hyutilities.lang.LangManager;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Set;

/**
 * This class serves as the entrypoint for your plugin. Use the setup method to register into game registries or add
 * event listeners.
 */
public class HyUtilities extends JavaPlugin {

    @Nonnull
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Nonnull
    private final static Set<String> LANGUAGES = Set.of("en_US", "de_DE");

    private ConfigurationHolder<PluginConfig> mainConfigHolder;

    private LangManager langManager;

    public HyUtilities(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Setting up plugin " + this.getName());

        try {
            createConfigFiles(this.getDataDirectory());
            mainConfigHolder = ConfigurationHolder.getInstance(PluginConfig.class, Namespace.INSTANCE);
        } catch (IOException | InitializationException e) {
            throw new RuntimeException(e);
        }

        langManager = new LangManager(IssueLib.snakeYamlLoader(), getDataDirectory().resolve("lang"));
        langManager.reload("en_US");

        reloadConfig();

        langManager.reload(getMainConfig().general.language);

        LOGGER.atInfo().log("Cats: " + langManager.get("test"));

        this.getCommandRegistry().registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));
    }

    @Override
    protected void start() {
        LOGGER.atInfo().log("The plugin %s was started!".formatted(this.getName()));
    }

    @Override
    protected void shutdown() {
        LOGGER.atInfo().log("The plugin %s is shutting down!".formatted(this.getName()));
    }

    private void createConfigFiles(Path rootPath) throws IOException {
        boolean rootFolderCreated = rootPath.toFile().mkdirs();

        Path generalConfigPath = rootPath.resolve("config.yml");
        copyDefaultConfig(generalConfigPath, "configs/config.yml");

        Path langFolderPath = rootPath.resolve("lang");
        boolean langFolderCreated = langFolderPath.toFile().mkdirs();
        if (!langFolderCreated) return;
        for (String lang : LANGUAGES) {
            copyDefaultConfig(langFolderPath.resolve(lang + ".yml"), "lang/%s.yml".formatted(lang));
        }
    }

    public void copyDefaultConfig(Path path, String defaultPath) throws IOException {
        if (Files.exists(path)) return;
        InputStream stream = getClassLoader().getResourceAsStream(defaultPath);
        if (stream == null) throw new IOException("DefaultConfig doesn't exist.");
        Files.write(path, stream.readAllBytes(), StandardOpenOption.CREATE_NEW);
        stream.close();
    }

    public PluginConfig getMainConfig() {
        return Objects.requireNonNull(mainConfigHolder.get());
    }

    public void reloadConfig() {
        try {
            mainConfigHolder.load(getDataDirectory().resolve("config.yml").toFile(), IssueLib.snakeYamlLoader());
        } catch (IssuesFoundException e) {
            e.printToConsole(Namespace.INSTANCE);
        }
    }

}