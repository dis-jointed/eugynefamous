package quinton.terence.eugynefamous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import quinton.terence.eugynefamous.Model.cart;
import quinton.terence.eugynefamous.ViewHolder.cartViewHolder;
import quinton.terence.eugynefamous.prevalent.prevalent;

public class CartActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessBtn;
    private TextView totalAmount;

    //for storing all the total price
    private int overTotalPrice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProcessBtn = findViewById(R.id.next_process_btn);
        totalAmount = findViewById(R.id.total_price);


        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);

                //String.valueOf(overTotalPrice) converts it to string data type

                intent.putExtra("Total Price", String.valueOf(overTotalPrice));

                startActivity(intent);

                finish();


            }
        });


        totalAmount.setText( "Total Price : "  + String.valueOf(overTotalPrice));

    }

    @Override
    protected void onStart() {
        super.onStart();

        //creating a database refernce
        final DatabaseReference cartlistrefer = FirebaseDatabase.getInstance().getReference().child("Cart List");

        //setting up the recycler view
        FirebaseRecyclerOptions<cart> options =
                new FirebaseRecyclerOptions.Builder<cart>()
                        .setQuery(cartlistrefer.child("User View")
                                .child(prevalent.currentOnlineUser.getPhone())
                                .child("Products"), cart.class)
                        .build();


        FirebaseRecyclerAdapter<cart, cartViewHolder> adapter = new FirebaseRecyclerAdapter<cart, cartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull cartViewHolder holder, int i, @NonNull final cart model) {

                //setting the texts to our recycler view items
                holder.txtProductQuantity.setText("Quantity:" + model.getQuantity());
                holder.txtProductPrice.setText("Price" + model.getPrice() + "Ksh");
                holder.txtProductSize.setText("Size" + model.getSize());
                holder.txtProductName.setText("Name" + model.getPname());
                holder.txtProductColor.setText("Color" + model.getcolor());


                //getting individual total price by calculating price and quantity and later adding it to the overall total price
                int oneTypeProductTPrice  = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());

                //adding this int to our overall total price
                overTotalPrice = overTotalPrice + oneTypeProductTPrice;





                //setting an onClick listener
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //creating a dialog box
                        CharSequence options[] = new CharSequence[]
                                {

                                        "Edit",

                                        "Remove"

                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);

                        builder.setTitle("Cart options");

                        //setting a click listener on the twoo buttons
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == 0) {

                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);

                                    intent.putExtra("pid", model.getPid());

                                    startActivity(intent);


                                } else if (which == 1) {

                                    cartlistrefer.child("User View")
                                            .child(prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        Toast.makeText(CartActivity.this, "item removed", Toast.LENGTH_SHORT).show();


                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);


                                                        startActivity(intent);

                                                    }


                                                }
                                            });

                                }


                            }
                        });

                        builder.show();


                    }
                });


            }

            @NonNull
            @Override
            public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);

                cartViewHolder holder = new cartViewHolder(view);
                return holder;

            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
}