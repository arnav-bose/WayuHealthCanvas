package com.example.arnav.wayuhealth.RecyclerViewImageUpload;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.arnav.wayuhealth.R;

import java.util.ArrayList;

/**
 * Created by Arnav on 08/06/2016.
 */
public class RecyclerViewImageUploadAdapter extends RecyclerView.Adapter<RecyclerViewImageUploadAdapter.RecyclerViewHolder> {

    private ArrayList<DataSetImageUpload> arrayListImageUpload = new ArrayList<>();
    private static MyClickListener myClickListener;

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageViewImageUpload;
        CardView cardViewImageUpload;

        public RecyclerViewHolder(View view){
            super(view);
            imageViewImageUpload = (ImageView)view.findViewById(R.id.imageViewUpload);
            cardViewImageUpload = (CardView)view.findViewById(R.id.cardViewImageUpload);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener){
        this.myClickListener = myClickListener;
    }

    public RecyclerViewImageUploadAdapter(ArrayList<DataSetImageUpload> dataSetImageUploads){
        this.arrayListImageUpload = dataSetImageUploads;
    }

    @Override
    public RecyclerViewImageUploadAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card_row_layout, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewImageUploadAdapter.RecyclerViewHolder holder, int position) {
        DataSetImageUpload dataSetImageUpload = arrayListImageUpload.get(position);
        holder.imageViewImageUpload.setImageBitmap(dataSetImageUpload.getImageViewNotes());
        holder.cardViewImageUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO: Make NotesDetails.java
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListImageUpload.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
