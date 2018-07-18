package www.sakkawy.com.hyperdesigncodingtask.View;

import android.graphics.Movie;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sakkawy.com.hyperdesigncodingtask.Adpater.PaginationAdapter;
import www.sakkawy.com.hyperdesigncodingtask.Adpater.PaginationScrollListener;
import www.sakkawy.com.hyperdesigncodingtask.ApiClient.IClient;
import www.sakkawy.com.hyperdesigncodingtask.ApiClient.ProductClient;
import www.sakkawy.com.hyperdesigncodingtask.Model.Product;
import www.sakkawy.com.hyperdesigncodingtask.R;

public class ProductActivity extends AppCompatActivity {
    private static final String TAG = "ProductActivity";
    private static final int PAGE_START = 10;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private Toolbar mToolbar;
    private PaginationAdapter adapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private RecyclerView rv;
    private ProgressBar progressBar;


    private int TOTAL_PAGES = 10;
    private int currentCount= PAGE_START;
    private int currentFrom = 1;
    private IClient productClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        final View toolbarContainerView = findViewById(R.id.toolbar_container);


        rv = (RecyclerView) findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        adapter = new PaginationAdapter(ProductActivity.this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(staggeredGridLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new PaginationScrollListener(staggeredGridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentCount += 10;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        //init service and load data
        productClient = ProductClient.getClient().create(IClient.class);
        loadFirstPage();
    }


    private void loadFirstPage() {

        callProductApi().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> results = response.body();
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                if(currentCount <= TOTAL_PAGES) adapter.addLoadingFooter();
                else
                    isLastPage = true;
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void loadNextPage() {

        callProductApi().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<Product> results = response.body();
                adapter.addAll(results);

                if(currentCount != TOTAL_PAGES) adapter.addLoadingFooter();
                else
                    isLastPage = true;
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private Call<List<Product>> callProductApi()
    {
        return productClient.fetchDataProduct(currentCount,currentFrom);
    }

}
