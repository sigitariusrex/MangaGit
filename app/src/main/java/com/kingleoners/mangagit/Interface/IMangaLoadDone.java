package com.kingleoners.mangagit.Interface;

import com.kingleoners.mangagit.Model.Manga;

import java.util.List;

public interface IMangaLoadDone {

    void onMangaLoadDoneListener(List<Manga> mangaList);
}
