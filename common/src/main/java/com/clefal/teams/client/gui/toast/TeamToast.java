package com.clefal.teams.client.gui.toast;

import com.clefal.teams.config.ATClientConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;


public abstract class TeamToast implements Toast {

    public final String team;
    private boolean firstDraw = true;
    private long firstDrawTime;

    public TeamToast(String team) {
        this.team = team;
    }

    public abstract Component title();

    public abstract Component subTitle();

    protected long getFirstDrawTime(){
        return this.firstDrawTime;
    }
    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent manager, long startTime) {
        if (firstDraw) {
            firstDrawTime = startTime;
            firstDraw = false;
        }

        graphics.blit(TEXTURE, 0, 0, 0, 64, this.width(), this.height());
        graphics.drawString(manager.getMinecraft().font, title(), 22, 7, ChatFormatting.WHITE.getColor());
        graphics.drawString(manager.getMinecraft().font, subTitle(), 22, 18, ChatFormatting.WHITE.getColor(),false);

        return startTime - firstDrawTime < ATClientConfig.config.info.normalToastShowSecond.get() * 1000L && team != null ? Visibility.SHOW : Visibility.HIDE;
    }
}
