package com.amanapp.ui.elements;

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
import com.amanapp.logics.FileSerialized;
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
    private final onMetaDataClick clickHandler;
    private List<Metadata> items;

    public MetadataAdapter(Picasso picasso, onMetaDataClick click) {
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

    public interface onMetaDataClick {
        void onFileClicked(FileMetadata file);

        void onFolderClicked(FolderMetadata folder);
    }

    public class MetadataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView itemImage;
        private TextView itemName;
        private Metadata item;

        public MetadataViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (item instanceof FileMetadata) {
                clickHandler.onFileClicked((FileMetadata) item);
            } else if (item instanceof FolderMetadata) {
                clickHandler.onFolderClicked((FolderMetadata) item);
            }
        }

        public void bind(Metadata item) {
            this.item = item;
            this.itemName.setText(item.getName());

            /*
            Comment from dropbox:
            Load based on file path prepending a magic scheme to get it to
            be picked up by DropboxPicassoRequestHandler
             */
            if (item instanceof FileMetadata) {
//                Log.v(TAG, "item is file(Name:" + item.getName() + ")");
//                MimeTypeMap mime = MimeTypeMap.getSingleton();
//                String extension = item.getName().toLowerCase().substring(item.getName().indexOf(".") + 1);
//                String type = mime.getMimeTypeFromExtension(extension);
//                if (type != null && type.startsWith("image/")) {
//                    Log.v(TAG, "item file is an image (Name:" + item.getName() + ")");
//                    picasso.load(FileThumbnailRequestHandler.buildPicassoUri((FileMetadata) item))
//                            .placeholder(R.drawable.ic_photo_grey_600_36dp)
//                            .error(R.drawable.ic_photo_grey_600_36dp)
//                            .into(itemImage);
//                } else {
//                    Log.v(TAG, "item file is not an image (Name:" + item.getName() + ")");
//                    picasso.load(R.drawable.ic_insert_drive_file_blue_36dp)
//                            .noFade()
//                            .into(itemImage);
//                }


//                String extension = item.getName().toLowerCase().substring(item.getName().indexOf(".") + 1);
//                IconFactory factory = new IconFactory(extension);
//                picasso.load(factory.getIcon(null))
//                            .noFade()
//                            .into(itemImage);
                picasso.load((new FileSerialized((FileMetadata) item).getIcon()))
                            .noFade()
                            .into(itemImage);

            } else if (item instanceof FolderMetadata) {
                Log.v(TAG, "item is folder(Name:" + item.getName() + ")");
                picasso.load(R.drawable.ic_folder_blue_36dp)
                        .noFade()
                        .into(itemImage);
            }
        }
    }
}
