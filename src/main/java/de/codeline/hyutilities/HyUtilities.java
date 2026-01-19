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

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * This class serves as the entrypoint for your plugin. Use the setup method to register into game registries or add
 * event listeners.
 */
public class HyUtilities extends JavaPlugin {

    @Nonnull
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private ConfigurationHolder<PluginConfig> mainConfigHolder;

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

        reloadConfig();

        LOGGER.atInfo().log("Cats: " + getMainConfig().hateCats);

        this.getCommandRegistry().registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));
    }

    @Override
    protected void start() {
        LOGGER.atInfo().log("The plugin " + this.getName() + "was started!");
    }

    @Override
    protected void shutdown() {
        LOGGER.atInfo().log("The plugin " + this.getName() + "is shutting down!");
    }

    private void createConfigFiles(Path rootPath) throws IOException {
        boolean rootFolderCreated = rootPath.toFile().mkdirs();

        Path generalConfigPath = rootPath.resolve("config.yml");
        if (!Files.exists(generalConfigPath)) {
            InputStream stream = getClassLoader().getResourceAsStream("configs/config.yml");
            if (stream == null) throw new IOException("DefaultConfig doesn't exist.");
            Files.write(generalConfigPath, stream.readAllBytes(), StandardOpenOption.CREATE_NEW);
            stream.close();
        }
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