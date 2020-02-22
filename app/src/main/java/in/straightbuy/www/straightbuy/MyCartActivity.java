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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

public class MyCartActivity extends AppCompatActivity {

    private FirebaseDatabase mProductDatabase;
    private DatabaseReference mProductReference;
    private ChildEventListener mProductListener;

    private ProductAdapter mproductAdapter;

    private ListView productsListView;

    private LinearLayout bottomLinearLayout,centerLinearLayout;
    private Button continueButton;
    private TextView totalPriceTextView;

    private int cartTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        cartTotal = 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        continueButton = (Button) findViewById(R.id.continue_button);
        totalPriceTextView = (TextView) findViewById(R.id.cartTotal_textview);
        bottomLinearLayout = (LinearLayout) findViewById(R.id.bottomLayout_linearLayout);
        centerLinearLayout = (LinearLayout) findViewById(R.id.emptycart_linearLayout);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uniqueId = user.getUid();

        mProductDatabase = FirebaseDatabase.getInstance();
        mProductReference = mProductDatabase.getReference();

        mProductReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(("CARTS OF USER/" + uniqueId))){
                    bottomLinearLayout.setVisibility(View.VISIBLE);
                    centerLinearLayout.setVisibility(View.GONE);
                }
                else{
                    bottomLinearLayout.setVisibility(View.GONE);
                    centerLinearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mProductReference = mProductDatabase.getReference().child(("CARTS OF USER/" + uniqueId));

        final ArrayList<ProductDetails> products = new ArrayList<ProductDetails>();

        mproductAdapter = new ProductAdapter(this, products);

        productsListView = (ListView) findViewById(R.id.listview_products);
        productsListView.setAdapter(mproductAdapter);

        mProductListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ProductDetails product = dataSnapshot.getValue(ProductDetails.class);
                mproductAdapter.add(product);
                try {
                    cartTotal = cartTotal + Integer.parseInt(product.getPrice());
                }
                catch(Exception NumberFormatException){
                    Toast.makeText(MyCartActivity.this,"NUMBER FORMAT EXCEPTION",Toast.LENGTH_SHORT).show();
                }
                String cartTotalString = "₹" + String.valueOf(cartTotal);

                totalPriceTextView.setText(cartTotalString);
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

                Intent intent = new Intent(MyCartActivity.this,ProductDetailActivity.class);

                intent.putExtra("Product",currentProduct);

                startActivity(intent);
            }
        });

        //TOTAL PRICE TEXTVIEW AND CONTINUE BUTTON
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyCartActivity.this,"Continuing",Toast.LENGTH_SHORT).show();
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
