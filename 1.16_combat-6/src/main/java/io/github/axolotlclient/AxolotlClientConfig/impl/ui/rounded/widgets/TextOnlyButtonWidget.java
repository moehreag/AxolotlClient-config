/*
 * Copyright © 2021-2024 moehreag <moehreag@gmail.com> & Contributors
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

package io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets;

import io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.NVGHolder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class TextOnlyButtonWidget extends RoundedButtonWidget {
	private final Text content, highlightedContent;

	public TextOnlyButtonWidget(int x, int y, Text message, PressAction action) {
		this(x, y, (int) NVGHolder.getFont().getWidth(message.getString()), (int) NVGHolder.getFont().getLineHeight(), message, action);
	}

	private TextOnlyButtonWidget(int x, int y, int width, int height, Text message, PressAction action) {
		super(x, y, width, height, message, action);
		this.content = message;
		this.highlightedContent = content.copy().setStyle(Style.EMPTY.withUnderline(true));
	}

	public static TextOnlyButtonWidget centeredWidget(int centerX, int y, Text message, PressAction action) {
		float textWidth = NVGHolder.getFont().getWidth(message.getString());
		return new TextOnlyButtonWidget((int) (centerX - textWidth / 2), y, (int) textWidth, (int) NVGHolder.getFont().getLineHeight(), message, action);
	}

	@Override
	public void renderButton(MatrixStack graphics, int mouseX, int mouseY, float delta) {
		Text message = isHovered() ? highlightedContent : content;
		drawString(NVGHolder.getContext(), NVGHolder.getFont(), message.getString(), getX(), this.getY(),
			(this.active ? activeColor : inactiveColor).withAlpha((int) (alpha * 255)));
	}
}
