package com.chat.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class RegexProfanityHandler extends ModerationHandler {

    // Pre-compile your patterns once for efficiency
    private final List<Pattern> patterns = List.of(
            Pattern.compile("(?i)\\bf\\*?u\\*?c\\*?k\\b"),
            Pattern.compile("(?i)\\bs\\*?h\\*?i\\*?t\\b"),
            Pattern.compile("(?i)\\bb\\*?i\\*?t\\*?c\\*?h\\b")
            // …add more as needed
    );

    @Override
    public boolean handle(String message) {
        for (Pattern p : patterns) {
            if (p.matcher(message).find()) {
                System.out.println("RegexProfanityHandler: Profanity detected!");
                return false;  // stop the chain, reject message
            }
        }
        // no match → delegate to next in chain (if any)
        return next == null || next.handle(message);
    }
}
