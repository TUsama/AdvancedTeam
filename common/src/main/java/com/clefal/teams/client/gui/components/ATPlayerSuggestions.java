package com.clefal.teams.client.gui.components;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.util.FormattedCharSequence;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ATPlayerSuggestions extends CommandSuggestions {
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("(\\s+)");
    private final Screen screen;
    final EditBox input;

    private final List<FormattedCharSequence> commandUsage = Lists.newArrayList();
    private int commandUsagePosition;
    private int commandUsageWidth;
    @Nullable
    private ParseResults<SharedSuggestionProvider> currentParse;
    @Nullable
    private CompletableFuture<Suggestions> pendingSuggestions;
    @Nullable
    private SuggestionsList suggestions;
    private boolean allowSuggestions;
    boolean keepSuggestions;

    public ATPlayerSuggestions(Minecraft minecraft, Screen screen, EditBox input, Font font, boolean onlyShowIfCursorPastError, int lineStartOffset, int suggestionLineLimit, boolean anchorToBottom, int fillColor) {
        super(minecraft, screen, input, font, false, onlyShowIfCursorPastError, lineStartOffset, suggestionLineLimit, anchorToBottom, fillColor);
        this.input = input;
        this.screen = screen;
    }

    @Override
    public void updateCommandInfo() {
        String $$0 = this.input.getValue();
        if (this.currentParse != null && !this.currentParse.getReader().getString().equals($$0)) {
            this.currentParse = null;
        }

        if (!this.keepSuggestions) {
            this.input.setSuggestion((String)null);
            this.suggestions = null;
        }

        this.commandUsage.clear();
        StringReader $$1 = new StringReader($$0);
        boolean $$2 = $$1.canRead() && $$1.peek() == '/';
        if ($$2) {
            $$1.skip();
        }

        int $$4 = this.input.getCursorPosition();
        int $$6;
        String $$7 = $$0.substring(0, $$4);
        $$6 = getLastWordIndex($$7);
        Collection<String> $$9 = Minecraft.getInstance().player.connection.getSuggestionsProvider().getCustomTabSugggestions();
        this.pendingSuggestions = SharedSuggestionProvider.suggest($$9, new SuggestionsBuilder($$7, $$6));
    }

    private static int getLastWordIndex(String text) {
        if (Strings.isNullOrEmpty(text)) {
            return 0;
        } else {
            int i = 0;

            for(Matcher matcher = WHITESPACE_PATTERN.matcher(text); matcher.find(); i = matcher.end()) {
            }

            return i;
        }
    }
}
