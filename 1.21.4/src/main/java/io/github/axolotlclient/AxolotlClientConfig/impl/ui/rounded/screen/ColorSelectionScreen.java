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

package io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.screen;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.axolotlclient.AxolotlClientConfig.api.util.Color;
import io.github.axolotlclient.AxolotlClientConfig.api.util.Colors;
import io.github.axolotlclient.AxolotlClientConfig.impl.options.BooleanOption;
import io.github.axolotlclient.AxolotlClientConfig.impl.options.ColorOption;
import io.github.axolotlclient.AxolotlClientConfig.impl.options.FloatOption;
import io.github.axolotlclient.AxolotlClientConfig.impl.options.IntegerOption;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.DrawingUtil;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.Updatable;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.NVGHolder;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.NVGUtil;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets.RoundedButtonWidget;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets.TextFieldWidget;
import io.github.axolotlclient.AxolotlClientConfig.impl.util.ConfigStyles;
import io.github.axolotlclient.AxolotlClientConfig.impl.util.DrawUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.nanovg.NanoVG.*;

public class ColorSelectionScreen extends Screen implements DrawingUtil {
	private final ColorOption option;
	private final Screen parent;
	private NVGPaint paint;
	private BooleanOption chroma;
	private FloatOption speed;
	private IntegerOption alpha;
	private int selectorRadius;
	private float selectorX;
	private float selectorY;
	private int buttonsX;

	public ColorSelectionScreen(Screen parent, ColorOption option) {
		super(Component.translatable("select_color"));
		this.option = option;
		this.parent = parent;
	}

	@Override
	public void init() {
		addRenderableWidget(new RoundedButtonWidget(width / 2 - 75, height - 40, Component.translatable("gui.back"),
			button -> minecraft.setScreen(parent)));

		chroma = new BooleanOption("option.chroma", option.getOriginal().isChroma(), option.getOriginal()::setChroma);
		speed = new FloatOption("option.speed", option.getOriginal().getChromaSpeed(), option.getOriginal()::setChromaSpeed, 0f, 4f);
		alpha = new IntegerOption("option.alpha", option.getOriginal().getAlpha(), val -> {
			option.getOriginal().setAlpha(val);
			children().forEach(e -> {
				if (e instanceof TextFieldWidget) {
					((TextFieldWidget) e).setText(option.getOriginal().toString().split(";")[0]);
				}
			});
		}, 0, 255);

		selectorRadius = Math.max(Math.min(width / 4 - 10, (height) / 2 - 60), 75);
		selectorX = width / 4f - selectorRadius;//width/2f-selectorRadius*2;
		selectorY = height / 2f - selectorRadius;//height/2f - selectorRadius;

		buttonsX = (int) Math.max(width / 2f + 25, selectorX + selectorRadius * 2 + 10);

		int y = 120;
		addRenderableWidget(ConfigStyles.createWidget(buttonsX, y, 150, 20, chroma));
		y += 45;
		if (height > 300) {
			addRenderableWidget(ConfigStyles.createWidget(buttonsX, y, 150, 20, speed));
			y += 45;
		}
		addRenderableWidget(ConfigStyles.createWidget(buttonsX, y, 150, 20, alpha));
		y += 45;
		if (this.height - 250 > 0) {
			y -= 20;
			TextFieldWidget text = new TextFieldWidget(buttonsX, y, 150, 20, Component.empty());
			text.setChangedListener(s -> {
				try {
					option.set(Color.parse(s));
					option.getOriginal().setChroma(chroma.get());
					option.getOriginal().setChromaSpeed(speed.get());

					children().forEach(e -> {
						if (e instanceof Updatable) {
							((Updatable) e).update();
						}
					});
				} catch (Throwable ignored) {
				}
			});
			text.setText(option.get().toString().split(";")[0]);
			addRenderableWidget(text);
		}
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		NVGUtil.wrap(ctx -> {
			NVGHolder.setContext(ctx);
			super.render(graphics, mouseX, mouseY, delta);

			drawCenteredString(ctx, NVGHolder.getFont(), title.getString(), width / 2f, 20, Colors.text());

			if (paint == null || paint.address() == 0) {
				int image = DrawUtil.nvgCreateImage(ctx, ResourceLocation.fromNamespaceAndPath("axolotlclientconfig", "textures/gui/colorwheel.png"));
				paint = nvgImagePattern(ctx, selectorX, selectorY, selectorRadius * 2, selectorRadius * 2, 0, image, 1, NVGPaint.create());
			}

			nvgBeginPath(ctx);
			nvgRoundedRect(ctx, selectorX, selectorY, selectorRadius * 2, selectorRadius * 2, selectorRadius);
			nvgFillPaint(ctx, paint);
			nvgFill(ctx);

			nvgBeginPath(ctx);
			nvgRoundedRect(ctx, selectorX, selectorY, selectorRadius * 2, selectorRadius * 2, selectorRadius);
			nvgStrokeColor(ctx, Colors.BLACK.toNVG());
			nvgStrokeWidth(ctx, 1);
			nvgStroke(ctx);

			drawString(ctx, NVGHolder.getFont(), I18n.get("option.current"), buttonsX, 40, Colors.text());

			fillRoundedRect(ctx, buttonsX, 55, 150, 40, option.get(), 10);
			outlineRoundedRect(ctx, buttonsX, 55, 150, 40, Colors.BLACK, 10, 1);

			int y = 105;
			drawString(ctx, NVGHolder.getFont(), I18n.get("option.chroma"), buttonsX, y, Colors.text());
			y += 45;
			if (height > 300) {
				drawString(ctx, NVGHolder.getFont(), I18n.get("option.speed"), buttonsX, y, Colors.text());
				y += 45;
			}
			drawString(ctx, NVGHolder.getFont(), I18n.get("option.alpha"), buttonsX, y, Colors.text());
		});
	}

