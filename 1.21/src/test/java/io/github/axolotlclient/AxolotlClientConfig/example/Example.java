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

package io.github.axolotlclient.AxolotlClientConfig.example;

import java.util.function.Function;

import com.mojang.blaze3d.platform.InputUtil;
import io.github.axolotlclient.AxolotlClientConfig.api.AxolotlClientConfig;
import io.github.axolotlclient.AxolotlClientConfig.api.manager.ConfigManager;
import io.github.axolotlclient.AxolotlClientConfig.api.options.OptionCategory;
import io.github.axolotlclient.AxolotlClientConfig.api.ui.ConfigUI;
import io.github.axolotlclient.AxolotlClientConfig.api.util.Colors;
import io.github.axolotlclient.AxolotlClientConfig.impl.managers.JsonConfigManager;
import io.github.axolotlclient.AxolotlClientConfig.impl.options.*;
import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBind;

public class Example implements ClientModInitializer {

	@Getter
	private static Example Instance;

	@Override
	public void onInitializeClient() {
		Instance = this;
		final String modid = "axolotlclientconfig-test";

		OptionCategory example = OptionCategory.create(modid);
		example.add(new BooleanOption("boolean", true));
		example.add(new BooleanOption("false", false));

		OptionCategory subCategory = OptionCategory.create("sub-category");
		subCategory.add(new BooleanOption("some option", true));
		example.add(subCategory);
		OptionCategory sub2 = OptionCategory.create("sub2");
		sub2.add(new BooleanOption("§0colored 1", false));
		sub2.add(new BooleanOption("§1colored 2", false));
		sub2.add(new BooleanOption("§2colored 3", false));
		sub2.add(new BooleanOption("§3colored 4", false));
		sub2.add(new BooleanOption("§4colored 5", false));
		sub2.add(new BooleanOption("§5colored 6", false));
		sub2.add(new BooleanOption("§6colored 7", false));
		sub2.add(new BooleanOption("§7colored 8", false));
		sub2.add(new BooleanOption("§8colored 9", false));
		sub2.add(new BooleanOption("§9colored 10", false));
		sub2.add(new BooleanOption("§acolored 11", false));
		sub2.add(new BooleanOption("§bcolored 12", false));
		sub2.add(new BooleanOption("§ccolored 13", false));
		sub2.add(new BooleanOption("§dcolored 14", false));
		sub2.add(new BooleanOption("§ecolored 15", false));
		sub2.add(new BooleanOption("§fcolored 16", false));
		sub2.add(new BooleanOption("§kobfuscated", false));
		sub2.add(new BooleanOption("§lbold", false));
		sub2.add(new BooleanOption("§mstrikethrough", false));
		sub2.add(new BooleanOption("§nunderlined", false));
		sub2.add(new BooleanOption("§oitalic", false));
		sub2.add(new BooleanOption("§rreset", false));
		example.add(sub2);
		OptionCategory sub3 = OptionCategory.create("sub3");
		example.add(sub3);
		sub3.add(new StringArrayOption("loooooooooooooooooooooooooooooooong option name", "value1", "value2"));

		example.add(new IntegerOption("integer", 5, 0, 8));
		example.add(new FloatOption("float", 3f, 1f, 5f));
		example.add(new DoubleOption("double", 8d, 5d, 12d));
		example.add(new ColorOption("color", Colors.GREEN));

		example.add(new StringOption("string option", "default value"));
		example.add(new BooleanOption("option", false));
		example.add(new BooleanOption("other option", true));
		example.add(new GraphicsOption("graphics", 40, 40));
		example.add(new EnumOption<>("enum", TestEnum.class, TestEnum.TEST_ENUM1));
		example.add(new StringArrayOption("another string array", "value 1", "value 2", "value 3"));

		AxolotlClientConfig.getInstance().register(new JsonConfigManager(FabricLoader.getInstance().getConfigDir().resolve(modid + ".json"), example));

		KeyBind bind = new KeyBind(modid, InputUtil.KEY_O_CODE, modid);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (bind.wasPressed()) {
				client.setScreen(getConfigScreenFactory(modid).apply(client.currentScreen));
			}
		});

		ConfigUI.getInstance().runWhenLoaded(() -> {
			StringArrayOption option;
			example.add(option = new StringArrayOption("style",
				ConfigUI.getInstance().getStyleNames().toArray(new String[0]),
				ConfigUI.getInstance().getCurrentStyle().getName(), s -> {
				ConfigUI.getInstance().setStyle(s);
				MinecraftClient.getInstance().currentScreen.closeScreen();
			}));
			AxolotlClientConfig.getInstance().getConfigManager(modid).load();
			ConfigUI.getInstance().setStyle(option.get());
		});
	}

	public enum TestEnum {
		TEST_ENUM1,
		TEST_ENUM2,
		TEST_ENUM3,
	}

	public Function<Screen, ? extends Screen> getConfigScreenFactory(String name) {
		ConfigManager manager = AxolotlClientConfig.getInstance().getConfigManager(name);
		return parent -> (Screen) ConfigUI.getInstance().getScreen(this.getClass().getClassLoader(),
			manager.getRoot(), parent);
	}


}
