
package dev.kitkat;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClickGuiScreen extends Screen {

    private static final int BACKGROUND = 0xFF171113;
    private static final int PANEL = 0xFF291B20;
    private static final int HEADER = 0xFF42232B;
    private static final int RED = 0xFFFF555F;
    private static final int WHITE = 0xFFFFF0E8;
    private static final int MUTED = 0xFF99858A;
    private static final int ENABLED = 0xFFB52E38;
    private static final int DISABLED = 0xFF51464A;

    private final String[] categories = {
        "Combat", "Movement", "Player",
        "Render", "Misc", "Configs"
    };

    private int startX;
    private int startY;
    private int columnWidth;
    private final int gap = 7;

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
        startY = Math.max(25, (height - 430) / 2);
    }

    @Override
    public void render(
        DrawContext ctx,
        int mouseX,
        int mouseY,
        float delta
    ) {
        calculateLayout();

        ctx.fill(0, 0, width, height, BACKGROUND);

        renderTopBar(ctx);

        for (int i = 0; i < categories.length; i++) {
            int x = startX + i * (columnWidth + gap);

            renderPanel(ctx, x, i);

            if (i < 5) {
                renderModules(ctx, x, mouseX, mouseY, i);
            } else {
                renderConfigs(ctx, x);
            }
        }

        ctx.drawCenteredTextWithShadow(
            textRenderer,
            "KITKAT CLIENT 1.0.0 | RIGHT SHIFT",
            width / 2,
            height - 18,
            RED
        );

        super.render(ctx, mouseX, mouseY, delta);
    }

    private void renderTopBar(DrawContext ctx) {
        int x = startX;
        int y = startY - 24;
        int w = columnWidth * 6 + gap * 5;

        ctx.fill(x, y, x + w, y + 21, PANEL);
        ctx.fill(x, y, x + w, y + 2, RED);

        ctx.drawText(
            textRenderer,
            "KITKAT CLIENT",
            x + 8,
            y + 7,
            WHITE,
            true
        );

        String version = "1.0.0";

        ctx.drawText(
            textRenderer,
            version,
            x + w - textRenderer.getWidth(version) - 8,
            y + 7,
            MUTED,
            false
        );
    }

    private void renderPanel(
        DrawContext ctx,
        int x,
        int category
    ) {
        int bottom = height - 30;

        ctx.fill(
            x,
            startY,
            x + columnWidth,
            bottom,
            PANEL
        );

        ctx.fill(
            x,
            startY,
            x + columnWidth,
            startY + 32,
            HEADER
        );

        ctx.fill(
            x,
            startY,
            x + 3,
            startY + 32,
            RED
        );

        ctx.drawCenteredTextWithShadow(
            textRenderer,
            categories[category],
            x + columnWidth / 2,
            startY + 12,
            WHITE
        );

        ctx.fill(
            x + 7,
            startY + 35,
            x + columnWidth - 7,
            startY + 36,
            0xFF60434A
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

        int currentY = startY + 43;

        for (Module module :
            KitKatClient.MODULES.getModules(cat)) {

            if (currentY + 22 > height - 35) break;

            boolean hover =
                mouseX >= x + 5 &&
                mouseX <= x + columnWidth - 5 &&
                mouseY >= currentY - 2 &&
                mouseY <= currentY + 20;

            if (hover) {
                ctx.fill(
                    x + 5,
                    currentY - 2,
                    x + columnWidth - 5,
                    currentY + 20,
                    0xFF493038
                );
            }

            int color = !module.isAvailable()
                ? MUTED
                : module.isEnabled() ? RED : WHITE;

            ctx.drawText(
                textRenderer,
                module.getName(),
                x + 9,
                currentY + 5,
                color,
                false
            );

            int switchX = x + columnWidth - 23;

            ctx.fill(
                switchX,
                currentY + 5,
                switchX + 14,
                currentY + 15,
                !module.isAvailable()
                    ? 0xFF393237
                    : module.isEnabled()
                        ? ENABLED : DISABLED
            );

            ctx.fill(
                module.isEnabled()
                    ? switchX + 8 : switchX + 2,
                currentY + 7,
                module.isEnabled()
                    ? switchX + 12 : switchX + 6,
                currentY + 13,
                WHITE
            );

            currentY += 24;
        }
    }

    private void renderConfigs(DrawContext ctx, int x) {
        ctx.drawText(
            textRenderer,
            "THEME",
            x + 9,
            startY + 48,
            RED,
            true
        );

        ctx.drawText(
            textRenderer,
            "KitKat Dark",
            x + 9,
            startY + 66,
            WHITE,
            false
        );

        ctx.drawText(
            textRenderer,
            "COLORS",
            x + 9,
            startY + 101,
            RED,
            true
        );

        int[] colors = {
            0xFFFF555F,
            0xFFB52E38,
            0xFF7D2630,
            0xFFFF9999,
            0xFFFFD1B8,
            0xFFFFFFFF
        };

        for (int i = 0; i < colors.length; i++) {
            int cx = x + 9 + i * 19;

            ctx.fill(
                cx,
                startY + 122,
                cx + 13,
                startY + 135,
                colors[i]
            );
        }

        ctx.drawText(
            textRenderer,
            "CONFIGS",
            x + 9,
            startY + 159,
            RED,
            true
        );

        ctx.fill(
            x + 7,
            startY + 179,
            x + columnWidth - 7,
            startY + 202,
            HEADER
        );

        ctx.drawText(
            textRenderer,
            "Default",
            x + 12,
            startY + 187,
            WHITE,
            false
        );

        ctx.drawText(
            textRenderer,
            "Fabric 1.21.4",
            x + 9,
            startY + 222,
            MUTED,
            false
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
                int currentY = startY + 43;

                for (Module module :
                    KitKatClient.MODULES.getModules(
                        Module.Category.values()[i])) {

                    if (currentY + 22 > height - 35) {
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

