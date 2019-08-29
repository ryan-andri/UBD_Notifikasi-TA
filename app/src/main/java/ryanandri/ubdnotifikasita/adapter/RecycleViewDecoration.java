package ryanandri.ubdnotifikasita.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Ryan Andri on 8/29/2019.
 *
 * Adds 8dp padding to the top of the first and the bottom of the last item in the list,
 * as specified in https://www.google.com/design/spec/components/lists.html#lists-specs
 */
public class RecycleViewDecoration extends RecyclerView.ItemDecoration {
    private final static int PADDING_IN_DIPS = 5;
    private final int mPadding;

    public RecycleViewDecoration(@NonNull Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PADDING_IN_DIPS, metrics);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {

        final int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition == RecyclerView.NO_POSITION) {
            return;
        }

        if (itemPosition == 0) {
            outRect.top = mPadding;
        }

        RecyclerView.Adapter adapter = parent.getAdapter();
        if ((adapter != null) && (itemPosition == adapter.getItemCount() - 1)) {
            outRect.bottom = mPadding;
        }
    }
}
