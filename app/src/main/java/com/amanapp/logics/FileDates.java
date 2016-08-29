package com.amanapp.logics;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Abdullah ALT on 8/27/2016.
 */
class FileDates implements Serializable {

    protected DateFormat dateFormat;
    protected String created;
    protected String modified;

    FileDates(Date created, Date modified) {
        this.created = getString(created);
        this.modified = getString(modified);
    }

    FileDates(Date created, Date modified, String template, Locale locale) {
        this(created, modified);
        dateFormat = new SimpleDateFormat(template, locale);
    }

    String getString(Date date) {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        }
        return dateFormat.format(date);
    }

    String getCreated() {
        return created;
    }

    String getModified() {
        return modified;
    }
}
