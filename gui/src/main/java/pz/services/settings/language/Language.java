package pz.services.settings.language;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public enum Language {
    POLSKI("polski", new Locale("pl", "PL")),
    ENGLISH("english", new Locale("en", "EN"));

    @Getter
    public String languageName;
    public Locale locale;

    Language(String languageName, Locale locale) {
        this.languageName = languageName;
        this.locale = locale;
    }

    public static List<String> languageNames() {
        return Arrays.stream(values())
                .map(Language::getLanguageName)
                .collect(Collectors.toList());
    }

    public static Language forLanguageName(String languageName) {
        return Arrays.stream(values())
                .filter(language -> language.getLanguageName().equalsIgnoreCase(languageName))
                .findFirst()
                .orElse(null);
    }
}
