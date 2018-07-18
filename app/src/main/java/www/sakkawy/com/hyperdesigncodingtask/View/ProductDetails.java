package www.sakkawy.com.hyperdesigncodingtask.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import www.sakkawy.com.hyperdesigncodingtask.R;

public class ProductDetails extends AppCompatActivity {


    public static final String DES_KEY="description";
    public static final String PRICE_KEY="price";
    public static final String IMAGE_KEY="image";


    private ImageView mImageView;
    private TextView mPriceTextView;
    private TextView mDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent intent = getIntent();

        String description = intent.getStringExtra(DES_KEY);
        String price = intent.getStringExtra(PRICE_KEY);
        String imageUrl = intent.getStringExtra(IMAGE_KEY);

        init();

        Picasso.with(ProductDetails.this).load(imageUrl)
                .into(mImageView);

        mDescriptionTextView.setText(description);
        mPriceTextView.setText("$"+price);

    }


    public void init(){
        mImageView = (ImageView)findViewById(R.id.detail_image_view);
        mPriceTextView = (TextView)findViewById(R.id.detail_price_text_view);
        mDescriptionTextView= (TextView) findViewById(R.id.detail_description_text_view);
    }
}
