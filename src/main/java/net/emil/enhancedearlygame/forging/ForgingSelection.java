package net.emil.enhancedearlygame.forging;

public enum ForgingSelection {
    PICKAXE_HEAD(0,3),
    AXE_HEAD(1, 3),
    SHOVEL_HEAD(2, 1),
    SWORD_BLADE(3, 2),
    HOE_HEAD(4, 2),
    HELMET(5, 5),
    CHESTPLATE(6, 8),
    LEGGINGS(7, 7),
    BOOTS(8, 4);

    private final int buttonId;
    private final int materialCost;

    ForgingSelection(int buttonId,int materialCost) {
        this.buttonId = buttonId;
        this.materialCost = materialCost;
    }

    public int buttonId() {
        return buttonId;
    }

    public int MaterialCost() {
        return materialCost;
    }

    public static ForgingSelection byButtonId(int id) {
        for (ForgingSelection selection : values()) {
            if (selection.buttonId == id) {
                return selection;

            }
        }

        return null;
    }
}
