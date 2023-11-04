package io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.widgets;

import io.github.axolotlclient.AxolotlClientConfig.api.util.Colors;
import io.github.axolotlclient.AxolotlClientConfig.impl.options.BooleanOption;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded.NVGHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.text.CommonTexts;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public class PillBooleanWidget extends RoundedButtonWidget {

	protected static final int HANDLE_MARGIN = 3;
	protected static final int OFF_POSITION = HANDLE_MARGIN;
	protected int handleWidth;
	protected int onPosition;

	private boolean state;
	private boolean targetState;
	private double progress;

	private long progressStartTime = Util.getEpochTimeMs();
	private final BooleanOption option;
	private int notWidth;

	public PillBooleanWidget(int x, int y, int width, int height, BooleanOption option) {
		super(x, y, 40, height, option.get() ? CommonTexts.ON : CommonTexts.OFF, widget -> {
			option.set(!option.get());
			widget.setMessage(option.get() ? CommonTexts.ON : CommonTexts.OFF);
		});

		this.option = option;

		state = targetState = option.get();

		if (state) {
			progress = 1f;
		}

		handleWidth = height - HANDLE_MARGIN * 2;
		onPosition = super.getWidth() - handleWidth - HANDLE_MARGIN;

		//setWidth(WIDGET_WIDTH);
		this.notWidth = width;
	}

	@Override
	public void setWidth(int value) {
		setX(getX() + value - 40);
		this.notWidth = value;
	}

	@Override
	public int getWidth() {
		return notWidth;
	}

	@Override
	protected void drawWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		fillRoundedRect(NVGHolder.getContext(), getX(), getY(), super.getWidth(), getHeight(), Colors.GRAY, Math.min(getHeight(), super.getWidth()) / 2f);

		//if (targetState != state) {

			if (((Util.getEpochTimeMs() - progressStartTime) / 300L) % 2L == 0L) {
				if (state != targetState) {
					if (targetState) {
						//progress = Math.min(1, progress + 0.04f);
						progress += 0.04;
					} else {
						//progress = Math.max(0, progress - 0.04f);
						progress -= 0.04;
					}
					if (progress >= 1 || progress <= 0) {
						if (progress > 1){
							progress = 1;
						} else if (progress < 0) {
							progress = 0;
						}
						state = targetState;
					}
				}
			}

			double x = getX() + MathHelper.lerp(progress, OFF_POSITION, onPosition); //getX() + OFF_POSITION + (onPosition - OFF_POSITION) * progress;
			if (x != (int)x){
				System.out.println("Progress: "+progress+" X: "+x);
			}
			//System.out.println("Progress: "+progress+" X: "+x);
			double widthProgress = progress > 0.5f ? 1 - progress : progress;
			drawHandle(NVGHolder.getContext(), (float) x, getY(), (float) (handleWidth + (handleWidth * widthProgress)));


		/*} else {
			if (state) {
				drawHandle(NVGHolder.getContext(), getX() + OFF_POSITION + onOffset, getY(), handleWidth);
			} else {
				drawHandle(NVGHolder.getContext(), getX() + OFF_POSITION, getY(), handleWidth);
			}
		}*/
	}

	protected void drawHandle(long ctx, float x, float y, float width) {
		fillRoundedRect(ctx, x, y + HANDLE_MARGIN, width, getHeight() - HANDLE_MARGIN * 2,
			getWidgetColor(), Math.min(width, getHeight()) / 2f + HANDLE_MARGIN);

		if (isFocused()) {
			outlineRoundedRect(ctx, x, y + HANDLE_MARGIN, width, getHeight() - HANDLE_MARGIN * 2,
				Colors.WHITE, Math.min(width, getHeight()) / 2f + HANDLE_MARGIN, 1);
		}
	}

	@Override
	public void onPress() {
		super.onPress();
		state = targetState;
		targetState = !targetState;
		progressStartTime = Util.getEpochTimeMs();
	}

	public void update(){
		targetState = option.get();
		setMessage(option.get() ? CommonTexts.ON : CommonTexts.OFF);
		progressStartTime = Util.getEpochTimeMs();
	}
}
