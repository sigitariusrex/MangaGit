package com.kingleoners.mangagit;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kingleoners.mangagit.Adapter.MyMangaAdapter;
import com.kingleoners.mangagit.Adapter.MySliderAdapter;
import com.kingleoners.mangagit.Common.Common;
import com.kingleoners.mangagit.Interface.IBannerLoadDone;
import com.kingleoners.mangagit.Interface.IMangaLoadDone;
import com.kingleoners.mangagit.Model.Manga;
import com.kingleoners.mangagit.Service.PicassoLoadingService;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity implements IBannerLoadDone, IMangaLoadDone {

    Slider slider;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycler_manga;
    TextView txt_manga;
    ImageView btn_filter_search;

    //Database
    DatabaseReference banners,manga;

    //Listener
    IBannerLoadDone bannerListener;
    IMangaLoadDone mangaListener;

    android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Database
        banners = FirebaseDatabase.getInstance().getReference("Banners");
        manga = FirebaseDatabase.getInstance().getReference("Manga");

        //Init Listener
        bannerListener = this;
        mangaListener = this;

        btn_filter_search = (ImageView)findViewById(R.id.btn_show_filter_search);
        btn_filter_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FilterSearchActivity.class));
            }
        });

        slider = (Slider)findViewById(R.id.slider);
        Slider.init(new PicassoLoadingService());

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBanner();
                loadManga();
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
                loadManga();
            }
        });

        recycler_manga = (RecyclerView)findViewById(R.id.recycler_manga);
        recycler_manga.setHasFixedSize(true);
        recycler_manga.setLayoutManager(new GridLayoutManager(this,2));

        txt_manga = (TextView)findViewById(R.id.txt_manga);
    }

    private void loadManga() {

        //Show Dialog
        alertDialog = new SpotsDialog.Builder().setContext(this)
                .setCancelable(false)
                .setMessage("Please wait...")
                .build();

        if (!swipeRefreshLayout.isRefreshing())
        alertDialog.show();

        manga.addListenerForSingleValueEvent(new ValueEventListener() {

            List<Manga> manga_load = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot mangaSnapShot:dataSnapshot.getChildren())
                {
                    Manga manga = mangaSnapShot.getValue(Manga.class);
                    manga_load.add(manga);
                }

                mangaListener.onMangaLoadDoneListener(manga_load);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBanner() {
        banners.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> bannerList = new ArrayList<>();
                for (DataSnapshot bannerSnapShot:dataSnapshot.getChildren())
                {
                    String image = bannerSnapShot.getValue(String.class);
                    bannerList.add(image);
                }

                //Call Listener
                bannerListener.onBannerLoadDoneListener(bannerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBannerLoadDoneListener(List<String> banners) {
        slider.setAdapter(new MySliderAdapter(banners));

    }

    @Override
    public void onMangaLoadDoneListener(List<Manga> mangaList) {
        Common.mangaList = mangaList;

        recycler_manga.setAdapter(new MyMangaAdapter(getBaseContext(),mangaList));

        txt_manga.setText(new StringBuilder("NEW MANGA (")
        .append(mangaList.size())
        .append(")"));

        if (!swipeRefreshLayout.isRefreshing())
            alertDialog.dismiss();
    }
}
