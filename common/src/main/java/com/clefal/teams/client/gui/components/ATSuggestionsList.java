package com.clefal.teams.client.gui.components;


import com.mojang.brigadier.Message;
import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.CommandSuggestions.SuggestionsList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ATSuggestionsList {
    private final Rect2i rect;
    private final String originalContents;
    private final List<Suggestion> suggestionList;
    private int offset;
    private int current;
    private Vec2 lastMouse;
    private boolean tabCycles;
    private int lastNarratedEntry;
    private boolean anchorToBottom = true;
    final int suggestionLineLimit;
    EditBox input;
    int fillColor = FastColor.ARGB32.color(255, 255, 255, 255);
    Font font = Minecraft.getInstance().font;
    Minecraft minecraft = Minecraft.getInstance();
    boolean shouldHide = false; //!!无更新
    int lineStartOffset = 2;
    boolean keepSuggestions;

    ATSuggestionsList(int xPos, int yPos, int width, List<Suggestion> suggestionList, boolean narrateFirstSuggestion, int suggestionLineLimit, EditBox input) {
        this.lastMouse = Vec2.ZERO;
        this.input = input;
        this.suggestionLineLimit = suggestionLineLimit;
        int i = xPos - 1;
        int j = this.anchorToBottom ? yPos - 3 - Math.min(suggestionList.size(), this.suggestionLineLimit) * 12 : yPos;
        this.rect = new Rect2i(i, j, width + 1, Math.min(suggestionList.size(), this.suggestionLineLimit) * 12);
        this.originalContents = this.input.getValue();
        this.lastNarratedEntry = narrateFirstSuggestion ? -1 : 0;
        this.suggestionList = suggestionList;
        this.select(0);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int i = Math.min(this.suggestionList.size(), this.suggestionLineLimit);
        int j = -5592406;
        boolean bl = this.offset > 0;
        boolean bl2 = this.suggestionList.size() > this.offset + i;
        boolean bl3 = bl || bl2;
        boolean bl4 = this.lastMouse.x != (float)mouseX || this.lastMouse.y != (float)mouseY;
        if (bl4) {
            this.lastMouse = new Vec2((float)mouseX, (float)mouseY);
        }

        if (bl3) {
            guiGraphics.fill(this.rect.getX(), this.rect.getY() - 1, this.rect.getX() + this.rect.getWidth(), this.rect.getY(), this.fillColor);
            guiGraphics.fill(this.rect.getX(), this.rect.getY() + this.rect.getHeight(), this.rect.getX() + this.rect.getWidth(), this.rect.getY() + this.rect.getHeight() + 1, this.fillColor);
            int k;
            if (bl) {
                for(k = 0; k < this.rect.getWidth(); ++k) {
                    if (k % 2 == 0) {
                        guiGraphics.fill(this.rect.getX() + k, this.rect.getY() - 1, this.rect.getX() + k + 1, this.rect.getY(), -1);
                    }
                }
            }

            if (bl2) {
                for(k = 0; k < this.rect.getWidth(); ++k) {
                    if (k % 2 == 0) {
                        guiGraphics.fill(this.rect.getX() + k, this.rect.getY() + this.rect.getHeight(), this.rect.getX() + k + 1, this.rect.getY() + this.rect.getHeight() + 1, -1);
                    }
                }
            }
        }

        boolean bl5 = false;

        for(int l = 0; l < i; ++l) {
            Suggestion suggestion = (Suggestion)this.suggestionList.get(l + this.offset);
            guiGraphics.fill(this.rect.getX(), this.rect.getY() + 12 * l, this.rect.getX() + this.rect.getWidth(), this.rect.getY() + 12 * l + 12, this.fillColor);
            if (mouseX > this.rect.getX() && mouseX < this.rect.getX() + this.rect.getWidth() && mouseY > this.rect.getY() + 12 * l && mouseY < this.rect.getY() + 12 * l + 12) {
                if (bl4) {
                    this.select(l + this.offset);
                }

                bl5 = true;
            }

            guiGraphics.drawString(this.font, suggestion.getText(), this.rect.getX() + 1, this.rect.getY() + 2 + 12 * l, l + this.offset == this.current ? -256 : -5592406);
        }

        if (bl5) {
            Message message = ((Suggestion)this.suggestionList.get(this.current)).getTooltip();
            if (message != null) {
                guiGraphics.renderTooltip(this.font, ComponentUtils.fromMessage(message), mouseX, mouseY);
            }
        }

    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!this.rect.contains(mouseX, mouseY)) {
            return false;
        } else {
            int i = (mouseY - this.rect.getY()) / 12 + this.offset;
            if (i >= 0 && i < this.suggestionList.size()) {
                this.select(i);
                this.useSuggestion();
            }

            return true;
        }
    }

    public boolean mouseScrolled(double delta) {
        int i = (int)(this.minecraft.mouseHandler.xpos() * (double)this.minecraft.getWindow().getGuiScaledWidth() / (double)this.minecraft.getWindow().getScreenWidth());
        int j = (int)(this.minecraft.mouseHandler.ypos() * (double)this.minecraft.getWindow().getGuiScaledHeight() / (double)this.minecraft.getWindow().getScreenHeight());
        if (this.rect.contains(i, j)) {
            this.offset = Mth.clamp((int)((double)this.offset - delta), 0, Math.max(this.suggestionList.size() - this.suggestionLineLimit, 0));
            return true;
        } else {
            return false;
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 265) {
            this.cycle(-1);
            this.tabCycles = false;
            return true;
        } else if (keyCode == 264) {
            this.cycle(1);
            this.tabCycles = false;
            return true;
        } else if (keyCode == 258) {
            if (this.tabCycles) {
                this.cycle(Screen.hasShiftDown() ? -1 : 1);
            }

            this.useSuggestion();
            return true;
        } else {
            return false;
        }
    }

    public void cycle(int change) {
        this.select(this.current + change);
        int i = this.offset;
        int j = this.offset + this.suggestionLineLimit - 1;
        if (this.current < i) {
            this.offset = Mth.clamp(this.current, 0, Math.max(this.suggestionList.size() - this.suggestionLineLimit, 0));
        } else if (this.current > j) {
            this.offset = Mth.clamp(this.current + this.lineStartOffset - this.suggestionLineLimit, 0, Math.max(this.suggestionList.size() - this.suggestionLineLimit, 0));
        }

    }

    public void select(int index) {
        this.current = index;
        if (this.current < 0) {
            this.current += this.suggestionList.size();
        }

        if (this.current >= this.suggestionList.size()) {
            this.current -= this.suggestionList.size();
        }

        Suggestion suggestion = (Suggestion)this.suggestionList.get(this.current);
        this.input.setSuggestion(calculateSuggestionSuffix(this.input.getValue(), suggestion.apply(this.originalContents)));
        if (this.lastNarratedEntry != this.current) {
            this.minecraft.getNarrator().sayNow(this.getNarrationMessage());
        }

    }

    @Nullable
    static String calculateSuggestionSuffix(String inputText, String suggestionText) {
        return suggestionText.startsWith(inputText) ? suggestionText.substring(inputText.length()) : null;
    }

    public void useSuggestion() {
        Suggestion suggestion = (Suggestion)this.suggestionList.get(this.current);
        this.keepSuggestions = true;
        this.input.setValue(suggestion.apply(this.originalContents));
        int i = suggestion.getRange().getStart() + suggestion.getText().length();
        this.input.setCursorPosition(i);
        this.input.setHighlightPos(i);
        this.select(this.current);
        this.keepSuggestions = false;
        this.tabCycles = true;
    }

    Component getNarrationMessage() {
        this.lastNarratedEntry = this.current;
        Suggestion suggestion = (Suggestion)this.suggestionList.get(this.current);
        Message message = suggestion.getTooltip();
        return message != null ? Component.translatable("narration.suggestion.tooltip", new Object[]{this.current + 1, this.suggestionList.size(), suggestion.getText(), message}) : Component.translatable("narration.suggestion", new Object[]{this.current + 1, this.suggestionList.size(), suggestion.getText()});
    }
}

