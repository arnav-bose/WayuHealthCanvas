package com.example.arnav.wayuhealth.ImageProcessing;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.arnav.wayuhealth.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.xdty.preference.ColorPreference;
import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.io.IOException;

public class FullScreenCanvas extends AppCompatActivity implements View.OnClickListener {

    CanvasView canvasView;
    Bitmap bitmap;
    private int selectedColor;
    ColorPickerDialog dialog;

    private static final String TAG_COLOR_PICKER = "colorPicker";
    private static final String TAG_UNDO = "undo";
    private static final String TAG_REDO = "redo";
    private static final String TAG_STROKE_WIDTH = "strokeWidth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen_canvas);

        canvasView = (CanvasView) findViewById(R.id.canvasView);

        assert canvasView != null;
        canvasView.setBaseColor(Color.TRANSPARENT);

        displayImage();

        //Color Picker
        selectedColor = ContextCompat.getColor(this, R.color.red);
        canvasView.setPaintStrokeColor(selectedColor);
        int[] mColors = getResources().getIntArray(R.array.default_rainbow);
        dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title, mColors, selectedColor, 5, ColorPickerDialog.SIZE_SMALL);

        //OnClickColorPicker
        dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                selectedColor = color;
                canvasView.setPaintStrokeColor(selectedColor);
            }

        });

        //Floating Action Button
        ImageView imageViewFloatingActionButton = new ImageView(this);
        imageViewFloatingActionButton.setImageResource(R.drawable.ic_gesture_white_24dp);

        FloatingActionButton floatingActionButtonCanvas = new FloatingActionButton.Builder(this)
                .setContentView(imageViewFloatingActionButton)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_blue_selector));

        ImageView imageViewColorPicker = new ImageView(this);
        imageViewColorPicker.setImageResource(R.drawable.ic_palette_white_24dp);
        SubActionButton subActionButtonColorPicker = itemBuilder.setContentView(imageViewColorPicker).build();
        subActionButtonColorPicker.setTag(TAG_COLOR_PICKER);
        subActionButtonColorPicker.setOnClickListener(this);


        ImageView imageViewUndo = new ImageView(this);
        imageViewUndo.setImageResource(R.drawable.ic_undo_white_24dp);
        SubActionButton subActionButtonUndo = itemBuilder.setContentView(imageViewUndo).build();
        subActionButtonUndo.setTag(TAG_UNDO);
        subActionButtonUndo.setOnClickListener(this);

        ImageView imageViewRedo = new ImageView(this);
        imageViewRedo.setImageResource(R.drawable.ic_redo_white_24dp);
        SubActionButton subActionButtonRedo = itemBuilder.setContentView(imageViewRedo).build();
        subActionButtonRedo.setTag(TAG_REDO);
        subActionButtonRedo.setOnClickListener(this);

        ImageView imageViewStrokeWidth = new ImageView(this);
        imageViewStrokeWidth.setImageResource(R.drawable.ic_create_white_24dp);
        SubActionButton subActionButtonStrokeWidth = itemBuilder.setContentView(imageViewStrokeWidth).build();
        subActionButtonStrokeWidth.setTag(TAG_STROKE_WIDTH);
        subActionButtonStrokeWidth.setOnClickListener(this);

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(subActionButtonColorPicker)
                .attachTo(floatingActionButtonCanvas)
                .addSubActionView(subActionButtonUndo)
                .attachTo(floatingActionButtonCanvas)
                .addSubActionView(subActionButtonRedo)
                .attachTo(floatingActionButtonCanvas)
                .addSubActionView(subActionButtonStrokeWidth)
                .attachTo(floatingActionButtonCanvas)
                .build();
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals(TAG_STROKE_WIDTH)) {
            strokePicker();
        } else if (v.getTag().equals(TAG_UNDO)) {
            canvasView.undo();
        } else if (v.getTag().equals(TAG_REDO)) {
            canvasView.redo();
        } else if(v.getTag().equals(TAG_COLOR_PICKER)){
            dialog.show(getFragmentManager(), "Color Picker");
        }
    }

    public void displayImage() {
        if (TakePicture.optionSelected.equals("Camera")) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), TakePicture.imageUriCamera);
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                canvasView.setBackground(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (TakePicture.optionSelected.equals("Gallery")) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), TakePicture.selectedImageUri);
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                canvasView.setBackground(d);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            finish();
    }

    public void strokePicker(){
        AlertDialog.Builder builderStrokePicker = new AlertDialog.Builder(this);
        String[] types = {"Thin", "Medium", "Fat"};
        builderStrokePicker.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which){
                    case 0: {
                        canvasView.setPaintStrokeWidth(2F);
                    }
                    break;
                    case 1: {
                        canvasView.setPaintStrokeWidth(5F);
                    }
                    break;
                    case 2: {
                        canvasView.setPaintStrokeWidth(12F);
                    }
                    break;
                }
            }
        });

        builderStrokePicker.show();
    }
}
