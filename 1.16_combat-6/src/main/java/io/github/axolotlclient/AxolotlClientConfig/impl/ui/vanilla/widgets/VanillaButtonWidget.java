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

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.Selectable;
import io.github.axolotlclient.AxolotlClientConfig.impl.util.DrawUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class VanillaButtonWidget extends ButtonWidget implements Selectable {
	public VanillaButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
		super(x, y, width, height, message, onPress);
	}

	public boolean isHovered() {
		return hovered;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		minecraftClient.getTextureManager().bindTexture(WIDGETS_LOCATION);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
		int i = this.getYImage(this.isHovered());
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		this.drawTexture(matrices, this.getX(), this.getY(), 0, 46 + i * 20, this.width / 2, this.height);
		this.drawTexture(matrices, this.getX() + this.width / 2, this.getY(), 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
		int textColor = this.active ? 16777215 : 10526880;
		this.drawScrollableText(matrices, minecraftClient.textRenderer, textColor | MathHelper.ceil(this.alpha * 255.0F) << 24);
	}

	public void drawScrollableText(MatrixStack matrices, TextRenderer renderer, int color) {
		this.drawScrollableText(matrices, renderer, 2, color);
	}

	protected void drawScrollableText(MatrixStack matrices, TextRenderer textRenderer, int xOffset, int color) {
		int i = this.getX() + xOffset;
		int j = this.getX() + this.getWidth() - xOffset;
		DrawUtil.drawScrollingText(matrices, textRenderer, this.getMessage(), i + j / 2, i, this.getY(), j, this.getY() + this.getHeight(), color);
	}

	@Override
	public SelectionType getType() {
		return isHovered() ? SelectionType.HOVERED : (isFocused() ? SelectionType.FOCUSED : SelectionType.NONE);
	}
}
