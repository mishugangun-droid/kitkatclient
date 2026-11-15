
package dev.kitkat;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    public void registerDefaults() {

        // COMBAT
        add("AutoBow", "Bow module", Module.Category.COMBAT, false);
        add("AutoHeal", "Heal module", Module.Category.COMBAT, false);
        add("AutoTotem", "Totem module", Module.Category.COMBAT, false);
        add("KillAura", "Combat automation", Module.Category.COMBAT, false);
        add("HitBoxes", "Hitbox visualization", Module.Category.COMBAT, false);

        // MOVEMENT
        add("AutoJump", "Automatic jumping", Module.Category.MOVEMENT, true);
        add("AutoSprint", "Automatic sprint", Module.Category.MOVEMENT, true);
        add("EdgeJump", "Edge jumping", Module.Category.MOVEMENT, false);
        add("Eagle", "Edge assistance", Module.Category.MOVEMENT, false);
        add("Flight", "Flight module", Module.Category.MOVEMENT, false);

        // PLAYER
        add("AutoEat", "Automatic eating", Module.Category.PLAYER, false);
        add("AutoJoin", "Server joining", Module.Category.PLAYER, false);
        add("AutoTool", "Automatic tool", Module.Category.PLAYER, false);
        add("ClickPearl", "Pearl assistance", Module.Category.PLAYER, false);

        // RENDER
        add("Animations", "Visual animations", Module.Category.RENDER, true);
        add("ArmorDurability", "Armor display", Module.Category.RENDER, false);
        add("Arrows", "Directional arrows", Module.Category.RENDER, false);
        add("AspectRatio", "Aspect ratio", Module.Category.RENDER, false);
        add("BlockESP", "Block highlighting", Module.Category.RENDER, false);
        add("Crosshair", "Custom crosshair", Module.Category.RENDER, true);
        add("EntityESP", "Entity highlighting", Module.Category.RENDER, false);

        // MISC
        add("HUD", "Client HUD", Module.Category.MISC, true);
        add("FPS", "FPS counter", Module.Category.MISC, true);
        add("Coordinates", "Coordinates display", Module.Category.MISC, true);
        add("Fullbright", "Brightness", Module.Category.MISC, true);
    }

    private void add(String name, String description,
                     Module.Category category, boolean available) {
        modules.add(new Module(name, description, category, available));
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModules(Module.Category category) {
        List<Module> result = new ArrayList<>();

        for (Module module : modules) {
            if (module.getCategory() == category) {
                result.add(module);
            }
        }

        return result;
    }

    public Module get(String name) {
        for (Module module : modules) {
            if (module.getName().equals(name)) {
                return module;
            }
        }
        return null;
    }

    public void tick(MinecraftClient client) {
        if (client.player == null) return;

        Module sprint = get("AutoSprint");

        if (sprint != null && sprint.isEnabled()
                && client.player.forwardSpeed > 0
                && !client.player.isSneaking()) {
            client.player.setSprinting(true);
        }

        Module jump = get("AutoJump");

        if (jump != null && jump.isEnabled()
                && client.player.isOnGround()
                && client.player.forwardSpeed > 0) {
            client.player.jump();
        }

        Module bright = get("Fullbright");

        if (bright != null) {
            client.options.getGamma().setValue(
                bright.isEnabled() ? 16.0 : 1.0
            );
        }
    }

    public void renderHud(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) return;

        int y = 10;

        Module hud = get("HUD");

        if (hud != null && hud.isEnabled()) {
            context.fill(8, 7, 145, 27, 0xBB35101C);
            context.drawText(
                client.textRenderer,
                "KITKAT CLIENT",
                14, 13,
                0xFFFF7777,
                true
            );
            y = 34;
        }

        Module fps = get("FPS");

        if (fps != null && fps.isEnabled()) {
            context.drawText(
                client.textRenderer,
                "FPS: " + client.getCurrentFps(),
                10, y,
                0xFFFFAAAA,
                true
            );
            y += 13;
        }

        Module coords = get("Coordinates");

        if (coords != null && coords.isEnabled()) {
            String position = String.format(
                "XYZ: %.1f / %.1f / %.1f",
                client.player.getX(),
                client.player.getY(),
                client.player.getZ()
            );

            context.drawText(
                client.textRenderer,
                position,
                10, y,
                0xFFFFAAAA,
                true
            );
        }
    }
}