	@Override
	public void renderBackground(GuiGraphics graphics, int i, int j, float f) {
		super.renderBackground(graphics, i, j, f);
		graphics.fill(0, 0, 1, 1, 0); // Don't ask, it seems to work
		fillRoundedRect(NVGHolder.getContext(), 15, 15, width - 30, height - 30, Colors.background(), 12);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {

		if (button == 0) {
			double x = (selectorX + selectorRadius - mouseX);
			double y = (selectorY + selectorRadius - mouseY);
			if (Math.hypot(Math.abs(x), Math.abs(y)) <= selectorRadius) {

				final ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(4);
				pixelBuffer.order(ByteOrder.nativeOrder());

				RenderSystem.readPixels(toGlCoordsX(mouseX), toGlCoordsY(mouseY),
					1, 1, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixelBuffer);

				final int r = pixelBuffer.get(0) & 0xff;
				final int g = pixelBuffer.get(1) & 0xff;
				final int b = pixelBuffer.get(2) & 0xff;
				option.getOriginal().setRed(r);
				option.getOriginal().setGreen(g);
				option.getOriginal().setBlue(b);

				children().forEach(e -> {
					if (e instanceof Updatable) {
						((Updatable) e).update();
					}
				});
				children().forEach(e -> {
					if (e instanceof TextFieldWidget) {
						((TextFieldWidget) e).setText(option.get().toString().split(";")[0]);
					}
				});
				return true;
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	private int toGlCoordsX(double x) {
		Window window = minecraft.getWindow();
		return (int) (x * window.getGuiScale());
	}

	private int toGlCoordsY(double y) {
		Window window = minecraft.getWindow();
		double scale = window.getGuiScale();
		return Math.round((float) (minecraft.getMainRenderTarget().height - y * scale - scale));
	}

	@Override
	public void resize(Minecraft minecraftClient, int i, int j) {
		super.resize(minecraftClient, i, j);
		if (paint != null) {
			paint = null;
		}
	}
}
