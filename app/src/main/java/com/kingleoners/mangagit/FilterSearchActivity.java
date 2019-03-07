package com.kingleoners.mangagit;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kingleoners.mangagit.Adapter.MyMangaAdapter;
import com.kingleoners.mangagit.Common.Common;
import com.kingleoners.mangagit.Model.Manga;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FilterSearchActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView recycler_filter_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);

        recycler_filter_search = (RecyclerView)findViewById(R.id.recycler_filter_search);
        recycler_filter_search.setHasFixedSize(true);
        recycler_filter_search.setLayoutManager(new GridLayoutManager(this,2));

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_nav);
        bottomNavigationView.inflateMenu(R.menu.main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.action_filter:
                        showFiltersDialog();
                        break;
                    case R.id.action_search:
                        showSearchDialog();
                        break;
                        default:
                            break;
                }
                return true;
            }
        });

    }

    private void showSearchDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterSearchActivity.this);
        alertDialog.setTitle("Search ");

        final LayoutInflater inflater = this.getLayoutInflater();
        View search_layout = inflater.inflate(R.layout.dialog_search,null);

        final EditText git_search = (EditText)search_layout.findViewById(R.id.git_search);

        alertDialog.setView(search_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fetchSearchManga(git_search.getText().toString());
            }
        });

        alertDialog.show();

    }

    private void fetchSearchManga(String query) {
        List<Manga> manga_search = new ArrayList<>();
        for (Manga manga:Common.mangaList)
        {
            if (manga.Name.contains(query))
                manga_search.add(manga);
        }

        if (manga_search.size() > 0)
            recycler_filter_search.setAdapter(new MyMangaAdapter(getBaseContext(),manga_search));
        else
            Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show();
    }

    private void showFiltersDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterSearchActivity.this);
        alertDialog.setTitle("Select Category");

        final LayoutInflater inflater = this.getLayoutInflater();
        View filter_layout = inflater.inflate(R.layout.dialog_options,null);

        final AutoCompleteTextView txt_category = (AutoCompleteTextView)filter_layout.findViewById(R.id.txt_category);
        final ChipGroup chipGroup = (ChipGroup)filter_layout.findViewById(R.id.chipGroup);

        //Create Autocomplete
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.select_dialog_item, Common.categories);
        txt_category.setAdapter(adapter);
        txt_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Clear
                txt_category.setText("");

                //Create Tags
                Chip chip = (Chip) inflater.inflate(R.layout.chip_item,null,false);
                chip.setText(((TextView)view).getText());
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chipGroup.removeView(view);
                    }
                });

                chipGroup.addView(chip);
            }
        });

        alertDialog.setView(filter_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("FILTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> filter_key = new ArrayList<>();
                StringBuilder filter_query = new StringBuilder("");
                for (int j=0; j<chipGroup.getChildCount(); j++)
                {
                    Chip chip = (Chip)chipGroup.getChildAt(j);
                    filter_key.add(chip.getText().toString());
                }

                //Sort Filter A-Z
                Collections.sort(filter_key);

                //Convert String
                for (String key:filter_key)
                {
                    filter_query.append(key).append(",");
                }

                //Remove
                filter_query.setLength(filter_query.length()-1);

                //Filter Query
                fetchFilterCategory(filter_query.toString());
            }
        });

        alertDialog.show();
    }

    private void fetchFilterCategory(String query) {
        List<Manga> manga_filtered = new ArrayList<>();
        for (Manga manga:Common.mangaList)
        {
            if (manga.Category != null) {
                if (manga.Category.contains(query))
                    manga_filtered.add(manga);
            }
        }

        if (manga_filtered.size() > 0)
            recycler_filter_search.setAdapter(new MyMangaAdapter(getBaseContext(),manga_filtered));
        else
            Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show();
    }
}
