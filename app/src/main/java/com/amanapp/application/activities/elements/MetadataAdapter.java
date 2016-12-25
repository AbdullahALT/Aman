package com.amanapp.application.activities.elements;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amanapp.R;
import com.amanapp.application.core.logics.FileSerialized;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Abdullah ALT on 8/12/2016.
 */
public class MetadataAdapter extends RecyclerView.Adapter<MetadataAdapter.MetadataViewHolder> {

    private static final String TAG = MetadataAdapter.class.getName();
    private final Picasso picasso;
    private final onClick clickHandler;
    private List<Metadata> items;

    public MetadataAdapter(Picasso picasso, onClick click) {
        this.picasso = picasso;
        this.clickHandler = click;
    }

    public void setItems(@NonNull List<Metadata> items) {
        this.items = Collections.unmodifiableList(items);
        notifyDataSetChanged();
    }

    @Override
    public MetadataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.files_item, parent, false);

        return new MetadataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MetadataViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return (items == null) ? 0 : items.size();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getName().toLowerCase().hashCode();
    }

    public interface onClick {
        void onFileClicked(FileMetadata file);
        void onFolderClicked(FolderMetadata folder);

        void onFolderOptionsClicked(FolderMetadata folder);
    }

    public class MetadataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView itemImage;
        private ImageView itemOptions;
        private TextView itemName;
        private Metadata item;

        public MetadataViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemOptions = (ImageView) itemView.findViewById(R.id.itemOptions);
            itemView.setOnClickListener(this);
            itemOptions.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.itemOptions) {
                clickHandler.onFolderOptionsClicked((FolderMetadata) item);
            } else {
                if (item instanceof FileMetadata) {
                    clickHandler.onFileClicked((FileMetadata) item);
                } else if (item instanceof FolderMetadata) {
                    clickHandler.onFolderClicked((FolderMetadata) item);
                }
            }
        }

        public void bind(final Metadata item) {
            this.item = item;
            this.itemName.setText(item.getName());

            if (item instanceof FileMetadata) {
                itemOptions.setVisibility(View.GONE);
                picasso.load((new FileSerialized((FileMetadata) item).getIcon()))
                            .noFade()
                            .into(itemImage);

            } else if (item instanceof FolderMetadata) {
                itemOptions.setVisibility(View.VISIBLE);
                Log.v(TAG, "item is folder(Name:" + item.getName() + ")");
                itemImage.setImageResource(R.drawable.ic_folder_aman_24dp);
            }
        }
    }
}
