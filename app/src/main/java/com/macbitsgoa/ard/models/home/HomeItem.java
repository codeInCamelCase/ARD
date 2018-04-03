package com.macbitsgoa.ard.models.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.macbitsgoa.ard.utils.AHC;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Class representing a home section item.
 *
 * @author Vikramaditya Kukreja
 */
public class HomeItem extends RealmObject {

    /**
     * Unique item key.
     */
    @PrimaryKey
    private String key;

    /**
     * Author info string.
     */
    @NonNull
    private String author = AHC.DEFAULT_AUTHOR;

    /**
     * Date of update/creation.
     */
    @NonNull
    private Date date = Calendar.getInstance().getTime();

    /**
     * List containing sub sections as defined in Firebase.
     */
    @NonNull
    private RealmList<PhotoItem> images;

    /**
     * List containing sub sections as defined in Firebase.
     */
    @NonNull
    private RealmList<TextItem> texts;

    /**
     * Constructor with no args.
     */
    public HomeItem() {
        images = new RealmList<>();
        texts = new RealmList<>();
    }

    /**
     * Overloaded constructor.
     *
     * @param key    Unique key.
     * @param author Author string.
     * @param date   Date object of post.
     * @param images List of sub section image items.
     * @param texts  List of sub section text items.
     */
    public HomeItem(@NonNull final String key, @NonNull final String author,
                    @NonNull final Date date, @NonNull final RealmList<PhotoItem> images,
                    @NonNull final RealmList<TextItem> texts) {
        this.key = key;
        this.author = author;
        this.date = date;
        this.images = images;
        this.texts = texts;
    }

    @NonNull
    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    @NonNull
    public String getAuthor() {
        return author;
    }

    public void setAuthor(@Nullable final String author) {
        if (author != null && author.length() != 0) {
            this.author = author;
        }
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@Nullable Date date) {
        if (date != null) {
            this.date = date;
        }
    }

    @NonNull
    public RealmList<PhotoItem> getImages() {
        return images;
    }

    public void setImages(@NonNull final RealmList<PhotoItem> images) {
        this.images = images;
    }

    @NonNull
    public RealmList<TextItem> getTexts() {
        return texts;
    }

    public void setTexts(@NonNull final RealmList<TextItem> texts) {
        this.texts = texts;
    }

    @Override
    public String toString() {
        return "HomeItem{"
                + "key='" + key + '\''
                + ", author='" + author + '\''
                + ", date=" + date
                + ", images=" + images.toString()
                + ", texts=" + texts.toString()
                + '}';
    }
}
