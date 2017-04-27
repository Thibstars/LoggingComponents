package be.thibaulthelsmoortel.loggingcomponents.appenders;

import be.thibaulthelsmoortel.loggingcomponents.components.LoggingPanel;
import com.vaadin.ui.UI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Log appender for the {@link be.thibaulthelsmoortel.loggingcomponents.components.LoggingPanel}.
 *
 * @author Thibault Helsmoortel
 */
@Plugin(name = "LoggingPanelAppender", category = "Core", elementType = "appender", printObject = true)
public class LoggingPanelAppender extends AbstractAppender {

    private static final Logger LOGGER = LogManager.getLogger(LoggingPanelAppender.class);

    private static final ArrayList<LoggingPanel> loggingPanelList = new ArrayList<>();
    private static final List<LogEvent> logs = new ArrayList<>();
    private static Layout<?> patternLayout = PatternLayout.createDefaultLayout();
    private int maxLines = 0;

    private LoggingPanelAppender(String name, Layout<?> layout, Filter filter, int maxLines, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        this.maxLines = maxLines;
    }

    @PluginFactory
    public static LoggingPanelAppender createAppender(@PluginAttribute("name") String name,
            @PluginAttribute("maxLines") int maxLines,
            @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
            @PluginElement("Layout") Layout<?> layout,
            @PluginElement("Filters") Filter filter) {

        if (name == null) {
            LOGGER.error("No name provided for TextAreaAppender");
            return null;
        }

        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }

        patternLayout = layout;

        return new LoggingPanelAppender(name, layout, filter, maxLines, ignoreExceptions);
    }

    // Add the target LoggingPanel to be populated and updated by the logging information.
    public static void addPanel(LoggingPanel loggingPanel) {
        loggingPanelList.add(loggingPanel);
    }

    public static void appendPreviousLogs(LoggingPanel loggingPanel) {
        for (LogEvent log : logs) {
            final String message = new String(patternLayout.toByteArray(log));

            UI.getCurrent().access(() -> {
                // Append formatted message to logging panel
                loggingPanel.addLogLine(message, log.getLevel());
                loggingPanel.setScrollTop(Integer.MAX_VALUE);
            });
        }
    }

    @Override
    public void append(LogEvent event) {
        logs.add(event);
        final String message = new String(this.getLayout().toByteArray(event));

        UI current = UI.getCurrent();

        if (current != null) {
            current.access(() -> {
                // Append formatted message to logging panel
                for (LoggingPanel lp : loggingPanelList) {
                    lp.addLogLine(message, event.getLevel());
                    lp.setScrollTop(Integer.MAX_VALUE);
                }
            });
        }
    }
}
