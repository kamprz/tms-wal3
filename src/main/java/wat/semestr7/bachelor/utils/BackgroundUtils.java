package wat.semestr7.bachelor.utils;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class BackgroundUtils {
    public static Background getMainBackground()
    {
        return new Background(
                        new BackgroundFill(Color.valueOf("#fdfff3"),
                        new CornerRadii(2),
                        new Insets(2)));
    }
    public static Background getUpperPaneBackground()
    {
        return new Background(
                new BackgroundFill(Color.valueOf("#e1f2ff"),
                        new CornerRadii(2),
                        new Insets(2)));
    }
}
