package dev.sirtimme.scriletio.localization;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MultiResourceBundle extends ResourceBundle {
    private final List<ResourceBundle> bundles;

    public MultiResourceBundle(final ResourceBundle... bundles) {
        this.bundles = Arrays.asList(bundles);
    }

    @Override
    protected Object handleGetObject(@NotNull final String key) {
        return bundles
                .stream()
                .filter(bundle -> bundle.containsKey(key))
                .map(bundle -> bundle.getObject(key))
                .findFirst()
                .orElse(null);
    }

    @NotNull
    @Override
    public Enumeration<String> getKeys() {
        final var keys = bundles
                .stream()
                .flatMap(bundle -> Collections.list(bundle.getKeys()).stream())
                .toList();

        return Collections.enumeration(keys);
    }
}
