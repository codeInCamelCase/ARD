package in.ac.bits_pilani.goa.ard.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nirvan Anjirbag
 *     Used to save data of all the cards displayed in the home fragment recyclerView.
 *     Convention:
 *     imageCard:
 *     value.get(0) -> Image
 *     value.get(1) -> Title
 *     value.get(2) -> Date
 *     value.get(3) -> no. of comments
 *     type = 0  (refer to AHC.java)
 *     -------------
 *     default
 *     value.get(0) -> title
 */
public class CardType {

    /**
     * type of card.
     */
    private int type;

    /**
     * Content of card.
     */
    private List<Object> value;

    /**
     * initialize List<> of cards.
     */
    public CardType() {
        super();
        value = new ArrayList<Object>();
    }

    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }

    public List<Object> getValue() {
        return value;
    }

    public void setValue(final List<Object> value) {
        this.value = value;
    }

}
