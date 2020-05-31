package pl.walaniam.srabble.gui.i18n;

import java.text.MessageFormat;
import java.util.*;

public class I18N {
    
    public static final Locale DEFAULT_LOCALE = new Locale("pl");
    
    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages", DEFAULT_LOCALE);
    
    private I18N() {
    }
    
    /**
     * 
     * @param key
     * @param replacements
     * @return
     */
    public static String getMessage(String key, Object[] replacements) {

        String message = null;

        try {
            message = bundle.getString(key);

            if (replacements != null && replacements.length > 0) {
                message = MessageFormat.format(message, replacements);
            }

        } catch (Exception e) {
            message = "#I18N missing: " + key;
        }

        return message;
    }
    
    public static String getMessage(String key, Object replacement) {
        return getMessage(key, new Object[]{replacement});
    }
    
    public static String getMessage(String key) {
        return getMessage(key, (Object[]) null);
    }
    
    public static Map<String,String> getMessages(String prefix, boolean truncateKeyPrefix) {
        Map<String,String> result = new HashMap<>();
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.startsWith(prefix)) {
                String newKey = truncateKeyPrefix ? key.replaceFirst(prefix, "") : key;
                result.put(newKey, getMessage(key));
            }
        }
        return result;
    }
}
