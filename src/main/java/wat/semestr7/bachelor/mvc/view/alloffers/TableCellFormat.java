package wat.semestr7.bachelor.mvc.view.alloffers;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

class TableCellFormat extends TableCell<OfferViewRow, String>
{
    @Override
    protected void updateItem(String item, boolean empty)
    {
        super.updateItem(item,empty);
        this.setText(item);
        this.setAlignment(Pos.CENTER);
    }
}