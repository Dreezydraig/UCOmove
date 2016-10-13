package com.gnzlt.ucotren.model;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("News")
public class New extends ParseObject {

    public New() {
    }

    public String getTitle() {
        return getString("title");
    }

    public Spanned getFormattedBody() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(getString("text"), Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(getString("text"));
        }
    }

    public Date getDate() {
        return getDate("date");
    }

    public ParseFile getImage() {
        return getParseFile("image");
    }
}
