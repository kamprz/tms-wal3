package wat.semestr7.bachelor.mvc.view.allOffers;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

class TableCellFormat extends TableCell<OfferView, String>
{
    @Override
    protected void updateItem(String item, boolean empty)
    {
        super.updateItem(item,empty);
        this.setText(item);
        this.setAlignment(Pos.CENTER);
    }
}