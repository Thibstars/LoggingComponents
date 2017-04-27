package be.thibaulthelsmoortel.loggingcomponents.appenders;

import com.vaadin.ui.TextArea;
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
 * Log appender for the {@link be.thibaulthelsmoortel.loggingcomponents.components.LoggingArea}.
 *
 * @author Thibault Helsmoortel
 */
@Plugin(name = "TextAreaAppender", category = "Core", elementType = "appender", printObject = true)
public class TextAreaAppender extends AbstractAppender {

    private static final Logger LOGGER = LogManager.getLogger(TextAreaAppender.class);
    private static final ArrayList<TextArea> textAreaList = new ArrayList<>();
    private static final List<LogEvent> logs = new ArrayList<>();
    private static Layout<?> patternLayout = PatternLayout.createDefaultLayout();
    private int maxLines = 0;

    private TextAreaAppender(String name, Layout<?> layout, Filter filter, int maxLines, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        this.maxLines = maxLines;
    }

    @PluginFactory
    public static TextAreaAppender createAppender(@PluginAttribute("name") String name,
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

        return new TextAreaAppender(name, layout, filter, maxLines, ignoreExceptions);
    }

    // Add the target TextArea to be populated and updated by the logging information.
    public static void addTextArea(final TextArea textArea) {
        textAreaList.add(textArea);
    }

    public static void appendPreviousLogs(TextArea textArea) {
        for (LogEvent log : logs) {
            final String message = new String(patternLayout.toByteArray(log));

            UI.getCurrent().access(() -> {
                // Append formatted message to logging panel
                textArea.setValue(textArea.getValue() + message);
                textArea.setCursorPosition(Integer.MAX_VALUE);
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
                // Append formatted message to text area
                for (TextArea ta : textAreaList) {
                    ta.setValue(ta.getValue() + message);
                    ta.setCursorPosition(Integer.MAX_VALUE);
                }
            });
        }
    }
}