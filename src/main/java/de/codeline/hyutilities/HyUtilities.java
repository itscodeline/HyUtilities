package de.codeline.hyutilities;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.membercat.issuelib.IssueLib;
import com.membercat.issuelib.api.config.ConfigurationHolder;
import com.membercat.issuelib.api.config.InitializationException;
import com.membercat.issuelib.api.config.IssuesFoundException;
import com.membercat.issuelib.api.internationalization.IssueListPrinter;
import de.codeline.hyutilities.commands.GamemodeCommand;
import de.codeline.hyutilities.commands.HyUtilitiesCommand;
import de.codeline.hyutilities.config.Namespace;
import de.codeline.hyutilities.config.PluginConfig;
import de.codeline.hyutilities.lang.LangManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Set;

/**
 * Main class of HyUtilities containing the starting point of the plugin
 * @author codeline
 */
public class HyUtilities extends JavaPlugin {

    @Nonnull
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Nonnull
    private final static Set<String> LANGUAGES = Set.of("en_US", "de_DE");

    private ConfigurationHolder<PluginConfig> mainConfigHolder;

    private static LangManager langManager;

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

        reloadConfig(null);

        langManager.reload(getMainConfig().general.language);

        LOGGER.atInfo().log("Cats: " + langManager.get("test"));

        this.getCommandRegistry().registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));
        if (getMainConfig().features.gamemodeCommandEnabled) this.getCommandRegistry().registerCommand(new GamemodeCommand(this));
        this.getCommandRegistry().registerCommand(new HyUtilitiesCommand(this));
    }

    @Override
    protected void start() {
        LOGGER.atInfo().log("The plugin %s was started!".formatted(this.getName()));
    }

    @Override
    protected void shutdown() {
        LOGGER.atInfo().log("The plugin %s is shutting down!".formatted(this.getName()));
    }

    /**
     * Creates config files used by the plugin
     * @param rootPath Plugin specific config folder found, usually found in 'mods'
     * @throws IOException Exception thrown if creation of config files or folders causes issues
     */
    public void createConfigFiles(Path rootPath) throws IOException {
        boolean rootFolderCreated = rootPath.toFile().mkdirs();

        Path generalConfigPath = rootPath.resolve("config.yml");
        copyDefaultConfig(generalConfigPath, "configs/config.yml");

        Path langFolderPath = rootPath.resolve("lang");
        boolean langFolderCreated = langFolderPath.toFile().mkdirs();
        if (!langFolderCreated && langFolderPath.resolve("en_US.yml").toFile().exists()) return;
        for (String lang : LANGUAGES) {
            copyDefaultConfig(langFolderPath.resolve(lang + ".yml"), "lang/%s.yml".formatted(lang));
        }
    }

    /**
     * Copies the plugin internal default config files, usually copied to 'mods/user_pluginName'
     * @param path Path for the config file to be copied to
     * @param defaultPath Relative internal path to the config file to be copied
     * @throws IOException Exception thrown if copying of default config files causes issues
     */
    public void copyDefaultConfig(Path path, String defaultPath) throws IOException {
        if (Files.exists(path)) return;
        InputStream stream = getClassLoader().getResourceAsStream(defaultPath);
        if (stream == null) throw new IOException("DefaultConfig doesn't exist.");
        Files.write(path, stream.readAllBytes(), StandardOpenOption.CREATE_NEW);
        stream.close();
    }

    /**
     * Getter to receive PluginConfig object for the main config
     * @return {@link PluginConfig} object of plugin's main config (typically 'config.yml')
     */
    public PluginConfig getMainConfig() {
        return Objects.requireNonNull(mainConfigHolder.get());
    }

    /**
     * Getter to receive LangManager object for language management
     * @return {@link LangManager} object managing plugin's multi-language functionality
     */
    public static LangManager getLangManager() {
        return Objects.requireNonNull(langManager);
    }

    /**
     * Reloads the plugins config files
     */
    public void reloadConfig(@Nullable CommandContext commandContext) {
        try {
            mainConfigHolder.load(getDataDirectory().resolve("config.yml").toFile(), IssueLib.snakeYamlLoader());
        } catch (IssuesFoundException e) {
            IssueListPrinter<?> issueListPrinter = Namespace.INSTANCE.getOutputEnvironment().getIssueListPrinter();
            if (commandContext != null) commandContext.sendMessage(Message.raw(issueListPrinter.create(e).asString()));
            else e.printToConsole(Namespace.INSTANCE);
        }
    }

}