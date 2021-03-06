package org.jenkinsci.plugins.gitclient;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Recording log handler to allow assertions on logging.  
 * Not intended for use outside tests.
 *
 * @author <a href="mailto:mark.earl.waite@gmail.com">Mark Waite</a>
 */
public class LogHandler extends Handler {

    private List<String> messages = new ArrayList<String>();

    @Override
    public void publish(LogRecord lr) {
        messages.add(lr.getMessage());
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
        messages = new ArrayList<String>();
    }

    public List<String> getMessages() {
        return messages;
    }

    public boolean containsMessageSubstring(String messageSubstring) {
        for (String message : messages) {
            if (message.contains(messageSubstring)) {
                return true;
            }
        }
        return false;
    }

    /* Intentionally package protected since tests outside the package should
     * not need to assert the values of timeout settings.
    */
    /* package */ List<Integer> getTimeouts() {
        List<Integer> timeouts = new ArrayList<Integer>();
        for (String message : getMessages()) {
            int start = message.indexOf(CliGitAPIImpl.TIMEOUT_LOG_PREFIX);
            if (start >= 0) {
                String timeoutStr = message.substring(start + CliGitAPIImpl.TIMEOUT_LOG_PREFIX.length());
                timeouts.add(Integer.parseInt(timeoutStr));
            }
        }
        return timeouts;
    }
}
