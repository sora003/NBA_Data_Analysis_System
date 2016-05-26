package com.sora.projectn.utils.Adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.sora.projectn.utils.BitmapHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by Sora on 2016-04-26.
 * 当图片数量过多或图片大小较大时，加载速度明显减慢
 * 优化SimpleAdapter的图片加载
 */
public class MySimpleAdapter extends SimpleAdapter {

    Context mContext;

    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
    }

    @Override
    public void setViewImage(ImageView v, int value) {
        v.setImageBitmap(BitmapHelper.loadBitMap(mContext, value));
    }
}
