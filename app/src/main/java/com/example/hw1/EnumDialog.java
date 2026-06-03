package com.example.hw1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

public class EnumDialog {

    public static void showDialog(Context context, String[] enumStrings) {
        StringBuilder messageBuilder = new StringBuilder();
        if (enumStrings != null && enumStrings.length > 0) {
            for (String enumString : enumStrings) {
                messageBuilder.append(enumString).append("\n");
            }
            messageBuilder.deleteCharAt(messageBuilder.length() - 1); // Remove last newline
        } else {
            messageBuilder.append("No data provided");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enumeration Values");
        builder.setMessage(messageBuilder.toString());
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}