package com.example.android.newsapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;

import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {


    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView sectionNameView = (TextView) listItemView.findViewById(R.id.section_name);

        sectionNameView.setText(currentNews.getSectionName());

        TextView webTitleView = (TextView) listItemView.findViewById(R.id.web_title);

        webTitleView.setText(currentNews.getWebTitle());

        TextView authorNameView = (TextView) listItemView.findViewById(R.id.author_name);

        authorNameView.setText(currentNews.getAuthorName());

        TextView webUrlView = (TextView) listItemView.findViewById(R.id.web_url);

        webUrlView.setText(currentNews.getWebUrl());

        TextView publicationDateView = (TextView) listItemView.findViewById(R.id.publication_date);
        publicationDateView.setText(currentNews.getPublicationDate());

        return listItemView;
    }


}