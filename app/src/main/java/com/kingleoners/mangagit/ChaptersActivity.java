package com.kingleoners.mangagit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.kingleoners.mangagit.Adapter.MyChapterAdapter;
import com.kingleoners.mangagit.Common.Common;
import com.kingleoners.mangagit.Model.Manga;

public class ChaptersActivity extends AppCompatActivity {

    RecyclerView recycler_chapter;
    TextView txt_chapter_name;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        //View
        txt_chapter_name = (TextView)findViewById(R.id.txt_chapter_name);
        recycler_chapter = (RecyclerView)findViewById(R.id.recycler_chapter);
        recycler_chapter.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_chapter.setLayoutManager(layoutManager);
        recycler_chapter.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(Common.mangaSelected.Name);

        //Set Icon
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fetchChapter(Common.mangaSelected);
    }

    private void fetchChapter(Manga mangaSelected) {
        Common.chapterList = mangaSelected.Chapters;
        recycler_chapter.setAdapter(new MyChapterAdapter(this,mangaSelected.Chapters));
        txt_chapter_name.setText(new StringBuilder("CHAPTERS (").append(mangaSelected.Chapters.size()).append(")"));
    }
}
