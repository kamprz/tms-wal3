package wat.semestr7.bachelor.mvc.view.profitable;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;


public class CurrencyIndicator extends HBox {
    private Label currencySymbol = new Label();
    private Circle circleIndicator = new Circle();

    public CurrencyIndicator(String currencySymbol)
    {
        this.currencySymbol.setText(currencySymbol.substring(0,3) + " / " + currencySymbol.substring(3,6));
        init();
    }

    public void turnOff()
    {
        circleIndicator.setFill(Paint.valueOf("#ff2121"));
    }

    public void turnOn()
    {
        circleIndicator.setFill(Paint.valueOf("#1fff3f"));
    }

    private void init()
    {
        currencySymbol.setFont(new Font("Arial",17));
        circleIndicator.setFill(Paint.valueOf("#1fff3f"));
        circleIndicator.setRadius(20.0);
        circleIndicator.setStroke(Paint.valueOf("#000000"));
        circleIndicator.setStrokeType(StrokeType.INSIDE);
        circleIndicator.setEffect(new DropShadow());
        setAlignment(Pos.CENTER);
        setPadding(new Insets(30));
        setSpacing(30);

        getChildren().addAll(currencySymbol,circleIndicator);
    }
}
