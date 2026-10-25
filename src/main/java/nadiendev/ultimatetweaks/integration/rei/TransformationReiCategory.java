package nadiendev.ultimatetweaks.integration.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import nadiendev.ultimatetweaks.transformation.view.TransformationKind;
import nadiendev.ultimatetweaks.transformation.view.TransformationView;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class TransformationReiCategory implements DisplayCategory<TransformationReiDisplay> {

    private static final int PADDING = 5;
    private static final int TEXT_COLOR = 0xFF808080;

    private final TransformationKind kind;

    public TransformationReiCategory(TransformationKind kind) {
        this.kind = kind;
    }

    public static CategoryIdentifier<TransformationReiDisplay> identifier(TransformationKind kind) {
        return CategoryIdentifier.of(kind.id());
    }

    @Override
    public CategoryIdentifier<? extends TransformationReiDisplay> getCategoryIdentifier() {
        return identifier(kind);
    }

    @Override
    public Component getTitle() {
        return kind.title();
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(kind.icon());
    }

    @Override
    public int getDisplayHeight() {
        return TransformationView.HEIGHT + PADDING * 2;
    }

    @Override
    public int getDisplayWidth(TransformationReiDisplay display) {
        return TransformationView.WIDTH + PADDING * 2;
    }

    @Override
    public List<Widget> setupDisplay(TransformationReiDisplay display, Rectangle bounds) {
        TransformationView view = display.view();
        int originX = bounds.x + PADDING;
        int originY = bounds.y + PADDING;

        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        int input = 0;
        for (var ingredient : display.getInputEntries()) {
            widgets.add(Widgets.createSlot(new Point(originX + TransformationView.slotX(TransformationView.INPUT_X, input),
                    originY + TransformationView.slotY(input))).entries(ingredient).markInput());
            input++;
        }

        widgets.add(Widgets.createArrow(new Point(originX + TransformationView.ARROW_X, originY + TransformationView.ARROW_Y)));

        int output = 0;
        for (var ingredient : display.getOutputEntries()) {
            widgets.add(Widgets.createSlot(new Point(originX + TransformationView.slotX(TransformationView.OUTPUT_X, output),
                    originY + TransformationView.slotY(output))).entries(ingredient).markOutput());
            output++;
        }

        int line = 0;
        for (Component note : view.notes()) {
            widgets.add(Widgets.createLabel(new Point(originX + TransformationView.INPUT_X,
                    originY + TransformationView.NOTES_Y + line * TransformationView.NOTE_HEIGHT), note)
                    .leftAligned().color(TEXT_COLOR).noShadow());
            line++;
        }
        return widgets;
    }
}
