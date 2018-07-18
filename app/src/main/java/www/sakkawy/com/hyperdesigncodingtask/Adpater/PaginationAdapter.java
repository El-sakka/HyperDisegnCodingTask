package www.sakkawy.com.hyperdesigncodingtask.Adpater;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import www.sakkawy.com.hyperdesigncodingtask.Model.Product;
import www.sakkawy.com.hyperdesigncodingtask.R;
import www.sakkawy.com.hyperdesigncodingtask.View.ProductDetails;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "PaginationAdapter";
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private List<Product> productList;
    private Context context;
    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        productList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View view = inflater.inflate(R.layout.product_progress_bar, parent, false);
                viewHolder = new LoadingViewHolder(view);
                break;
        }
        return viewHolder;
    }

    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View view = inflater.inflate(R.layout.product_item, parent, false);
        viewHolder = new ProductViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                ProductViewHolder productViewHolder = (ProductViewHolder) holder;
                productViewHolder.bind(position);
                break;
            case LOADING:
                // Do nothing
                break;
        }
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == productList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    protected class ProductViewHolder extends RecyclerView.ViewHolder {

        View view;
        CardView cardView;
        TextView mProductPrice;
        TextView mProductDescrition;
        ImageView mProductImage;

        String description= null;
        String price  = null;
        String imageUrl = null;

        public ProductViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            mProductPrice = (TextView) itemView.findViewById(R.id.product_price_text_view);
            mProductDescrition = (TextView) itemView.findViewById(R.id.product_description_text_view);
            mProductImage = (ImageView) itemView.findViewById(R.id.product_image_view);
            cardView = (CardView)itemView.findViewById(R.id.card_view);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: cardView ");
                    Intent intent = new Intent(context, ProductDetails.class);
                    intent.putExtra(ProductDetails.PRICE_KEY,price);
                    intent.putExtra(ProductDetails.DES_KEY,description);
                    intent.putExtra(ProductDetails.IMAGE_KEY,imageUrl);

                    context.startActivity(intent);
                }
            });

        }


        public void bind(int position){

            Product product = productList.get(position); // product

            int height = product.getImage().getHeight();
            int width = product.getImage().getWidth();

            description =  product.getProductDescription();
            price = product.getPrice().toString();
            imageUrl = product.getImage().getUrl();

            mProductDescrition.setWidth(width);
            mProductDescrition.setText(
                    description
            );

            mProductImage.getLayoutParams().height = height;
            Picasso.with(context).load(imageUrl)
                    .into(mProductImage);
            mProductPrice.setText("$" + price);

        }
    }


    protected class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }


    // helper methods
    public void add(Product r) {
        productList.add(r);
        notifyItemInserted(productList.size() - 1);
    }

    public void addAll(List<Product> productList) {
        for (Product result : productList) {
            add(result);
        }
    }

    public void remove(Product r) {
        int position = productList.indexOf(r);
        if (position > -1) {
            productList.remove(position);
            notifyItemRemoved(position);
        }
    }


    public Product getItem(int position) {
        return productList.get(position);
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Product());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = productList.size() - 1;
        Product result = getItem(position);

        if (result != null) {
            productList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
