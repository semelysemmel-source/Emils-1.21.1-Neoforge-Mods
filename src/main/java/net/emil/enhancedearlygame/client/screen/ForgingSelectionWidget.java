package net.emil.enhancedearlygame.client.screen;

import net.emil.enhancedearlygame.EnhancedEarlygame;
import net.emil.enhancedearlygame.forging.ForgingSelection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ForgingSelectionWidget extends AbstractWidget {

    /*
     * Jeder Button ist 18 × 18 Pixel groß:
     * 16 × 16 Pixel für das Icon und jeweils ein Pixel Rand.
     */
    private static final int BUTTON_SIZE = 18;

    /*
     * Das ausgeklappte Menü besteht aus drei Spalten.
     */
    private static final int GRID_COLUMNS = 3;

    /*
     * Hintergrund eines Auswahlbuttons.
     *
     * Datei:
     * assets/enhancedearlygame/textures/gui/forging_widgets.png
     */
    private static final ResourceLocation BUTTON_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    EnhancedEarlygame.MODID,
                    "textures/gui/forging_widgets.png"
            );

    /*
     * Icon, solange noch keine Schmiedeoption ausgewählt wurde.
     *
     * Datei:
     * assets/enhancedearlygame/textures/gui/forging/forging_default.png
     */
    private static final ResourceLocation DEFAULT_ICON =
            ResourceLocation.fromNamespaceAndPath(
                    EnhancedEarlygame.MODID,
                    "textures/gui/forging/forging_default.png"
            );

    /*
     * Ordnet jeder ForgingSelection die passende weiße Icon-Textur zu.
     */
    private static final Map<ForgingSelection, ResourceLocation> SELECTION_ICONS =
            Map.ofEntries(
                    Map.entry(
                            ForgingSelection.PICKAXE_HEAD,
                            forgingIcon("pickaxe_head")
                    ),
                    Map.entry(
                            ForgingSelection.AXE_HEAD,
                            forgingIcon("axe_head")
                    ),
                    Map.entry(
                            ForgingSelection.SHOVEL_HEAD,
                            forgingIcon("shovel_head")
                    ),
                    Map.entry(
                            ForgingSelection.SWORD_BLADE,
                            forgingIcon("sword_blade")
                    ),
                    Map.entry(
                            ForgingSelection.HOE_HEAD,
                            forgingIcon("hoe_head")
                    ),
                    Map.entry(
                            ForgingSelection.HELMET,
                            forgingIcon("helmet")
                    ),
                    Map.entry(
                            ForgingSelection.CHESTPLATE,
                            forgingIcon("chestplate")
                    ),
                    Map.entry(
                            ForgingSelection.LEGGINGS,
                            forgingIcon("leggings")
                    ),
                    Map.entry(
                            ForgingSelection.BOOTS,
                            forgingIcon("boots")
                    )
            );

    /*
     * Das Menü, zu dem dieser Button gehört.
     *
     * Daraus wird die containerId benötigt, damit der Klick
     * an das serverseitige Menü gesendet werden kann.
     */
    private final AbstractContainerMenu menu;

    /*
     * true:
     * Das 3 × 3-Auswahlmenü ist geöffnet.
     *
     * false:
     * Nur der Hauptbutton wird angezeigt.
     */
    private boolean expanded;

    /*
     * Die momentan ausgewählte Schmiedeoption.
     *
     * null bedeutet, dass noch nichts ausgewählt wurde.
     */
    @Nullable
    private ForgingSelection selectedSelection;

    public ForgingSelectionWidget(
            int x,
            int y,
            AbstractContainerMenu menu
    ) {
        super(
                x,
                y,
                BUTTON_SIZE,
                BUTTON_SIZE,
                Component.translatable("gui.enhancedearlygame.forging")
        );

        this.menu = menu;
        this.expanded = false;
        this.selectedSelection = null;
    }

    /*
     * Erzeugt einen vollständigen ResourceLocation-Pfad für ein Forging-Icon.
     */
    private static ResourceLocation forgingIcon(String iconName) {
        return ResourceLocation.fromNamespaceAndPath(
                EnhancedEarlygame.MODID,
                "textures/gui/forging/" + iconName + ".png"
        );
    }

    /*
     * Zeichnet den Hauptbutton und gegebenenfalls das ausgeklappte Menü.
     */
    @Override
    protected void renderWidget(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        renderMainButton(graphics, mouseX, mouseY);

        if (this.expanded) {
            renderSelectionGrid(graphics, mouseX, mouseY);
        }
    }

    /*
     * Zeichnet den immer sichtbaren Hauptbutton.
     */
    private void renderMainButton(
            GuiGraphics graphics,
            int mouseX,
            int mouseY
    ) {
        int buttonX = this.getX();
        int buttonY = this.getY();

        renderButtonBackground(graphics, buttonX, buttonY);

        ResourceLocation icon;

        if (this.selectedSelection == null) {
            icon = DEFAULT_ICON;
        } else {
            icon = SELECTION_ICONS.get(this.selectedSelection);
        }

        renderIcon(graphics, icon, buttonX, buttonY);

        if (isInsideButton(mouseX, mouseY, buttonX, buttonY)) {
            renderHoverOverlay(graphics, buttonX, buttonY);
        }
    }

    /*
     * Zeichnet die neun Schmiedeoptionen als 3 × 3-Raster.
     *
     * Das Raster beginnt direkt rechts neben dem Hauptbutton.
     */
    private void renderSelectionGrid(
            GuiGraphics graphics,
            int mouseX,
            int mouseY
    ) {
        for (ForgingSelection selection : ForgingSelection.values()) {
            int buttonId = selection.buttonId();

            int column = buttonId % GRID_COLUMNS;
            int row = buttonId / GRID_COLUMNS;

            int optionX =
                    this.getX()
                            + BUTTON_SIZE
                            + column * BUTTON_SIZE;

            int optionY =
                    this.getY()
                            + row * BUTTON_SIZE;

            renderButtonBackground(graphics, optionX, optionY);

            ResourceLocation icon = SELECTION_ICONS.get(selection);
            renderIcon(graphics, icon, optionX, optionY);

            if (isInsideButton(mouseX, mouseY, optionX, optionY)) {
                renderHoverOverlay(graphics, optionX, optionY);
            }
        }
    }

    /*
     * Zeichnet den 18 × 18 Pixel großen Button-Hintergrund.
     */
    private void renderButtonBackground(
            GuiGraphics graphics,
            int buttonX,
            int buttonY
    ) {
        graphics.blit(
                BUTTON_TEXTURE,
                buttonX,
                buttonY,
                0.0F,
                0.0F,
                BUTTON_SIZE,
                BUTTON_SIZE,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
    }

    /*
     * Zeichnet ein 16 × 16 Pixel großes Icon mittig in den Button.
     */
    private void renderIcon(
            GuiGraphics graphics,
            ResourceLocation icon,
            int buttonX,
            int buttonY
    ) {
        graphics.blit(
                icon,
                buttonX + 1,
                buttonY + 1,
                0.0F,
                0.0F,
                16,
                16,
                16,
                16
        );
    }

    /*
     * Legt eine halbtransparente schwarze Fläche über einen Button.
     * Dadurch wird der Button beim Darüberfahren dunkler.
     */
    private void renderHoverOverlay(
            GuiGraphics graphics,
            int buttonX,
            int buttonY
    ) {
        graphics.fill(
                buttonX,
                buttonY,
                buttonX + BUTTON_SIZE,
                buttonY + BUTTON_SIZE,
                0x55000000
        );
    }

    /*
     * AbstractWidget prüft normalerweise nur Klicks innerhalb seiner eigenen
     * 18 × 18 Pixel.
     *
     * Das ausgeklappte Raster liegt aber außerhalb dieses Bereichs.
     * Deshalb überschreiben wir mouseClicked vollständig und prüfen hier
     * auch die Positionen der neun Auswahlbuttons.
     */
    @Override
    public boolean mouseClicked(
            double mouseX,
            double mouseY,
            int mouseButton
    ) {
        if (!this.visible || !this.active || mouseButton != 0) {
            return false;
        }

        /*
         * Klick auf den Hauptbutton:
         * Auswahlmenü öffnen oder schließen.
         */
        if (isInsideButton(
                mouseX,
                mouseY,
                this.getX(),
                this.getY()
        )) {
            this.expanded = !this.expanded;
            playClickSound();
            return true;
        }

        /*
         * Wenn das Raster geschlossen ist, können keine Optionen
         * angeklickt werden.
         */
        if (!this.expanded) {
            return false;
        }

        /*
         * Prüft, ob eine der neun Forging-Optionen angeklickt wurde.
         */
        ForgingSelection clickedSelection =
                findSelectionAt(mouseX, mouseY);

        if (clickedSelection == null) {
            return false;
        }

        /*
         * Die Auswahl wird lokal gespeichert.
         * Dadurch erscheint das gewählte Icon auf dem Hauptbutton.
         */
        this.selectedSelection = clickedSelection;
        this.expanded = false;

        /*
         * Informiert das serverseitige Menü über die neue Auswahl.
         *
         * Dort wird clickMenuButton(...) aufgerufen.
         */
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.gameMode != null) {
            minecraft.gameMode.handleInventoryButtonClick(
                    this.menu.containerId,
                    clickedSelection.buttonId()
            );
        }

        playClickSound();
        return true;
    }

    /*
     * Sucht die ForgingSelection, deren Button unter dem Mauszeiger liegt.
     *
     * Gibt null zurück, wenn keine Option getroffen wurde.
     */
    @Nullable
    private ForgingSelection findSelectionAt(
            double mouseX,
            double mouseY
    ) {
        for (ForgingSelection selection : ForgingSelection.values()) {
            int buttonId = selection.buttonId();

            int column = buttonId % GRID_COLUMNS;
            int row = buttonId / GRID_COLUMNS;

            int optionX =
                    this.getX()
                            + BUTTON_SIZE
                            + column * BUTTON_SIZE;

            int optionY =
                    this.getY()
                            + row * BUTTON_SIZE;

            if (isInsideButton(
                    mouseX,
                    mouseY,
                    optionX,
                    optionY
            )) {
                return selection;
            }
        }

        return null;
    }

    /*
     * Prüft, ob eine Mausposition innerhalb eines bestimmten
     * 18 × 18 Pixel großen Buttons liegt.
     */
    private boolean isInsideButton(
            double mouseX,
            double mouseY,
            int buttonX,
            int buttonY
    ) {
        return mouseX >= buttonX
                && mouseX < buttonX + BUTTON_SIZE
                && mouseY >= buttonY
                && mouseY < buttonY + BUTTON_SIZE;
    }

    /*
     * Spielt den normalen Minecraft-Button-Klicksound ab.
     *
     * Der Anvil-Sound beim Herausnehmen des geschmiedeten Ergebnisses
     * wird nicht hier abgespielt. Dieser gehört in onTake(...) des
     * Ergebnis-Slots.
     */
    private void playClickSound() {
        Minecraft minecraft = Minecraft.getInstance();

        this.playDownSound(minecraft.getSoundManager());
    }

    /*
     * Ermöglicht dem Screen, die aktuell ausgewählte Option abzufragen.
     */
    @Nullable
    public ForgingSelection getSelectedSelection() {
        return this.selectedSelection;
    }

    /*
     * Ermöglicht dem Screen, eine bereits bekannte Auswahl wiederherzustellen,
     * beispielsweise nachdem die Fenstergröße verändert wurde.
     */
    public void setSelectedSelection(
            @Nullable ForgingSelection selection
    ) {
        this.selectedSelection = selection;
    }

    /*
     * Schließt das Auswahlraster, ohne die aktuelle Auswahl zu löschen.
     */
    public void closeSelectionGrid() {
        this.expanded = false;
    }

    /*
     * Setzt den Hauptbutton wieder auf das Default-Forging-Icon zurück.
     */
    public void clearSelection() {
        this.selectedSelection = null;
        this.expanded = false;
    }

    /*
     * Wird für die Bedienung über Tastatur und Screenreader benötigt.
     */
    @Override
    protected void updateWidgetNarration(
            NarrationElementOutput narrationOutput
    ) {
        this.defaultButtonNarrationText(narrationOutput);
    }
}

