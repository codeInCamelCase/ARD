package in.ac.bits_pilani.goa.ard.Adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.ac.bits_pilani.goa.ard.Models.CardType;
import in.ac.bits_pilani.goa.ard.R;
import in.ac.bits_pilani.goa.ard.utils.AHC;

/**
 * @author Nirvan Anjirbag
 *     Adapter for home fragment recyclerView.
 */
public class HomeRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * list of cards to be shown in recyclerView.
     */
    private List<CardType> cardList;

    public HomeRvAdapter() {
        super();
    }

    public void setCardList(final List<CardType> cardList) {
        this.cardList = cardList;
    }

    @Override
    public int getItemViewType(final int position) {
        return cardList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        //just a dummy value
        RecyclerView.ViewHolder viewHolder = new ViewHolderImage(parent);
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == AHC.IMAGE_CARD_TYPE) {
            final View v1 = inflater.inflate(R.layout.home_card_image, parent, false);
            viewHolder = new ViewHolderImage(v1);
        } /*else {
            final View v2 = inflater.inflate(R.layout.home_card_default, parent, false);
            viewHolder = new ViewHolderDefault(v2);
        }*/

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final CardType card = cardList.get(position);
        //do the binding

        if (getItemViewType(position) == AHC.IMAGE_CARD_TYPE) {
            final ViewHolderImage v1 = (ViewHolderImage) holder;
            v1.viewHolderImageImage.setImageBitmap((Bitmap) card.getValue().get(0));
            v1.viewHolderImageTitle.setText(card.getValue().get(1).toString());
            v1.viewHolderImageDate.setText(card.getValue().get(2).toString());
            v1.viewHolderImageCommentCount.setText(card.getValue().get(3).toString());
        } /*else {
            final ViewHolderDefault v2 = (ViewHolderDefault) holder;
            v2.viewHolderDefaultTitle.setText( card.getValue().get(0).toString() );
        }*/

    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    /**
     * imageCard viewholder.
     */
    public static class ViewHolderImage extends RecyclerView.ViewHolder {
        /**
         * image in imageCard.
         */
        public ImageView viewHolderImageImage;

        /**
         * title in image card.
         */
        public TextView viewHolderImageTitle;

        /**
         * date in imagecard.
         */
        public TextView viewHolderImageDate;

        /**
         * commentCount in imageCard.
         */
        public TextView viewHolderImageCommentCount;

        /**
         * create the viewholder for imagecard.
         * @param view voew to attach
         */
        public ViewHolderImage(final View view) {
            super(view);
            this.viewHolderImageImage = (ImageView) view.findViewById( R.id.home_imagecard_imageView );
            this.viewHolderImageTitle = (TextView) view.findViewById( R.id.home_imagecard_title );
            this.viewHolderImageDate = (TextView) view.findViewById( R.id.home_imagecard_date );
            this.viewHolderImageCommentCount = (TextView) view.findViewById( R.id.home_imagecard_commentcount );
        }
    }

}
