package io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.math.MathHelper;

public class PlainTextButtonWidget extends VanillaButtonWidget {
	private final TextRenderer font;
	private final Text content;
	private final Text underlinedContent;

	public PlainTextButtonWidget(int x, int y, int width, int height, Text content, ButtonWidget.PressAction empty, TextRenderer font) {
		super(x, y, width, height, content, empty);
		this.font = font;
		this.content = content;
		this.underlinedContent = Texts.setStyleIfAbsent(content.copy(), Style.EMPTY.withUnderline(true));
	}

	@Override
	public void renderButton(MatrixStack graphics, int mouseX, int mouseY, float delta) {
		Text text = this.isHovered() ? this.underlinedContent : this.content;
		drawTextWithShadow(graphics, font, text, x, y, 16777215 | MathHelper.ceil(this.alpha * 255.0F) << 24);
	}
}
