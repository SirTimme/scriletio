package dev.sirtimme.scriletio.localization;

import io.github.classgraph.ClassGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.*;

import static dev.sirtimme.iuvo.api.listener.interaction.InteractionListener.USER_LOCALE;

public class LocalizationManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalizationManager.class);
    private final HashMap<Locale, MultiResourceBundle> bundles = new HashMap<>();

    public LocalizationManager() {
        registerBundles();
    }

    public String get(final String key, final Object... values) {
        final var bundle = getBundle(USER_LOCALE.get());
        final var template = new MessageFormat(bundle.getString(key));

        return template.format(values);
    }

    public String get(final String key, final Locale locale, final Object... values) {
        final var bundle = getBundle(locale);
        final var template = new MessageFormat(bundle.getString(key));

        return template.format(values);
    }

    private void registerBundles() {
        for (final var locale : getAvailableLocales()) {
            final var combinedBundle = new MultiResourceBundle(
                ResourceBundle.getBundle("localization/responses", locale),
                ResourceBundle.getBundle("localization/commands", locale)
            );

            bundles.put(locale, combinedBundle);
            LOGGER.info("Loaded bundle for locale '{}'", locale);
        }
    }

    private Set<Locale> getAvailableLocales() {
        final var availableLocales = new HashSet<Locale>();

        try (final var scanResult = new ClassGraph().acceptJars("scriletio.jar").acceptPathsNonRecursive("localization").scan()) {
            for (final var resource : scanResult.getResourcesWithExtension("properties")) {
                final var locale = parseResourcePath(resource.getPath());

                availableLocales.add(locale);
            }
        }

        return availableLocales;
    }

    private Locale parseResourcePath(final String resourcePath) {
        // commands_en_US.properties → en_US
        final var localeFilename = resourcePath.substring(resourcePath.indexOf("_") + 1, resourcePath.lastIndexOf("."));

        // locale only consists of language
        if (!localeFilename.contains("_")) {
            return Locale.of(localeFilename);
        }

        // locale consists of language and country
        final var language = localeFilename.substring(0, localeFilename.indexOf("_"));
        final var country = localeFilename.substring(localeFilename.indexOf("_") + 1);

        return Locale.of(language, country);
    }

    private ResourceBundle getBundle(final Locale locale) {
        final var bundle = bundles.get(locale);

        if (bundle == null) {
            return bundles.get(Locale.US);
        }

        return bundle;
    }
}
