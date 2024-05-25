package me.waterarchery.litlibs.inventory;

public class Action {

    private final String action;
    private final String guiName;
    private final ActionType type;

    public Action(String action, String guiName, ActionType type) {
        this.action = action;
        this.guiName = guiName;
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public String getGuiName() {
        return guiName;
    }

    public ActionType getType() {
        return type;
    }

}
