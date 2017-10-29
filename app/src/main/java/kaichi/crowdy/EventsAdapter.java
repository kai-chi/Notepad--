package kaichi.crowdy;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kaichi.crowdy.database.EventDatabaseContract.Event;

import static android.support.v7.widget.RecyclerView.ViewHolder;


public class EventsAdapter
        extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    //interface implemented by EventsFragment to respond
    //when the user touches an item in the RecycerView
    public interface EventClickListener {
        void onClick(Uri eventUri);
    }

    public class EventViewHolder extends ViewHolder {
        private final TextView title;
        private final TextView description;
        private long rowID;
        private final RelativeLayout relativeLayout;

        public EventViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.eventTitleTextView);
            description = itemView.findViewById(R.id.eventDescriptionTextView);
            relativeLayout = itemView.findViewById(R.id.cardRelativeLayout);
            relativeLayout.setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            clickListener.onClick(Event.buildEventUri(rowID));
                        }
                    }
            );
        }

        private void setRowID(long rowID) {
            this.rowID = rowID;
        }

        private void setColor(int color) {
            relativeLayout.setBackgroundColor(color);
        }
    }

    private final EventClickListener clickListener;
    private Cursor cursor = null;

    public EventsAdapter(EventClickListener eventClickListener) {
        this.clickListener = eventClickListener;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.event_card,
                                           parent,
                                           false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.setRowID(cursor.getLong(cursor.getColumnIndex(Event._ID)));
        holder.title.setText(cursor.getString(cursor.getColumnIndex(Event.COLUMN_TITLE)));
        holder.description.setText(cursor.getString(cursor.getColumnIndex(Event.COLUMN_DESCRIPTION)));
        holder.setColor(cursor.getInt(cursor.getColumnIndex(Event.COLUMN_COLOR)));
    }


    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}
