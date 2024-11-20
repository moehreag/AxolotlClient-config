package io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets;


import net.minecraft.client.render.TextRenderer;
import net.minecraft.text.Formatting;

public class PlainTextButtonWidget extends VanillaButtonWidget {
	private final TextRenderer font;
	private final String content;
	private final String underlinedContent;

	public PlainTextButtonWidget(int x, int y, int width, int height, String content, PressAction empty, TextRenderer font) {
		super(x, y, width, height, content, empty);
		this.font = font;
		this.content = content;
		this.underlinedContent = Formatting.UNDERLINE + content;
	}

	@Override
	public void drawWidget(int mouseX, int mouseY, float delta) {
		String text = this.isHovered() ? this.underlinedContent : this.content;
		font.drawWithShadow(text, getX(), getY(), 16777215 | 255 << 24);
	}
}
