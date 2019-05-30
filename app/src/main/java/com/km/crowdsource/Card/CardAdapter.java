package com.km.crowdsource.Card;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.km.crowdsource.R;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

    private Context context;
    CustomCardVeiwItemClickListener clickListener;
    private List<Card> cardList;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public ImageView cardImage;
        public ConstraintLayout constraintLayout;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_card_name);
            cardImage = (ImageView) view.findViewById(R.id.iv_card_image);
            constraintLayout = (ConstraintLayout) view.findViewById(R.id.card_layout);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Position:" + Integer.toString(getPosition()), Toast.LENGTH_SHORT).show();
                    if (clickListener != null) {
                        clickListener.onItemClick(v, getAdapterPosition());

                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
        }
    }


    public void setCustomCardViewClickclickListener(CustomCardVeiwItemClickListener customCardViewClickclickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_card_view, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Card card = cardList.get(position);
        holder.title.setText(card.getTitle());
        Glide.with(context).load(card.getCardImage()).into(holder.cardImage);
    }


    @Override
    public int getItemCount() {
        return cardList.size();
    }


    public CardAdapter(Context context, List<Card> cardList, CustomCardVeiwItemClickListener clickListener) {
        this.context = context;
        this.cardList = cardList;
        this.clickListener = clickListener;
    }
}
