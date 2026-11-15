
package dev.kitkat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import org.lwjgl.glfw.GLFW;

public class KitKatClient implements ClientModInitializer {

    public static final String NAME = "KITKAT CLIENT";
    public static final String VERSION = "1.0.0";

    public static final ModuleManager MODULES =
            new ModuleManager();

    private static KeyBinding openMenu;

    @Override
    public void onInitializeClient() {

        MODULES.registerDefaults();

        openMenu = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.kitkat.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.kitkat"
            )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (openMenu.wasPressed()) {

                if (client.currentScreen == null) {
                    client.setScreen(new ClickGuiScreen());
                } else if (client.currentScreen instanceof ClickGuiScreen) {
                    client.setScreen(null);
                }
            }

            MODULES.tick(client);
        });

        HudRenderCallback.EVENT.register(
            (drawContext, tickCounter) -> {
                MODULES.renderHud(drawContext);
            }
        );

    }
}
