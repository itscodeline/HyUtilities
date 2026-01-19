package de.codeline.hyutilities.config;

import com.hypixel.hytale.logger.HytaleLogger;
import com.membercat.issuelib.IssueLib;
import com.membercat.issuelib.api.issue.IssueNamespace;
import de.codeline.hyutilities.HyUtilities;
import org.jetbrains.annotations.NotNull;

public class Namespace extends IssueNamespace {

    public static final Namespace INSTANCE = new Namespace(HyUtilities.LOGGER);

    private Namespace(HytaleLogger logger) {
        super("HyUtilities", IssueLib.basicEnv(t -> logger.atWarning().log(t.asString())));
    }

    @Override
    protected @NotNull GlobalSettings createSettings() {
        return new GlobalSettings();
    }

}
