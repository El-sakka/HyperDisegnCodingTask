package www.sakkawy.com.hyperdesigncodingtask.Adpater;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener{
    StaggeredGridLayoutManager layoutManager;
    private static final String TAG = "PaginationScrollListene";

    public PaginationScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int []firstVisibleItemPosition = null;
        firstVisibleItemPosition = layoutManager.findFirstVisibleItemPositions(firstVisibleItemPosition);

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition[0]) >= totalItemCount
                    && firstVisibleItemPosition.length >= 0
                    && totalItemCount >= getTotalPageCount()) {
                loadMoreItems();
            }
        }
    }

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();
}
