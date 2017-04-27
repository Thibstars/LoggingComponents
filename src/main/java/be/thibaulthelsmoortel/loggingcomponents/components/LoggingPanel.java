package be.thibaulthelsmoortel.loggingcomponents.components;

import be.thibaulthelsmoortel.loggingcomponents.theme.LCTheme;
import be.thibaulthelsmoortel.loggingcomponents.appenders.LoggingPanelAppender;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.apache.logging.log4j.Level;

import static org.apache.logging.log4j.Level.*;

/**
 * Class representing a panel that displays log output.
 *
 * @author Thibault Helsmoortel
 */
public class LoggingPanel extends Panel {

    private final boolean colored;
    private VerticalLayout mainLayout;

    public LoggingPanel(String caption) {
        super(caption);
        this.colored = true;
        init();
    }

    public LoggingPanel(boolean colored) {
        super();
        this.colored = colored;
        init();
    }

    public LoggingPanel(String caption, boolean colored) {
        super(caption);
        this.colored = colored;
        init();
    }

    private void init() {
        LoggingPanelAppender.addPanel(this);
        LoggingPanelAppender.appendPreviousLogs(this);
        addStyleName(LCTheme.LOGGING_PANEL);
        mainLayout = new VerticalLayout();
        mainLayout.setSpacing(false);
        setContent(mainLayout);
    }

    public void addLogLine(String message, Level level) {
        Label lblLogLine = new Label(message);

        if (colored) {
            String styleName = "";
            if (INFO.equals(level)) {
                styleName = LCTheme.LOG_LEVEL_INFO;
            } else if (DEBUG.equals(level)) {
                styleName = LCTheme.LOG_LEVEL_DEBUG;
            } else if (WARN.equals(level)) {
                styleName = LCTheme.LOG_LEVEL_WARN;
            } else if (ERROR.equals(level)) {
                styleName = LCTheme.LOG_LEVEL_ERROR;
            } else if (FATAL.equals(level)) {
                styleName = LCTheme.LOG_LEVEL_FATAL;
            }
            lblLogLine.setStyleName(styleName);
        }
        mainLayout.addComponent(lblLogLine);
    }
}
