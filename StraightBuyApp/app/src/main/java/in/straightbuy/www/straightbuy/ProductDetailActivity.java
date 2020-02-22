package in.straightbuy.www.straightbuy;

import android.arch.core.executor.DefaultTaskExecutor;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Objects;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ProductDetailActivity extends AppCompatActivity {

    private Button addToCartButton;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ProductDetails product = getIntent().getExtras().getParcelable("Product");

        ImageView productImageview = (ImageView) findViewById(R.id.image_imageview);
        TextView productTextView = (TextView) findViewById(R.id.name_textview);
        TextView priceTextview = (TextView) findViewById(R.id.price_textview);
        TextView opriceTextView = (TextView) findViewById(R.id.oprice_textview);
        TextView discountTextview = (TextView) findViewById(R.id.discount_textview);
        TextView descriptionTextView = (TextView) findViewById(R.id.description_textview);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);

        Glide
                .with(this)
                .setDefaultRequestOptions(requestOptions.centerCrop())
                .load(product.getImageUrl())
                .transition(withCrossFade())
                .into(productImageview);

        productTextView.setText(product.getProductName());
        priceTextview.setText(("₹" + product.getPrice()));
        opriceTextView.setText(product.getOprice());
        opriceTextView.setPaintFlags(opriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
        discountTextview.setText((product.getDiscount() + "% off"));
        descriptionTextView.setText(product.getDescription());

        //ADD TO CART BUTTON

        addToCartButton = (Button) findViewById(R.id.addToCart_button);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                 String uniqueId = user.getUid();
                 mFirebaseDatabase = FirebaseDatabase.getInstance();
                 mDatabaseReference = mFirebaseDatabase.getReference().child(("CARTS OF USER/" + uniqueId));
                 mDatabaseReference.push().setValue(product);
                 Toast.makeText(ProductDetailActivity.this, "ADDED TO CART SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
