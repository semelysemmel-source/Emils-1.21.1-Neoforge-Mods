package net.emil.enhancedearlygame.client.screen;

import net.emil.enhancedearlygame.EnhancedEarlygame;
import net.emil.enhancedearlygame.forging.ForgingSelection;
import net.emil.enhancedearlygame.menu.StoneAnvilMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

public class StoneAnvilScreen
        extends AbstractContainerScreen<StoneAnvilMenu> {

    /*
     * Die vollständige Hintergrundtextur des Stone-Anvil-Menüs.
     *
     * Speicherort:
     * assets/enhancedearlygame/textures/gui/container/stone_anvil.png
     */
    private static final ResourceLocation BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    EnhancedEarlygame.MODID,
                    "textures/gui/container/stone_anvil.png"
            );

    /*
     * Das ForgingSelectionWidget wird in init() erstellt.
     *
     * Es ist der Button rechts neben dem Ergebnis-Slot und enthält
     * das ausklappbare 3×3-Auswahlmenü.
     */
    @Nullable
    private ForgingSelectionWidget forgingSelectionWidget;

    /*
     * Dieser Konstruktor wird von Minecraft aufgerufen, sobald der
     * Spieler das StoneAnvilMenu öffnet.
     *
     * menu:
     * Das zugehörige serverseitige/containerseitige Menü.
     *
     * playerInventory:
     * Das Inventar des Spielers.
     *
     * title:
     * Der Titel, den StoneAnvilBlock über den SimpleMenuProvider liefert.
     */
    public StoneAnvilScreen(
            StoneAnvilMenu menu,
            Inventory playerInventory,
            Component title
    ) {
        super(menu, playerInventory, title);

        /*
         * Der benutzte Bereich deiner PNG-Datei ist 176×166 Pixel groß.
         * Die eigentliche Datei darf trotzdem 256×256 Pixel groß sein.
         */
        this.imageWidth = 176;
        this.imageHeight = 166;

        /*
         * Position des Titels "Repair & Forge".
         * Die Werte beziehen sich auf die linke obere Ecke der GUI.
         */
        this.titleLabelX = 8;
        this.titleLabelY = 6;

        /*
         * Position des Textes "Inventory".
         */
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 72;
    }

    /*
     * init() wird aufgerufen, wenn der Screen geöffnet wird.
     *
     * Die Methode kann erneut aufgerufen werden, wenn beispielsweise
     * die Fenstergröße verändert wird. Deshalb speichern wir zuerst
     * die bisherige Forging-Auswahl.
     */
    @Override
    protected void init() {
        ForgingSelection previousSelection = null;

        if (this.forgingSelectionWidget != null) {
            previousSelection =
                    this.forgingSelectionWidget.getSelectedSelection();
        }

        /*
         * Berechnet unter anderem leftPos und topPos.
         *
         * leftPos:
         * Linke Bildschirmposition der GUI.
         *
         * topPos:
         * Obere Bildschirmposition der GUI.
         */
        super.init();

        /*
         * Erstellt den Forging-Button.
         *
         * In deiner aktuellen stone_anvil.png beginnt der Forging-Button
         * bei GUI-Position x=147 und y=49.
         *
         * leftPos und topPos werden hinzugerechnet, weil der gesamte
         * GUI-Hintergrund in der Bildschirmmitte positioniert wird.
         */
        this.forgingSelectionWidget =
                this.addRenderableWidget(
                        new ForgingSelectionWidget(
                                this.leftPos + 147,
                                this.topPos + 49,
                                this.menu
                        )
                );

        /*
         * Stellt die ausgewählte Schmiedeoption wieder her, falls init()
         * durch eine Änderung der Fenstergröße erneut ausgeführt wurde.
         */
        this.forgingSelectionWidget
                .setSelectedSelection(
                        previousSelection
                );
    }

    /*
     * Zeichnet den Hintergrund des Stone-Anvil-Menüs.
     *
     * Es werden nur die ersten 176×166 Pixel der 256×256 großen
     * PNG-Datei verwendet.
     */
    @Override
    protected void renderBg(
            GuiGraphics graphics,
            float partialTick,
            int mouseX,
            int mouseY
    ) {
        graphics.blit(
                BACKGROUND_TEXTURE,
                this.leftPos,
                this.topPos,
                0,
                0,
                this.imageWidth,
                this.imageHeight
        );
    }

    /*
     * Führt den vollständigen Rendervorgang des Menüs aus.
     *
     * super.render(...) zeichnet:
     * - den Hintergrund
     * - die Items in den Slots
     * - den ForgingSelectionWidget
     * - den Titel
     * - den Inventartext
     *
     * renderTooltip(...) zeichnet danach die Item-Tooltips, wenn der
     * Mauszeiger über einem Inventar-Slot liegt.
     */
    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        super.render(
                graphics,
                mouseX,
                mouseY,
                partialTick
        );

        this.renderTooltip(
                graphics,
                mouseX,
                mouseY
        );

        if (this.forgingSelectionWidget != null) {
            this.forgingSelectionWidget.renderSelectionTooltip(
                    graphics,
                    mouseX,
                    mouseY
            );
        }
    }
}