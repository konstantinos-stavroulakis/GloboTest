package gr.apphub.globotest;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by Konstantinos on 07/10/15.
 */
public class GridAdapter extends CursorAdapter {

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private DisplayImageOptions options;

    public GridAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View convertView = inflater.inflate(R.layout.grid_item, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.cardId = (TextView) convertView.findViewById(R.id.listItemCardId);
//        holder.cardName = (TextView) convertView.findViewById(R.id.listItemCardName);
        holder.image = (ImageView) convertView.findViewById(R.id.listItemCardImage);
        convertView.setTag(holder);

        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.cardId.setText(cursor.getString(cursor.getColumnIndex(DatabaseActivity.DBHelper.CARDID)));
//        holder.cardName.setText(cursor.getString(cursor.getColumnIndex(DatabaseActivity.DBHelper.NAME)));

        String imageurl = cursor.getString(cursor.getColumnIndex(DatabaseActivity.DBHelper.IMG));
        ImageLoader.getInstance().displayImage(imageurl, holder.image, options, animateFirstListener);

    }


    class ViewHolder {
        TextView cardId;
//              TextView cardName;

        ImageView image;

    }
}