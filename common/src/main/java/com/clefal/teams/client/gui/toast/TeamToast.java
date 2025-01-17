package com.clefal.teams.client.gui.toast;

import com.clefal.teams.config.ATConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;


public abstract class TeamToast implements Toast {

    public final String team;
    private boolean firstDraw = true;
    private long firstDrawTime;

    public TeamToast(String team) {
        this.team = team;
    }

    public abstract String title();

    public abstract String subTitle();

    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent manager, long startTime) {
        if (firstDraw) {
            firstDrawTime = startTime;
            firstDraw = false;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(TEXTURE, 0, 0, 0, 64, this.width(), this.height());
        graphics.drawString(manager.getMinecraft().font, title(), 22, 7, ChatFormatting.WHITE.getColor());
        graphics.drawString(manager.getMinecraft().font, subTitle(), 22, 18, 0xff000000,false);

        return startTime - firstDrawTime < ATConfig.config.info.toastShowSecond.get() * 1000L && team != null ? Visibility.SHOW : Visibility.HIDE;    }
}
