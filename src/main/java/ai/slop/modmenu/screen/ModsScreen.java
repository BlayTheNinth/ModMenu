package ai.slop.modmenu.screen;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ModsScreen extends Screen {
    private final Screen parent;
    private ModListWidget modList;

    public ModsScreen(Screen parent) {
        super(Component.literal("Mods"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.modList = new ModListWidget(this.minecraft, this.width, this.height - 64, 32, 36);
        this.addWidget(this.modList);

        this.addRenderableWidget(
            Button.builder(Component.literal("Done"), button -> {
                this.minecraft.setScreen(this.parent);
            })
            .bounds(this.width / 2 - 100, this.height - 28, 200, 20)
            .build()
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.modList.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 8, 0xFFFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private class ModListWidget extends ObjectSelectionList<ModListWidget.ModEntry> {
        public ModListWidget(net.minecraft.client.Minecraft minecraft, int width, int height, int y, int itemHeight) {
            super(minecraft, width, height, y, itemHeight);
            
            List<ModContainer> mods = new ArrayList<>(FabricLoader.getInstance().getAllMods());
            mods.sort((a, b) -> a.getMetadata().getName().compareToIgnoreCase(b.getMetadata().getName()));
            
            for (ModContainer mod : mods) {
                this.addEntry(new ModEntry(mod));
            }
        }

        @Override
        public int getRowWidth() {
            return this.width - 20;
        }

        private class ModEntry extends ObjectSelectionList.Entry<ModEntry> {
            private final ModContainer mod;

            public ModEntry(ModContainer mod) {
                this.mod = mod;
            }

            @Override
            public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean bl, float f) {
                String name = mod.getMetadata().getName();
                String version = mod.getMetadata().getVersion().getFriendlyString();
                String id = mod.getMetadata().getId();

                int left = this.getContentX();
                int top = this.getContentY();

                guiGraphics.drawString(
                    ModsScreen.this.font,
                    name,
                    left + 5,
                    top + 2,
                    0xFFFFFFFF,
                    false
                );
                
                guiGraphics.drawString(
                    ModsScreen.this.font,
                    "v" + version,
                    left + 5,
                    top + 12,
                    0x808080FF,
                    false
                );
                
                guiGraphics.drawString(
                    ModsScreen.this.font,
                    id,
                    left + 5,
                    top + 22,
                    0x606060FF,
                    false
                );
            }

            @Override
            public Component getNarration() {
                return Component.literal(mod.getMetadata().getName());
            }
        }
    }
}
