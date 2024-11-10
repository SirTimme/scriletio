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
                final var resourcePath = resource.getPath();
                // bundle-name_<Locale>.properties
                final var localeString = resourcePath.substring(resourcePath.indexOf("_") + 1, resourcePath.lastIndexOf("."));
                final var locale = parseLocale(localeString);

                availableLocales.add(locale);
            }
        }

        return availableLocales;
    }

    private Locale parseLocale(final String string) {
        if (!string.contains("_")) {
            return Locale.of(string);
        }

        final var language = string.substring(0, string.indexOf("_"));
        final var country = string.substring(string.indexOf("_") + 1);

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
