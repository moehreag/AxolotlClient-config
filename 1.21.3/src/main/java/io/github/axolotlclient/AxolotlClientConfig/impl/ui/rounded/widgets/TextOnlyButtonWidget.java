package io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets;

import io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.NVGHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;

public class TextOnlyButtonWidget extends RoundedButtonWidget {
	private final Component content, highlightedContent;

	public TextOnlyButtonWidget(int x, int y, Component message, OnPress action) {
		this(x, y, (int) NVGHolder.getFont().getWidth(message.getString()), (int) NVGHolder.getFont().getLineHeight(), message, action);
	}

	private TextOnlyButtonWidget(int x, int y, int width, int height, Component message, OnPress action) {
		super(x, y, width, height, message, action);
		this.content = message;
		this.highlightedContent = ComponentUtils.mergeStyles(content.copy(), Style.EMPTY.withUnderlined(true));
	}

	public static TextOnlyButtonWidget centeredWidget(int centerX, int y, Component message, OnPress action) {
		float textWidth = NVGHolder.getFont().getWidth(message.getString());
		return new TextOnlyButtonWidget((int) (centerX - textWidth / 2), y, (int) textWidth, (int) NVGHolder.getFont().getLineHeight(), message, action);
	}

	@Override
	protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		Component message = isHoveredOrFocused() ? highlightedContent : content;
		drawString(NVGHolder.getContext(), NVGHolder.getFont(), message.getString(), getX(), this.getY(),
			(this.active ? activeColor : inactiveColor).withAlpha((int) (alpha * 255)));
	}
}
