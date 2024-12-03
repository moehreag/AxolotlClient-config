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

package io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets;

import io.github.axolotlclient.AxolotlClientConfig.impl.options.EnumOption;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.ArrayUtils;

public class EnumWidget<T extends Enum<T>> extends Button {
	private final EnumOption<T> option;

	public EnumWidget(int x, int y, int width, int height, EnumOption<T> option) {
		super(x, y, width, height, Component.translatable(String.valueOf(option.get())), widget -> {
		}, DEFAULT_NARRATION);
		this.option = option;
	}

	@Override
	public void onPress() {
		cycle(Screen.hasShiftDown() ? -1 : 1);
	}

	private void cycle(int delta) {
		T[] values = option.getClazz().getEnumConstants();
		int i = ArrayUtils.indexOf(values, option.get());
		i = Mth.positiveModulo(i + delta, values.length);
		option.set(values[i]);
		setMessage(Component.translatable(String.valueOf(option.get())));
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		if (scrollY > 0.0) {
			this.cycle(-1);
		} else if (scrollY < 0.0) {
			this.cycle(1);
		}

		return true;
	}
}
