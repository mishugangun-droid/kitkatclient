
package dev.kitkat;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClickGuiScreen extends Screen {

    private static final int PANEL = 0xDD21191D;
    private static final int GLASS = 0x665C343B;
    private static final int RED = 0xFFFF7777;
    private static final int WHITE = 0xFFF4E9E9;
    private static final int MUTED = 0xFF95868A;

    private final String[] categories = {
        "Combat", "Movement", "Player",
        "Render", "Misc", "Configs"
    };

    public ClickGuiScreen() {
        super(Text.literal("KITKAT CLIENT"));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext ctx, int mouseX,
                       int mouseY, float delta) {

        ctx.fill(0, 0, width, height, 0x99050008);

        int gap = 8;
        int margin = 12;
        int columnWidth = Math.min(155,
            (width - margin * 2 - gap * 5) / 6);

        int total = columnWidth * 6 + gap * 5;
        int startX = (width - total) / 2;
        int startY = Math.max(25, (height - 440) / 2);

        for (int i = 0; i < categories.length; i++) {

            int x = startX + i * (columnWidth + gap);

            int bottom = Math.min(height - 30, startY + 440);

            glassPanel(ctx, x, startY,
                x + columnWidth, bottom);

            ctx.fill(x + 2, startY + 2,
                x + columnWidth - 2, startY + 34, GLASS);

            ctx.drawCenteredTextWithShadow(
                textRenderer,
                categories[i],
                x + columnWidth / 2,
                startY + 13,
                WHITE
            );

            if (i < 5) {
                renderModules(ctx, x, startY,
                    columnWidth, mouseX, mouseY, i);
            } else {
                renderConfigs(ctx, x, startY);
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

    private void renderModules(
        DrawContext ctx,
        int x,
        int y,
        int w,
        int mouseX,
        int mouseY,
        int category
    ) {

        Module.Category cat =
            Module.Category.values()[category];

        int currentY = y + 43;

        for (Module module :
            KitKatClient.MODULES.getModules(cat)) {

            if (currentY > height - 45) break;

            boolean hover =
                mouseX >= x + 5 &&
                mouseX <= x + w - 5 &&
                mouseY >= currentY - 3 &&
                mouseY <= currentY + 20;

            if (hover) {
                ctx.fill(
                    x + 5, currentY - 3,
                    x + w - 5, currentY + 21,
                    0x664B3038
                );
            }

            int color = !module.isAvailable()
                ? MUTED
                : module.isEnabled() ? RED : WHITE;

            ctx.drawText(
                textRenderer,
                module.getName(),
                x + 10,
                currentY + 4,
                color,
                false
            );

            int switchColor = !module.isAvailable()
                ? 0xFF494044
                : module.isEnabled() ? RED : 0xFF55474B;

            ctx.fill(
                x + w - 19,
                currentY + 7,
                x + w - 10,
                currentY + 15,
                switchColor
            );

            currentY += 25;
        }
    }

    private void renderConfigs(
        DrawContext ctx, int x, int y
    ) {

        ctx.drawText(textRenderer,
            "THEME", x + 12, y + 48, RED, false);

        ctx.drawText(textRenderer,
            "Liquid Glass", x + 12, y + 72,
            WHITE, false);

        ctx.drawText(textRenderer,
            "COLORS", x + 12, y + 110,
            RED, false);

        int[] colors = {
            0xFFFF5555,
            0xFFB52E38,
            0xFF7D2630,
            0xFFFF9999,
            0xFFFFD1B8,
            0xFFFFFFFF
        };

        for (int i = 0; i < colors.length; i++) {
            int cx = x + 12 + i * 19;
            ctx.fill(cx, y + 135, cx + 13, y + 148,
                colors[i]);
        }

        ctx.drawText(textRenderer,
            "CONFIGS", x + 12, y + 180,
            RED, false);

        ctx.drawText(textRenderer,
            "Default", x + 12, y + 205,
            WHITE, false);

        ctx.drawText(textRenderer,
            "KITKAT CLIENT", x + 12, y + 240,
            MUTED, false);
    }

    private void glassPanel(
        DrawContext ctx,
        int x1, int y1,
        int x2, int y2
    ) {

        ctx.fill(x1 + 3, y1, x2 - 3, y2, PANEL);
        ctx.fill(x1, y1 + 3, x2, y2 - 3, PANEL);

        ctx.fill(x1 + 3, y1 + 1,
            x2 - 3, y1 + 2, 0x66FFAAAA);

        ctx.fill(x1 + 3, y2 - 2,
            x2 - 3, y2 - 1, 0x44FFFFFF);
    }

    @Override
    public boolean mouseClicked(
        double mouseX,
        double mouseY,
        int button
    ) {

        if (button == 0) {

            int gap = 8;
            int margin = 12;

            int columnWidth = Math.min(155,
                (width - margin * 2 - gap * 5) / 6);

            int total = columnWidth * 6 + gap * 5;
            int startX = (width - total) / 2;
            int startY = Math.max(25, (height - 440) / 2);

            for (int i = 0; i < 5; i++) {

                int x = startX + i * (columnWidth + gap);
                int currentY = startY + 43;

                for (Module module :
                    KitKatClient.MODULES.getModules(
                        Module.Category.values()[i])) {

                    if (mouseX >= x + 5 &&
                        mouseX <= x + columnWidth - 5 &&
                        mouseY >= currentY - 3 &&
                        mouseY <= currentY + 21) {

                        module.toggle();
                        return true;
                    }

                    currentY += 25;
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
