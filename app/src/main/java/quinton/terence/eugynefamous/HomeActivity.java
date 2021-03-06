package quinton.terence.eugynefamous;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import quinton.terence.eugynefamous.Admin.AdminMaintainProductsActivity;
import quinton.terence.eugynefamous.Model.products;
import quinton.terence.eugynefamous.ViewHolder.StartProductViewHolder;
import quinton.terence.eugynefamous.common.LogInActivity;
import quinton.terence.eugynefamous.prevalent.prevalent;
import quinton.terence.eugynefamous.productsdetails.ProductDetailsActivity;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //variable for the navigation drawer
    static final float End_SCALE = 0.7f;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuicon;
    private RelativeLayout contenttView;
    private FloatingActionButton cartbtn;
    private ProgressBar progressBar;

    //getting intent

    private String type = "";

    //variable for database
    DatabaseReference shatiref, shoesRef, ShortsRef, TrousersRef, tShirtsRef;

    //firebase adapters for our setting our recycler views
    FirebaseRecyclerAdapter<products, StartProductViewHolder> adapter;
    FirebaseRecyclerAdapter<products, StartProductViewHolder> shoeAdapter;
    FirebaseRecyclerAdapter<products, StartProductViewHolder> trouserAdapter;
    FirebaseRecyclerAdapter<products, StartProductViewHolder> tShirtAdapter;
    FirebaseRecyclerAdapter<products, StartProductViewHolder> shortAdapter;

    //populating our recyclerview with data
    private RecyclerView recyclerView, shoesList, trouserList, tshirtsList, shortsList;
    RecyclerView.LayoutManager layoutManager;

    private TextView shirtsShowMore, shoesShowMore, shortShowMore, trouserShowMore, tShirtShowMore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            type = getIntent().getExtras().get("Admin").toString();

        }


        cartbtn = findViewById(R.id.cart_floating);

        //hooks for the drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuicon = findViewById(R.id.menuicon);
        contenttView = findViewById(R.id.content_view);


        recyclerView = findViewById(R.id.recyclermenu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        shoesList = findViewById(R.id.shoes_recycler);
        shoesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        shoesList.setHasFixedSize(true);

        trouserList = findViewById(R.id.trouser_recycler);
        trouserList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trouserList.setHasFixedSize(true);

        tshirtsList = findViewById(R.id.tshirts_recycler);
        tshirtsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tshirtsList.setHasFixedSize(true);

        shortsList = findViewById(R.id.short_recycler);
        shortsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        shortsList.setHasFixedSize(true);

        shirtsShowMore = findViewById(R.id.shirts_more_);
        shoesShowMore = findViewById(R.id.shoes_more_);
        trouserShowMore = findViewById(R.id.trouser_more_);
        tShirtShowMore = findViewById(R.id.tshirts_more_);
        shortShowMore = findViewById(R.id.shorts_more_);

        progressBar = findViewById(R.id.progress_bar_home);


        //hooks for database
        shatiref = FirebaseDatabase.getInstance().getReference().child("shirts").child("men");
        shoesRef = FirebaseDatabase.getInstance().getReference().child("shoes").child("men");
        ShortsRef = FirebaseDatabase.getInstance().getReference().child("shorts").child("men");
        TrousersRef = FirebaseDatabase.getInstance().getReference().child("trousers").child("men");
        tShirtsRef = FirebaseDatabase.getInstance().getReference().child("tShirts").child("men");


        //initializing header view
        //for drawer menu
        View headerview = navigationView.getHeaderView(0);
        TextView userNameTextView = headerview.findViewById(R.id.nav_username);
        CircleImageView profileImageView = headerview.findViewById(R.id.user_profile_image);


        if (!type.equals("Admin")) {

            //for setting the name to the text view
            userNameTextView.setText(prevalent.currentOnlineUser.getUsername());

            //for setting the image view to our drawer layout
            // Picasso.get().load(prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.usericon).into(profileImageView);


        }


        Paper.init(this);

        //for opening the user drawer
        navigationDrawer();


        //cart button
        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equals("Admin")) {

                    startActivity(new Intent(HomeActivity.this, CartActivity.class));

                }


            }
        });

        shirtsShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, ShirtsActivity.class));


            }
        });

        shoesShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, ShoesActivity.class));


            }
        });

        trouserShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, TrousersActivity.class));


            }
        });

        tShirtShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, tShirtsActivity.class));

                finish();


            }
        });

        shortShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, ShortsActivity.class));


            }
        });


    }

    //navigation drawer methods

    private void navigationDrawer() {

        //navigation drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


        menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {

                    drawerLayout.closeDrawer(GravityCompat.START);

                } else drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {

        //if you wnt a scrim color
        //drawerLayout.setScrimColor(getResources().getColor(R.color.colorminor));

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //scale the view based on the current scale offset

                final float diffScaledOffset = slideOffset * (1 - End_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contenttView.setScaleX(offsetScale);
                contenttView.setScaleY(offsetScale);

                //translate the accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contenttView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contenttView.setTranslationX(xTranslation);


            }

        });

    }


    //for closing the drawer when pressing back button
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);

        } else

            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {


            case R.id.nav_logout:

                if (!type.equals("Admin")) {

                    Paper.book().destroy();

                    Intent intent = new Intent(getApplicationContext(), LogInActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);


                }


                break;

            case R.id.nav_settingsbin:

                if (!type.equals("Admin")) {

                    startActivity(new Intent(HomeActivity.this, SettingsActivity.class));


                }


                break;

            case R.id.nav_cart:

                if (!type.equals("Admin")) {

                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
                }


                break;

            case R.id.nav_tshirts:

                startActivity(new Intent(HomeActivity.this, tShirtsActivity.class).putExtra("type", type));


                break;

            case R.id.nav_shirt:

                startActivity(new Intent(HomeActivity.this, ShirtsActivity.class).putExtra("ShirtsHome", type));


                break;

            case R.id.nav_shoes:

                startActivity(new Intent(HomeActivity.this, ShoesActivity.class).putExtra("shoesHome", type));


                break;

            case R.id.nav_socks:

                startActivity(new Intent(HomeActivity.this, SocksActivity.class).putExtra("socksHome", type));


                break;

            case R.id.nav_belts:

                startActivity(new Intent(HomeActivity.this, BeltsActivity.class).putExtra("beltsHome", type));


                break;

            case R.id.nav_hats:

                startActivity(new Intent(HomeActivity.this, HatsActivity.class).putExtra("hatsHome", type));


                break;

            case R.id.nav_shorts:

                startActivity(new Intent(HomeActivity.this, ShortsActivity.class).putExtra("shortHome", type));


                break;

            case R.id.nav_bags:

                startActivity(new Intent(HomeActivity.this, BagsActivity.class).putExtra("bagsHome", type));


                break;

            case R.id.nav_dresses:

                startActivity(new Intent(HomeActivity.this, DressesActivity.class).putExtra("dressesHome", type));


                break;

            case R.id.nav_trouser:

                startActivity(new Intent(HomeActivity.this, TrousersActivity.class).putExtra("trouserHome", type));


                break;

            case R.id.nav_sweaters:

                startActivity(new Intent(HomeActivity.this, SweaterActivity.class).putExtra("sweaterHome", type));


                break;

            case R.id.nav_skirt:

                startActivity(new Intent(HomeActivity.this, SkirtsActivity.class).putExtra("skirtHome", type));


                break;

            case R.id.nav_lingerie:

                startActivity(new Intent(HomeActivity.this, LingerieActivity.class).putExtra("lingerieHome", type));


                break;

            case R.id.nav_jumpsuits:

                startActivity(new Intent(HomeActivity.this, JumpSuitsActivity.class).putExtra("jumpSuitsHome", type));


                break;

            case R.id.nav_tops:

                startActivity(new Intent(HomeActivity.this, TopsActtivity.class).putExtra("topsHome", type));


        }


        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        HomeAsyncTask task = new HomeAsyncTask(this);
        task.execute(1);


    }


    private class HomeAsyncTask extends AsyncTask<Integer, Integer, String> {


        private WeakReference<HomeActivity> activityWeakReference;

        //a constructor for the weak reference
        HomeAsyncTask(HomeActivity activity) {

            activityWeakReference = new WeakReference<HomeActivity>(activity);


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            HomeActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            recyclerView.setVisibility(View.VISIBLE);
            shoesList.setVisibility(View.VISIBLE);
            trouserList.setVisibility(View.VISIBLE);
            tshirtsList.setVisibility(View.VISIBLE);
            shortsList.setVisibility(View.VISIBLE);

            progressBar.setVisibility(View.VISIBLE);

            Toast.makeText(activity, "loading...", Toast.LENGTH_SHORT).show();


        }

        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0]; i++) {

                publishProgress((i * 100) / integers[0]);

                if (i == 0) {

                    //adding a query to retrieve all products
                    FirebaseRecyclerOptions<products> options =
                            new FirebaseRecyclerOptions.Builder<products>()
                                    .setQuery(shatiref.limitToFirst(5), products.class)
                                    .build();


                    //adding a firebase recycler adapter

                    adapter =
                            new FirebaseRecyclerAdapter<products, StartProductViewHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int i, @NonNull final products model) {

                                    holder.txtProductName.setText(model.getPname());

                                    holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                                    Picasso.get().load(model.getImage()).placeholder(R.drawable.progressload).into(holder.imageView);


                                    //setting a click listener to the relative layout


                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (type.equals("Admin")) {


                                                Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            } else {

                                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);

                                            }


                                        }
                                    });


                                }

                                @NonNull
                                @Override
                                public StartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.start_products_layout, parent, false);

                                    StartProductViewHolder holder = new StartProductViewHolder(view);

                                    return holder;


                                }
                            };


                    //adding a query to retrieve all products
                    FirebaseRecyclerOptions<products> shoeOptions =
                            new FirebaseRecyclerOptions.Builder<products>()
                                    .setQuery(shoesRef.limitToFirst(5), products.class)
                                    .build();


                    //adding a firebase recycler adapter

                    shoeAdapter =
                            new FirebaseRecyclerAdapter<products, StartProductViewHolder>(shoeOptions) {
                                @Override
                                protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int i, @NonNull final products model) {

                                    holder.txtProductName.setText(model.getPname());

                                    holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                                    Picasso.get().load(model.getImage()).placeholder(R.drawable.progressload).into(holder.imageView);


                                    //setting a click listener to the relative layout

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (type.equals("Admin")) {


                                                Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            } else {


                                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);

                                            }


                                        }
                                    });


                                }

                                @NonNull
                                @Override
                                public StartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.start_products_layout, parent, false);

                                    StartProductViewHolder holder = new StartProductViewHolder(view);

                                    return holder;


                                }
                            };


                    //adding a query to retrieve all products
                    FirebaseRecyclerOptions<products> trouserOptions =
                            new FirebaseRecyclerOptions.Builder<products>()
                                    .setQuery(TrousersRef.limitToFirst(5), products.class)
                                    .build();


                    //adding a firebase recycler adapter

                    trouserAdapter =
                            new FirebaseRecyclerAdapter<products, StartProductViewHolder>(trouserOptions) {
                                @Override
                                protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int i, @NonNull final products model) {

                                    holder.txtProductName.setText(model.getPname());

                                    holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                                    Picasso.get().load(model.getImage()).placeholder(R.drawable.progressload).into(holder.imageView);


                                    //setting a click listener to the relative layout

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (type.equals("Admin")) {


                                                Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);

                                            } else {

                                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }


                                        }
                                    });


                                }

                                @NonNull
                                @Override
                                public StartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.start_products_layout, parent, false);

                                    StartProductViewHolder holder = new StartProductViewHolder(view);

                                    return holder;


                                }
                            };


                    //adding a query to retrieve all products
                    FirebaseRecyclerOptions<products> tShirtsOptions =
                            new FirebaseRecyclerOptions.Builder<products>()
                                    .setQuery(tShirtsRef.limitToFirst(5), products.class)
                                    .build();


                    //adding a firebase recycler adapter

                    tShirtAdapter =
                            new FirebaseRecyclerAdapter<products, StartProductViewHolder>(tShirtsOptions) {
                                @Override
                                protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int i, @NonNull final products model) {

                                    holder.txtProductName.setText(model.getPname());

                                    holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                                    Picasso.get().load(model.getImage()).placeholder(R.drawable.progressload).into(holder.imageView);


                                    //setting a click listener to the relative layout

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (type.equals("Admin")) {


                                                Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);

                                            } else {

                                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);

                                            }


                                        }
                                    });


                                }

                                @NonNull
                                @Override
                                public StartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.start_products_layout, parent, false);

                                    StartProductViewHolder holder = new StartProductViewHolder(view);

                                    return holder;


                                }
                            };


                    //adding a query to retrieve all products
                    FirebaseRecyclerOptions<products> shortOptions =
                            new FirebaseRecyclerOptions.Builder<products>()
                                    .setQuery(ShortsRef.limitToFirst(5), products.class)
                                    .build();


                    //adding a firebase recycler adapter

                    shortAdapter =
                            new FirebaseRecyclerAdapter<products, StartProductViewHolder>(shortOptions) {
                                @Override
                                protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int i, @NonNull final products model) {

                                    holder.txtProductName.setText(model.getPname());

                                    holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                                    Picasso.get().load(model.getImage()).placeholder(R.drawable.progressload).into(holder.imageView);


                                    //setting a click listener to the relative layout

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (type.equals("Admin")) {


                                                Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);

                                            } else {

                                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }

                                        }
                                    });


                                }

                                @NonNull
                                @Override
                                public StartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.start_products_layout, parent, false);

                                    StartProductViewHolder holder = new StartProductViewHolder(view);

                                    return holder;


                                }
                            };

                }


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }

            }


            return "Updated";

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            HomeActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            activity.progressBar.setProgress(values[0]);


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            HomeActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            recyclerView.setAdapter(adapter);
            adapter.startListening();


            shoesList.setAdapter(shoeAdapter);
            shoeAdapter.startListening();


            trouserList.setAdapter(trouserAdapter);
            trouserAdapter.startListening();


            tshirtsList.setAdapter(tShirtAdapter);
            tShirtAdapter.startListening();


            shortsList.setAdapter(shortAdapter);
            shortAdapter.startListening();

            recyclerView.setVisibility(View.VISIBLE);
            shoesList.setVisibility(View.VISIBLE);
            trouserList.setVisibility(View.VISIBLE);
            tshirtsList.setVisibility(View.VISIBLE);
            shortsList.setVisibility(View.VISIBLE);

            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);


            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();

        }
    }

}