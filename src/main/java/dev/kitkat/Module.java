
package dev.kitkat;

public class Module {

    public enum Category {
        COMBAT,
        MOVEMENT,
        PLAYER,
        RENDER,
        MISC
    }

    private final String name;
    private final String description;
    private final Category category;

    private boolean enabled;
    private final boolean available;

    public Module(
        String name,
        String description,
        Category category,
        boolean available
    ) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.available = available;
        this.enabled = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAvailable() {
        return available;
    }

    public void toggle() {
        if (available) {
            enabled = !enabled;
        }
    }

    public void setEnabled(boolean enabled) {
        if (available) {
            this.enabled = enabled;
        }
    }
}
