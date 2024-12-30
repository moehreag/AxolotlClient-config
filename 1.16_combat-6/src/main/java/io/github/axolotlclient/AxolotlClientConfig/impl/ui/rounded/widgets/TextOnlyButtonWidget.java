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
