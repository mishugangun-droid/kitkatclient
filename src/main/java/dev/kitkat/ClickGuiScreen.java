
package dev.kitkat;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClickGuiScreen extends Screen {

    private static final int BG = 0xB8080507;
    private static final int PANEL = 0xD91D1519;
    private static final int PANEL_INNER = 0x665A3038;
    private static final int RED = 0xFFFF555F;
    private static final int RED_DARK = 0xFF9E2934;
    private static final int WHITE = 0xFFFFF3ED;
    private static final int MUTED = 0xFFAA9699;
    private static final int GREEN = 0xFF77D9A0;

    private final String[] categories = {
        "Combat", "Movement", "Player",
        "Render", "Misc", "Configs"
    };

    private int startX;
    private int startY;
    private int columnWidth;
    private int gap = 7;
    private int panelBottom;

    public ClickGuiScreen() {
        super(Text.literal("KITKAT CLIENT"));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void calculateLayout() {
        int margin = 10;

        columnWidth = Math.min(
            170,
            Math.max(85,
                (width - margin * 2 - gap * 5) / 6)
        );

        int total = columnWidth * 6 + gap * 5;

        startX = (width - total) / 2;
        startY = Math.max(15, (height - 430) / 2);

        panelBottom = height - 35;
    }

    @Override
    public void render(
        DrawContext ctx,
        int mouseX,
        int mouseY,
        float delta
    ) {
        calculateLayout();

        ctx.fill(0, 0, width, height, BG);

        renderTopBar(ctx);

        for (int i = 0; i < categories.length; i++) {
            int x = startX + i * (columnWidth + gap);

            renderPanel(ctx, x, i);

            if (i < 5) {
                renderModules(
                    ctx, x, mouseX, mouseY, i
                );
            } else {
                renderConfigs(ctx, x, mouseX, mouseY);
            }
        }

        ctx.drawCenteredTextWithShadow(
            textRenderer,
            "KITKAT CLIENT  |  1.0.0  |  RIGHT SHIFT",
            width / 2,
            height - 19,
            RED
        );

        super.render(ctx, mouseX, mouseY, delta);
    }

    private void renderTopBar(DrawContext ctx) {
        int x = startX;
        int y = Math.max(2, startY - 29);
        int w = columnWidth * 6 + gap * 5;

        ctx.fill(x, y, x + w, y + 23, 0xDD211519);
        ctx.fill(x, y, x + w, y + 1, RED);
        ctx.fill(x, y + 22, x + w, y + 23, 0x66FFFFFF);

        ctx.drawText(
            textRenderer,
            "KITKAT",
            x + 9,
            y + 8,
            RED,
            true
        );

        ctx.drawText(
            textRenderer,
            "CLIENT",
            x + 48,
            y + 8,
            WHITE,
            false
        );

        String status = "FABRIC 1.21.4";

        ctx.drawText(
            textRenderer,
            status,
            x + w - textRenderer.getWidth(status) - 9,
            y + 8,
            MUTED,
            false
        );
    }

    private void renderPanel(
        DrawContext ctx,
        int x,
        int category
    ) {
        int w = columnWidth;
        int y = startY;
        int bottom = panelBottom;

        // Glass shadow
        ctx.fill(
            x + 3, y + 4,
            x + w + 3, bottom + 4,
            0x55000000
        );

        // Main glass body
        ctx.fill(
            x + 2, y,
            x + w - 2, bottom,
            PANEL
        );

        ctx.fill(
            x, y + 3,
            x + w, bottom - 3,
            PANEL
        );

        // Glass reflections
        ctx.fill(
            x + 4, y + 2,
            x + w - 4, y + 3,
            0x99FFFFFF
        );

        ctx.fill(
            x + 2, y + 4,
            x + 3, bottom - 4,
            0x55FFFFFF
        );

        ctx.fill(
            x + 3, bottom - 3,
            x + w - 3, bottom - 2,
            0x44FFFFFF
        );

        // Header glass
        ctx.fill(
            x + 3, y + 4,
            x + w - 3, y + 34,
            PANEL_INNER
        );

        // Red accent
        ctx.fill(
            x + 4, y + 4,
            x + 7, y + 34,
            RED
        );

        ctx.drawCenteredTextWithShadow(
            textRenderer,
            categories[category],
            x + w / 2,
            y + 15,
            WHITE
        );

        ctx.fill(
            x + 7, y + 37,
            x + w - 7, y + 38,
            0x44FFFFFF
        );
    }

    private void renderModules(
        DrawContext ctx,
        int x,
        int mouseX,
        int mouseY,
        int category
    ) {
        Module.Category cat =
            Module.Category.values()[category];

        int currentY = startY + 44;
        int available = 0;
        int enabled = 0;

        for (Module module :
            KitKatClient.MODULES.getModules(cat)) {
            if (module.isAvailable()) available++;
            if (module.isEnabled()) enabled++;
        }

        String count = enabled + "/" +
            KitKatClient.MODULES.getModules(cat).size();

        ctx.drawText(
            textRenderer,
            count,
            x + 9,
            startY + 40,
            MUTED,
            false
        );

        currentY += 15;

        for (Module module :
            KitKatClient.MODULES.getModules(cat)) {

            if (currentY + 22 > panelBottom - 5) {
                break;
            }

            int left = x + 5;
            int right = x + columnWidth - 5;

            boolean hover =
                mouseX >= left &&
                mouseX <= right &&
                mouseY >= currentY - 2 &&
                mouseY <= currentY + 20;

            if (hover) {
                ctx.fill(
                    left, currentY - 2,
                    right, currentY + 20,
                    0x554E3038
                );

                ctx.fill(
                    left, currentY - 2,
                    left + 2, currentY + 20,
                    RED
                );
            }

            int textColor = !module.isAvailable()
                ? MUTED
                : module.isEnabled() ? RED : WHITE;

            ctx.drawText(
                textRenderer,
                module.getName(),
                x + 10,
                currentY + 5,
                textColor,
                false
            );

            // Switch background
            int switchX = x + columnWidth - 25;

            ctx.fill(
                switchX,
                currentY + 5,
                switchX + 17,
                currentY + 15,
                !module.isAvailable()
                    ? 0xFF393237
                    : module.isEnabled()
                        ? RED_DARK
                        : 0xFF51464A
            );

            // Switch indicator
            int knobX = module.isEnabled()
                ? switchX + 10
                : switchX + 2;

            int knobColor = module.isEnabled()
                ? WHITE
                : 0xFFB6A9AC;

            ctx.fill(
                knobX,
                currentY + 7,
                knobX + 5,
                currentY + 13,
                knobColor
            );

            currentY += 24;
        }
    }

    private void renderConfigs(
        DrawContext ctx,
        int x,
        int mouseX,
        int mouseY
    ) {
        int y = startY;

        drawSection(ctx, "THEME", x + 10, y + 49);

        ctx.drawText(
            textRenderer,
            "Liquid Glass",
            x + 10,
            y + 65,
            WHITE,
            false
        );

        ctx.drawText(
            textRenderer,
            "KITKAT EDITION",
            x + 10,
            y + 80,
            MUTED,
            false
        );

        drawSection(ctx, "COLORS", x + 10, y + 108);

        int[] colors = {
            0xFFFF555F,
            0xFFB52E38,
            0xFF7D2630,
            0xFFFF9999,
            0xFFFFD1B8,
            0xFFFFFFFF
        };

        for (int i = 0; i < colors.length; i++) {
            int cx = x + 10 + i * 20;
            int cy = y + 126;

            ctx.fill(
                cx - 1, cy - 1,
                cx + 14, cy + 14,
                0xAAFFFFFF
            );

            ctx.fill(
                cx, cy,
                cx + 12, cy + 12,
                colors[i]
            );
        }

        drawSection(ctx, "CONFIGS", x + 10, y + 165);

        int configY = y + 183;

        ctx.fill(
            x + 7, configY,
            x + columnWidth - 7, configY + 22,
            0x664D3038
        );

        ctx.fill(
            x + 7, configY,
            x + 9, configY + 22,
            RED
        );

        ctx.drawText(
            textRenderer,
            "Default",
            x + 14,
            configY + 7,
            WHITE,
            false
        );

        drawSection(ctx, "INFORMATION", x + 10, y + 229);

        ctx.drawText(
            textRenderer,
            "Version 1.0.0",
            x + 10,
            y + 247,
            MUTED,
            false
        );

        ctx.drawText(
            textRenderer,
            "Fabric 1.21.4",
            x + 10,
            y + 262,
            MUTED,
            false
        );

        ctx.fill(
            x + 9, y + 285,
            x + columnWidth - 9, y + 286,
            0x44FFFFFF
        );

        ctx.drawText(
            textRenderer,
            "Made with KitKat",
            x + 10,
            y + 296,
            RED,
            false
        );
    }

    private void drawSection(
        DrawContext ctx,
        String title,
        int x,
        int y
    ) {
        ctx.drawText(
            textRenderer,
            title,
            x,
            y,
            RED,
            true
        );
    }

    @Override
    public boolean mouseClicked(
        double mouseX,
        double mouseY,
        int button
    ) {
        if (button == 0) {
            calculateLayout();

            for (int i = 0; i < 5; i++) {
                int x = startX + i * (columnWidth + gap);
                int currentY = startY + 59;

                for (Module module :
                    KitKatClient.MODULES.getModules(
                        Module.Category.values()[i])) {

                    if (currentY + 22 > panelBottom - 5) {
                        break;
                    }

                    if (mouseX >= x + 5 &&
                        mouseX <= x + columnWidth - 5 &&
                        mouseY >= currentY - 2 &&
                        mouseY <= currentY + 20) {

                        if (module.isAvailable()) {
                            module.toggle();
                        }

                        return true;
                    }

                    currentY += 24;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(
        int key,
        int scanCode,
        int modifiers
    ) {
        if (key == GLFW.GLFW_KEY_ESCAPE ||
            key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            close();
            return true;
        }

        return super.keyPressed(key, scanCode, modifiers);
    }
}

