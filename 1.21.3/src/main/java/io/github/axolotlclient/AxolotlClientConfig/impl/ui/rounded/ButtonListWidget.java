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

package io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded;

import io.github.axolotlclient.AxolotlClientConfig.api.manager.ConfigManager;
import io.github.axolotlclient.AxolotlClientConfig.api.options.Option;
import io.github.axolotlclient.AxolotlClientConfig.api.options.OptionCategory;
import io.github.axolotlclient.AxolotlClientConfig.api.options.WidgetIdentifieable;
import io.github.axolotlclient.AxolotlClientConfig.api.util.AlphabeticalComparator;
import io.github.axolotlclient.AxolotlClientConfig.api.util.Colors;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.DrawingUtil;
import io.github.axolotlclient.AxolotlClientConfig.impl.util.ConfigStyles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class ButtonListWidget extends ContainerObjectSelectionList<ButtonListWidget.Entry> implements DrawingUtil {

	protected static int WIDGET_WIDTH = 150;
	protected static int WIDGET_ROW_LEFT = -155;
	protected static int WIDGET_ROW_RIGHT = WIDGET_ROW_LEFT + WIDGET_WIDTH + 10;
	protected String searchFilter = null;
	@Nullable
	private final ConfigManager manager;
	private final OptionCategory category;

	public ButtonListWidget(ConfigManager manager, OptionCategory category, int screenWidth, int screenHeight, int top, int bottom, int entryHeight) {
		super(Minecraft.getInstance(), screenWidth, bottom-top, top, entryHeight);
		centerListVertically = false;
		this.manager = manager;
		this.category = category;

		addEntries(manager, category);
	}

	public void addEntry(OptionCategory first, @Nullable OptionCategory second) {
		addEntry(createCategoryEntry(createWidget(width / 2 + WIDGET_ROW_LEFT, first), first,
			second == null ? null : createWidget(width / 2 + WIDGET_ROW_RIGHT, second), second));
	}

	public void addEntry(Option<?> first, @Nullable Option<?> second) {
		addEntry(createOptionEntry(createWidget(width / 2 + WIDGET_ROW_LEFT, first), first,
			second == null ? null : createWidget(width / 2 + WIDGET_ROW_RIGHT, second), second));
	}

	protected void addCategories(ConfigManager manager, Collection<OptionCategory> categories) {
		List<OptionCategory> list = new ArrayList<>(categories);
		if (manager != null) {
			list.removeIf(c -> manager.getSuppressedNames().contains(c.getName()));
		}
		for (int i = 0; i < list.size(); i += 2) {
			addEntry(list.get(i), i < list.size() - 1 ? list.get(i + 1) : null);
		}
	}

	protected void addOptions(ConfigManager manager, Collection<Option<?>> options) {
		List<Option<?>> list = new ArrayList<>(options);
		if (manager != null) {
			list.removeIf(o -> manager.getSuppressedNames().contains(o.getName()));
		}
		for (int i = 0; i < list.size(); i += 2) {
			addEntry(list.get(i), i < list.size() - 1 ? list.get(i + 1) : null);
		}
	}

	public void addEntries(ConfigManager manager, OptionCategory category) {
		addCategories(manager, category.getSubCategories());
		if (!children().isEmpty() && !category.getOptions().isEmpty()) {
			addEntry(new Entry(Collections.emptyList()));
		}
		addOptions(manager, category.getOptions());
	}

	public void setSearchFilter(String filter) {
		this.searchFilter = filter;
		clearEntries();
		if (searchFilter == null) {
			addEntries(manager, category);
			return;
		}
		List<OptionCategory> flattenedCategories = new ArrayList<>();
		List<Option<?>> flattenedOptions = new ArrayList<>();
		collectEntries(category, flattenedCategories, flattenedOptions);
		flattenedCategories.removeIf(c -> !I18n.get(c.getName()).toLowerCase(Locale.ROOT).contains(searchFilter.toLowerCase(Locale.ROOT)));
		flattenedOptions.removeIf(c -> !I18n.get(c.getName()).toLowerCase(Locale.ROOT).contains(searchFilter.toLowerCase(Locale.ROOT)));
		flattenedCategories.sort((o1, o2) -> AlphabeticalComparator.cmp(I18n.get(o1.getName()), I18n.get(o2.getName())));
		flattenedOptions.sort((o1, o2) -> AlphabeticalComparator.cmp(I18n.get(o1.getName()), I18n.get(o2.getName())));
		addCategories(manager, flattenedCategories);
		addOptions(manager, flattenedOptions);
	}

	protected void collectEntries(OptionCategory current, List<OptionCategory> categoryCollector, List<Option<?>> optionCollector) {
		optionCollector.addAll(current.getOptions());
		current.getSubCategories().forEach(c -> {
			categoryCollector.add(c);
			collectEntries(c, categoryCollector, optionCollector);
		});
	}

	protected AbstractWidget createWidget(int x, WidgetIdentifieable id) {
		return ConfigStyles.createWidget(x, 0, WIDGET_WIDTH, itemHeight - 5, id);
	}

	protected Entry createOptionEntry(AbstractWidget widget, Option<?> option, @Nullable AbstractWidget other, @Nullable Option<?> otherOption) {
		return Entry.create(widget, other);
	}

	protected Entry createCategoryEntry(AbstractWidget widget, OptionCategory optionCategory, @Nullable AbstractWidget other, @Nullable OptionCategory otherOptionCategory) {
		return Entry.create(widget, other);
	}

	@Override
	public int getRowWidth() {
		return 308;
	}

	@Override
	protected boolean scrollbarVisible() {
		return false;
	}
	protected void renderListBackground(GuiGraphics guiGraphics) {
	}

	protected void renderListSeparators(GuiGraphics guiGraphics) {
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		return getHovered() != null && getHovered().mouseScrolled(mouseX, mouseY, scrollX, scrollY) || super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}

	@Override
	protected void renderDecorations(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		int i = this.getMaxScroll();
		if (i > 0) {
			int j = this.getScrollbarPosition();
			int k = (int) ((float) ((this.height) * (this.height)) / (float) this.getMaxPosition());
			k = Mth.clamp(k, 32, this.height - 8);
			int l = (int) this.getScrollAmount() * (this.height - k) / i + this.getY();
			if (l < this.getY()) {
				l = this.getY();
			}

			fillRoundedRect(NVGHolder.getContext(), j, getY(), 6, getHeight(), Colors.foreground(), 6 / 2);
			fillRoundedRect(NVGHolder.getContext(), j, l, 6, k, Colors.accent(), 6 / 2);
		}
	}

	@Override
	protected void renderListItems(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		pushScissor(NVGHolder.getContext(), getX(), getY(), getWidth(), getHeight());
		super.renderListItems(guiGraphics, mouseX, mouseY, partialTick);
		popScissor(NVGHolder.getContext());
	}

	protected static class Entry extends ContainerObjectSelectionList.Entry<Entry> {


		protected final List<AbstractWidget> children = new ArrayList<>();

		public Entry(Collection<AbstractWidget> widgets) {
			children.addAll(widgets);
		}

		public static Entry create(AbstractWidget first, AbstractWidget other) {
			return new Entry(Stream.of(first, other).filter(Objects::nonNull).toList());
		}

		@Override
		public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
			children.forEach(c -> {
				c.setY(y);
				c.render(graphics, mouseX, mouseY, tickDelta);
			});
		}

		@Override
		public List<? extends NarratableEntry> narratables() {
			return children;
		}

		@Override
		public List<? extends GuiEventListener> children() {
			return children;
		}
	}
}
