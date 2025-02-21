/*
 * Copyright © 2021-2023 moehreag <moehreag@gmail.com> & Contributors
 *
 * This file is part of AxolotlClient.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * For more information, see the LICENSE file.
 */

package io.github.axolotlclient.AxolotlClientConfig.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.github.axolotlclient.AxolotlClientConfig.api.ui.ConfigUI;
import io.github.axolotlclient.AxolotlClientConfig.api.util.WindowPropertiesProvider;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.ConfigUIImpl;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.NVGMC;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.StyleImpl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class AxolotlClientConfigMod implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> AxolotlClientConfigImpl.getInstance().runTick());

		NVGMC.setWindowPropertiesProvider(new WindowPropertiesProvider() {
			@Override
			public int getHeight() {
				return MinecraftClient.getInstance().getFramebuffer().textureHeight;
			}

			@Override
			public int getWidth() {
				return MinecraftClient.getInstance().getFramebuffer().textureWidth;
			}

			@Override
			public float getScaleFactor() {
				return (float) MinecraftClient.getInstance().getWindow().getScaleFactor();
			}
		});

		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public @NotNull Identifier getFabricId() {
				return new Identifier("axolotlclientconfig", "resource_listener");
			}

			@Override
			public void apply(ResourceManager resourceManager) {
				ConfigUIImpl.getInstance().preReload();
				try {
					Map<String, String> vanillaWidgets = new HashMap<>();
					vanillaWidgets.put("boolean", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.BooleanWidget");
					vanillaWidgets.put("string", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.StringWidget");
					vanillaWidgets.put("enum", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.EnumWidget");
					vanillaWidgets.put("string[]", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.StringArrayWidget");
					vanillaWidgets.put("color", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.ColorWidget");
					vanillaWidgets.put("double", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.DoubleWidget");
					vanillaWidgets.put("float", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.FloatWidget");
					vanillaWidgets.put("integer", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.IntegerWidget");
					vanillaWidgets.put("category", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.CategoryWidget");
					vanillaWidgets.put("graphics", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.GraphicsWidget");
					ConfigUI.getInstance().addStyle(new StyleImpl("vanilla", vanillaWidgets, "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.screen.VanillaConfigScreen", null));
					Map<String, String> roundedWidgets = new HashMap<>();
					roundedWidgets.put("boolean", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets.PillBooleanWidget");
					roundedWidgets.put("string[]", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets.StringArrayButtonWidget");
					roundedWidgets.put("category", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets.CategoryWidget");
					roundedWidgets.put("string", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets.StringWidget");
					roundedWidgets.put("enum", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets.EnumWidget");
					roundedWidgets.put("color", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets.ColorWidget");
					roundedWidgets.put("double", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets.DoubleWidget");
					roundedWidgets.put("float", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets.FloatWidget");
					roundedWidgets.put("integer", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets.IntegerWidget");
					roundedWidgets.put("graphics", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets.GraphicsWidget");
					ConfigUI.getInstance().addStyle(new StyleImpl("rounded", roundedWidgets, "io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.screen.RoundedConfigScreen", null));
					Identifier id = new Identifier(ConfigUI.getInstance().getUiJsonPath());
					resourceManager
						.getAllResources(id).forEach(resource -> {
							ConfigUIImpl.getInstance().read(resource.getInputStream());
						});
				} catch (IOException ignored) {
				}
				ConfigUIImpl.getInstance().postReload();
			}
		});
	}
}
