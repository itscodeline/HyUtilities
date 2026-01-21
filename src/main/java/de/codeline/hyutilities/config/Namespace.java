package de.codeline.hyutilities.config;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.util.Config;
import com.membercat.issuelib.IssueLib;
import com.membercat.issuelib.api.issue.ConfigIssue;
import com.membercat.issuelib.api.issue.IssueLevel;
import com.membercat.issuelib.api.issue.IssueNamespace;
import de.codeline.hyutilities.HyUtilities;
import org.jetbrains.annotations.NotNull;

/**
 * Custom IssueNamespace, used to log IssueLib issues via Hytale's logger
 */
public class Namespace extends IssueNamespace {

    public static final Namespace INSTANCE = new Namespace(HyUtilities.LOGGER);

    public final ConfigIssue langNotFoundIssue = this.add("HL0", IssueLevel.WARNING);

    private Namespace(HytaleLogger logger) {
        super("HyUtilities", IssueLib.basicEnv(t -> logger.atWarning().log(t.asString())));
    }

    @Override
    protected @NotNull GlobalSettings createSettings() {
        return new GlobalSettings();
    }

}
