package com.example.android.newsapp;

public class News {

    private String mSectionName, mWebTitle, mPublicationDate, mWebUrl, mAuthorName;

    public News(String sectionName, String webTitle, String publicationDate, String authorName, String webUrl) {
        mSectionName = sectionName;
        mWebTitle = webTitle;
        mPublicationDate = publicationDate;
        mAuthorName = authorName;
        mWebUrl = webUrl;
    }

    public String getSectionName() {
        return mSectionName;
    }


    public String getWebTitle() {
        return mWebTitle;
    }


    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getAuthorName() {
        return mAuthorName;
    }


    public String getWebUrl() {
        return mWebUrl;
    }
}
