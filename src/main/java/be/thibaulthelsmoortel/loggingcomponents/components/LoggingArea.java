package be.thibaulthelsmoortel.loggingcomponents.components;

/**
 * Created by thibault.helsmoortel on 27-Apr-17.
 */

import be.thibaulthelsmoortel.loggingcomponents.appenders.TextAreaAppender;
import com.vaadin.ui.TextArea;

/**
 * Class representing an area that displays log output.
 *
 * @author Thibault Helsmoortel
 */
public class LoggingArea extends TextArea {

    public LoggingArea(String caption) {
        super(caption);
        TextAreaAppender.addTextArea(this);
        TextAreaAppender.appendPreviousLogs(this);
        setWordWrap(true);
        setReadOnly(true);
    }
}