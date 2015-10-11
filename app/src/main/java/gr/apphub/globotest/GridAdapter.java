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

         //DisplayImageOptions are local for every display task (ImageLoader.displayImage(...))
        //https://github.com/nostra13/Android-Universal-Image-Loader/wiki/Display-Options
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    //view is created in newView() (simply inflate the view your custom xml and return it)
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View convertView = inflater.inflate(R.layout.grid_item, parent, false);
        // populate the ViewHolder and store it inside the layout
        ViewHolder holder = new ViewHolder();
        holder.cardId = (TextView) convertView.findViewById(R.id.listItemCardId);
        holder.image = (ImageView) convertView.findViewById(R.id.listItemCardImage);
        convertView.setTag(holder);

        return convertView;
    }

    //elements are populated in bindView() (set the elements of your view)
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.cardId.setText(cursor.getString(cursor.getColumnIndex(DatabaseActivity.DBHelper.CARDID)));

        String imageurl = cursor.getString(cursor.getColumnIndex(DatabaseActivity.DBHelper.IMG));
        ImageLoader.getInstance().displayImage(imageurl, holder.image, options, animateFirstListener);

    }


    //A ViewHolder object stores each of the component views inside the tag field of the Layout, so you can immediately access them without the need to look them up repeatedly.
    //so you can easily access each view without the need for the look-up, saving valuable processor cycles
    class ViewHolder {
        TextView cardId;
        ImageView image;

    }
}