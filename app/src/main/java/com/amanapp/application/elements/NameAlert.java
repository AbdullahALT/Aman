package com.amanapp.application.elements;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.amanapp.R;

/**
 * Created by Abdullah ALT on 12/22/2016.
 */

public class NameAlert {

    private AlertDialog.Builder builder;

    public NameAlert(Context context, String title, String positive, final onPositive onPositive) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogContent = inflater.inflate(R.layout.dialog_name, null);
        final EditText name = (EditText) dialogContent.findViewById(R.id.content_name);
        builder = new AlertDialog.Builder(context)
                .setView(dialogContent)
                .setTitle(title)
                .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onPositive.click(name.getText().toString().trim());
                    }
                })
                .setNegativeButton("Cancel", null);
    }

    public void show() {
        builder.create().show();
    }

    public interface onPositive {
        void click(String name);
    }

}
