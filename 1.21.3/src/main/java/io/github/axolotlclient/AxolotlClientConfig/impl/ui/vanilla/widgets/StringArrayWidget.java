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

import io.github.axolotlclient.AxolotlClientConfig.impl.options.StringArrayOption;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.ArrayUtils;

public class StringArrayWidget extends Button {

	private final StringArrayOption option;

	public StringArrayWidget(int x, int y, int width, int height, StringArrayOption option) {
		super(x, y, width, height, Component.translatable(option.get()), widget -> {
		}, DEFAULT_NARRATION);
		this.option = option;
	}

	@Override
	protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		if (!getMessage().getString().equals(Component.translatable(option.get()).getString())) {
			setMessage(Component.translatable(option.get()));
		}
		super.renderWidget(graphics, mouseX, mouseY, delta);
	}

	@Override
	public void onPress() {
		cycle(Screen.hasShiftDown() ? -1 : 1);
	}

	private void cycle(int delta) {
		String[] values = option.getValues();
		int i = ArrayUtils.indexOf(values, option.get());
		i = Mth.positiveModulo(i + delta, values.length);
		option.set(values[i]);
		setMessage(Component.translatable(option.get()));
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
