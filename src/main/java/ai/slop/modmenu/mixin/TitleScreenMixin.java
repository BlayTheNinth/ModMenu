package ai.slop.modmenu.mixin;

import ai.slop.modmenu.screen.ModsScreen;
import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void addModsButton(CallbackInfo ci) {
        Button realmsButton = null;
        
        for (final var widget : this.children()) {
            if (widget instanceof Button button) {
                Component message = button.getMessage();
                if (message.getString().equals(Component.translatable("menu.online").getString())) {
                    realmsButton = button;
                    break;
                }
            }
        }
        
        if (realmsButton != null) {
            int buttonWidth = 98;
            int buttonHeight = 20;
            int spacing = 4;
            
            int realmsX = this.width / 2 - buttonWidth - spacing / 2;
            int modsX = this.width / 2 + spacing / 2;
            int y = realmsButton.getY();
            
            realmsButton.setX(realmsX);
            realmsButton.setWidth(buttonWidth);
            
            this.addRenderableWidget(
                Button.builder(Component.literal("Mods"), button -> {
                    this.minecraft.setScreen(new ModsScreen(this));
                })
                .bounds(modsX, y, buttonWidth, buttonHeight)
                .build()
            );
        }
    }
}
