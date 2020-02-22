package in.straightbuy.www.straightbuy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProductCategoryActivity extends AppCompatActivity {

    private ListView productsListView;

    private FirebaseDatabase mProductDatabase;
    private DatabaseReference mProductReference;
    private ChildEventListener mProductListener;

    private ProductAdapter mproductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //GET THE BUNDLE
        Bundle bundle = getIntent().getExtras();

        //GET THE STRING
        String category = bundle.getString("categoryName");
        category = category.toUpperCase();

        mProductDatabase = FirebaseDatabase.getInstance();
        mProductReference = mProductDatabase.getReference().child(category);

        final ArrayList<ProductDetails> products = new ArrayList<ProductDetails>();

        mproductAdapter = new ProductAdapter(this, products);

        productsListView = (ListView) findViewById(R.id.listview_products);
        productsListView.setAdapter(mproductAdapter);

        mProductListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ProductDetails product = dataSnapshot.getValue(ProductDetails.class);
                mproductAdapter.add(product);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mProductReference.addChildEventListener(mProductListener);


        //ONCLICK LISTENER FOR LISTVIEW PRODUCTS

        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProductDetails currentProduct = products.get(i);

                Intent intent = new Intent(ProductCategoryActivity.this,ProductDetailActivity.class);

                intent.putExtra("Product",currentProduct);

                startActivity(intent);
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
