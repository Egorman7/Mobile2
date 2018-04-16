package app.and.mobile2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import app.and.mobile2.R;
import app.and.mobile2.database.DBHelper;

public class ListAdapter extends CursorRecyclerViewAdapter<ListAdapter.ViewHolder> {
    public ListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image, check;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.list_item_image);
            check = itemView.findViewById(R.id.list_item_check);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        Picasso.get().load(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.LIST_URL))).into(viewHolder.image);
        viewHolder.check.setImageResource((cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.LIST_APPROVED))==1) ? R.drawable.icon_ok : R.drawable.icon_no);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }
}
