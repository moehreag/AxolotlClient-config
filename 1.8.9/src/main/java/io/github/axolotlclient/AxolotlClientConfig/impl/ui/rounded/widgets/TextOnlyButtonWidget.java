package io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets;

import io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.NVGHolder;
import net.minecraft.text.Formatting;

public class TextOnlyButtonWidget extends RoundedButtonWidget {
	private final String content, highlightedContent;

	public TextOnlyButtonWidget(int x, int y, String message, PressAction action) {
		this(x, y, (int) NVGHolder.getFont().getWidth(message), (int) NVGHolder.getFont().getLineHeight(), message, action);
	}

	private TextOnlyButtonWidget(int x, int y, int width, int height, String message, PressAction action) {
		super(x, y, width, height, message, action);
		this.content = message;
		this.highlightedContent = Formatting.UNDERLINE + content;
	}

	public static TextOnlyButtonWidget centeredWidget(int centerX, int y, String message, PressAction action) {
		float textWidth = NVGHolder.getFont().getWidth(message);
		return new TextOnlyButtonWidget((int) (centerX - textWidth / 2), y, (int) textWidth, (int) NVGHolder.getFont().getLineHeight(), message, action);
	}

	@Override
	public void drawWidget(int mouseX, int mouseY, float delta) {
		String message = isHovered() ? highlightedContent : content;
		drawString(NVGHolder.getContext(), NVGHolder.getFont(), message, getX(), this.getY(),
			(this.active ? activeColor : inactiveColor).withAlpha(255));
	}
}
