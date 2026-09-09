package net.emil.enhancedearlygame.client.screen;

import net.emil.enhancedearlygame.EnhancedEarlygame;
import net.emil.enhancedearlygame.forging.ForgingSelection;
import net.emil.enhancedearlygame.menu.EnhancedAnvilMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EnhancedAnvilScreen
        extends ItemCombinerScreen<EnhancedAnvilMenu> {

    private static final ResourceLocation BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    EnhancedEarlygame.MODID,
                    "textures/gui/container/enhanced_anvil.png"
            );

    private static final ResourceLocation TEXT_FIELD_SPRITE =
            ResourceLocation.withDefaultNamespace(
                    "container/anvil/text_field"
            );

    private static final ResourceLocation
            TEXT_FIELD_DISABLED_SPRITE =
            ResourceLocation.withDefaultNamespace(
                    "container/anvil/text_field_disabled"
            );

    private static final ResourceLocation ERROR_SPRITE =
            ResourceLocation.withDefaultNamespace(
                    "container/anvil/error"
            );

    private static final Component TOO_EXPENSIVE_TEXT =
            Component.translatable(
                    "container.repair.expensive"
            );

    private final Player player;

    private EditBox name;

    @Nullable
    private ForgingSelectionWidget
            forgingSelectionWidget;

    public EnhancedAnvilScreen(
            EnhancedAnvilMenu menu,
            Inventory inventory,
            Component title
    ) {
        super(
                menu,
                inventory,
                title,
                BACKGROUND_TEXTURE
        );

        this.player = inventory.player;

        this.imageWidth = 176;
        this.imageHeight = 166;

        this.titleLabelX = 8;
        this.titleLabelY = 6;

        this.inventoryLabelX = 8;
        this.inventoryLabelY = 72;
    }

    /*
     * Wird von ItemCombinerScreen.init() aufgerufen.
     */
    @Override
    protected void subInit() {
        ForgingSelection previousSelection =
                this.forgingSelectionWidget == null
                        ? null
                        : this.forgingSelectionWidget
                        .getSelectedSelection();

        int screenX =
                (this.width - this.imageWidth) / 2;

        int screenY =
                (this.height - this.imageHeight) / 2;

        /*
         * Vanilla-Namensfeld.
         */
        this.name = new EditBox(
                this.font,
                screenX + 62,
                screenY + 24,
                103,
                12,
                Component.translatable(
                        "container.repair"
                )
        );

        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(50);
        this.name.setResponder(
                this::onNameChanged
        );
        this.name.setValue("");

        this.addWidget(this.name);

        this.name.setEditable(
                this.menu.getSlot(0).hasItem()
        );

        /*
         * Forging-Button rechts neben dem Output.
         */
        this.forgingSelectionWidget =
                this.addRenderableWidget(
                        new ForgingSelectionWidget(
                                this.leftPos + 147,
                                this.topPos + 49,
                                this.menu
                        )
                );

        this.forgingSelectionWidget
                .setSelectedSelection(
                        previousSelection
                );
    }

    @Override
    protected void setInitialFocus() {
        this.setInitialFocus(this.name);
    }

    /*
     * Erhält den eingegebenen Namen, wenn das Fenster
     * seine Größe verändert.
     */
    @Override
    public void resize(
            Minecraft minecraft,
            int width,
            int height
    ) {
        String currentName =
                this.name == null
                        ? ""
                        : this.name.getValue();

        this.init(
                minecraft,
                width,
                height
        );

        this.name.setValue(
                currentName
        );
    }

    @Override
    public boolean keyPressed(
            int keyCode,
            int scanCode,
            int modifiers
    ) {
        if (keyCode == 256) {
            this.minecraft
                    .player
                    .closeContainer();
        }

        if (this.name.keyPressed(
                keyCode,
                scanCode,
                modifiers
        )) {
            return true;
        }

        if (this.name.canConsumeInput()) {
            return true;
        }

        return super.keyPressed(
                keyCode,
                scanCode,
                modifiers
        );
    }

    /*
     * Wird bei jeder Änderung des Rename-Feldes aufgerufen.
     */
    private void onNameChanged(String newName) {
        Slot inputSlot =
                this.menu.getSlot(0);

        if (!inputSlot.hasItem()) {
            return;
        }

        String nameToSend = newName;

        if (!inputSlot.getItem().has(
                DataComponents.CUSTOM_NAME
        ) && newName.equals(
                inputSlot.getItem()
                        .getHoverName()
                        .getString()
        )) {
            nameToSend = "";
        }

        if (this.menu.setItemName(
                nameToSend
        )) {
            this.minecraft
                    .player
                    .connection
                    .send(
                            new ServerboundRenameItemPacket(
                                    nameToSend
                            )
                    );
        }
    }

    /*
     * Zeichnet Titel, Inventory und gegebenenfalls
     * die Enchantment-Kosten.
     */
    @Override
    protected void renderLabels(
            GuiGraphics graphics,
            int mouseX,
            int mouseY
    ) {
        super.renderLabels(
                graphics,
                mouseX,
                mouseY
        );

        int cost =
                this.menu.getCost();

        if (cost <= 0) {
            return;
        }

        int textColor = 8453920;
        Component costText;

        if (cost >= 40
                && !this.minecraft
                .player
                .getAbilities()
                .instabuild) {
            costText = TOO_EXPENSIVE_TEXT;
            textColor = 16736352;
        } else if (!this.menu
                .getSlot(2)
                .hasItem()) {
            return;
        } else {
            costText =
                    Component.translatable(
                            "container.repair.cost",
                            cost
                    );

            if (!this.menu
                    .getSlot(2)
                    .mayPickup(this.player)) {
                textColor = 16736352;
            }
        }

        int textX =
                this.imageWidth
                        - 8
                        - this.font.width(costText)
                        - 2;

        int textY = 69;

        graphics.fill(
                textX - 2,
                67,
                this.imageWidth - 8,
                79,
                1325400064
        );

        graphics.drawString(
                this.font,
                costText,
                textX,
                textY,
                textColor
        );
    }

    /*
     * ItemCombinerScreen zeichnet hier automatisch
     * enhanced_anvil.png.
     *
     * Danach wird der Hintergrund des Namensfeldes
     * darübergelegt.
     */
    @Override
    protected void renderBg(
            GuiGraphics graphics,
            float partialTick,
            int mouseX,
            int mouseY
    ) {
        super.renderBg(
                graphics,
                partialTick,
                mouseX,
                mouseY
        );

        ResourceLocation textFieldTexture =
                this.menu.getSlot(0).hasItem()
                        ? TEXT_FIELD_SPRITE
                        : TEXT_FIELD_DISABLED_SPRITE;

        graphics.blitSprite(
                textFieldTexture,
                this.leftPos + 59,
                this.topPos + 20,
                110,
                16
        );
    }

    /*
     * Zeichnet den Inhalt des Rename-Feldes.
     */
    @Override
    public void renderFg(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        this.name.render(
                graphics,
                mouseX,
                mouseY,
                partialTick
        );
    }

    /*
     * Zeichnet das rote X, wenn Inputs vorhanden sind,
     * aber kein gültiger Output erzeugt werden kann.
     */
    @Override
    protected void renderErrorIcon(
            GuiGraphics graphics,
            int screenX,
            int screenY
    ) {
        boolean hasInput =
                this.menu.getSlot(0).hasItem()
                        || this.menu
                        .getSlot(1)
                        .hasItem();

        boolean hasNoResult =
                !this.menu
                        .getSlot(
                                this.menu.getResultSlot()
                        )
                        .hasItem();

        if (hasInput && hasNoResult) {
            graphics.blitSprite(
                    ERROR_SPRITE,
                    screenX + 99,
                    screenY + 45,
                    28,
                    21
            );
        }
    }

    /*
     * Aktualisiert das Namensfeld, wenn sich der
     * linke Input-Slot verändert.
     */
    @Override
    public void slotChanged(
            AbstractContainerMenu menu,
            int slotIndex,
            ItemStack stack
    ) {
        if (slotIndex != 0) {
            return;
        }

        this.name.setValue(
                stack.isEmpty()
                        ? ""
                        : stack.getHoverName()
                        .getString()
        );

        this.name.setEditable(
                !stack.isEmpty()
        );

        this.setFocused(this.name);
    }
}
