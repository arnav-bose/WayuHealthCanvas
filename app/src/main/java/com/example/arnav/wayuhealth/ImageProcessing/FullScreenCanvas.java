package com.example.arnav.wayuhealth.ImageProcessing;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.arnav.wayuhealth.Dashboard;
import com.example.arnav.wayuhealth.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FullScreenCanvas extends AppCompatActivity implements View.OnClickListener {

    CanvasView canvasView;
    Bitmap bitmap;
    Bitmap bitmapAltered;
    private int selectedColor;
    ColorPickerDialog dialog;


    private static final String TAG_COLOR_PICKER = "colorPicker";
    private static final String TAG_UNDO = "undo";
    private static final String TAG_REDO = "redo";
    private static final String TAG_STROKE_WIDTH = "strokeWidth";

    private static final String TAG_RECTANGLE = "rectangle";
    private static final String TAG_CIRCLE = "circle";
    private static final String TAG_CROP = "crop";
    private static final String TAG_SAVE = "save";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen_canvas);

        canvasView = (CanvasView) findViewById(R.id.canvasView);

        assert canvasView != null;
        canvasView.setBaseColor(Color.TRANSPARENT);

        Bundle bundle = getIntent().getExtras();
        if(bundle==null){
            Toast.makeText(this, "Image not in Bundle", Toast.LENGTH_LONG).show();
            finish();
        }else{
            try{
                String stringCroppedImage = bundle.getString("uriCroppedImage", "");
                Uri uriCroppedImage = Uri.parse(stringCroppedImage);
                Bitmap bitmapCropped = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriCroppedImage);
                Drawable d = new BitmapDrawable(getResources(), bitmapCropped);
                canvasView.setBackground(d);
            }catch (IOException e){
                e.printStackTrace();
            }
        }


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

        FloatingActionButton floatingActionButtonCanvasEdit = new FloatingActionButton.Builder(this)
                .setContentView(imageViewFloatingActionButton)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .build();

        //Sub Floating Action Buttons
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

        //Floating Action Button Menu
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(subActionButtonColorPicker)
                .attachTo(floatingActionButtonCanvasEdit)
                .addSubActionView(subActionButtonUndo)
                .attachTo(floatingActionButtonCanvasEdit)
                .addSubActionView(subActionButtonRedo)
                .attachTo(floatingActionButtonCanvasEdit)
                .addSubActionView(subActionButtonStrokeWidth)
                .attachTo(floatingActionButtonCanvasEdit)
                .build();

        //Second Floating Action Button
        ImageView imageViewFloatingActionButtonCanvasShape = new ImageView(this);
        imageViewFloatingActionButtonCanvasShape.setImageResource(R.drawable.ic_donut_large_white_24dp);

        FloatingActionButton floatingActionButtonCanvasShape = new FloatingActionButton.Builder(this)
                .setContentView(imageViewFloatingActionButtonCanvasShape)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .setPosition(6)
                .build();

        SubActionButton.Builder itemBuilderCanvasShape = new SubActionButton.Builder(this);
        itemBuilderCanvasShape.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_blue_selector));

        ImageView imageViewRectangle = new ImageView(this);
        imageViewRectangle.setImageResource(R.drawable.ic_crop_square_white_24dp);
        SubActionButton subActionButtonRectangle = itemBuilderCanvasShape.setContentView(imageViewRectangle).build();
        subActionButtonRectangle.setTag(TAG_RECTANGLE);
        subActionButtonRectangle.setOnClickListener(this);


        ImageView imageViewCircle = new ImageView(this);
        imageViewCircle.setImageResource(R.drawable.blank_circle);
        SubActionButton subActionButtonCircle = itemBuilderCanvasShape.setContentView(imageViewCircle).build();
        subActionButtonCircle.setTag(TAG_CIRCLE);
        subActionButtonCircle.setOnClickListener(this);

        ImageView imageViewCrop = new ImageView(this);
        imageViewCrop.setImageResource(R.drawable.ic_format_shapes_white_24dp);
        SubActionButton subActionButtonCrop = itemBuilderCanvasShape.setContentView(imageViewCrop).build();
        subActionButtonCrop.setTag(TAG_CROP);
        subActionButtonCrop.setOnClickListener(this);

        ImageView imageViewSave = new ImageView(this);
        imageViewSave.setImageResource(R.drawable.ic_save_white_24dp);
        SubActionButton subActionButtonSave = itemBuilderCanvasShape.setContentView(imageViewSave).build();
        subActionButtonSave.setTag(TAG_SAVE);
        subActionButtonSave.setOnClickListener(this);

        //Floating Action Button Menu
        FloatingActionMenu actionMenuShape = new FloatingActionMenu.Builder(this)
                .addSubActionView(subActionButtonCrop)
                .attachTo(floatingActionButtonCanvasShape)

                .addSubActionView(subActionButtonRectangle)
                .attachTo(floatingActionButtonCanvasShape)

                .addSubActionView(subActionButtonCircle)
                .attachTo(floatingActionButtonCanvasShape)

                .addSubActionView(subActionButtonSave)
                .attachTo(floatingActionButtonCanvasShape)

                .setStartAngle(270)
                .setEndAngle(360)
                .build();


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
        } else if(v.getTag().equals(TAG_CIRCLE)){
            canvasView.setDrawer(CanvasView.Drawer.CIRCLE);
        } else if (v.getTag().equals(TAG_RECTANGLE)){
            canvasView.setDrawer(CanvasView.Drawer.RECTANGLE);
        } else if (v.getTag().equals(TAG_CROP)){
            bitmapAltered = saveImage();
            Uri uriAltered = getImageUri(FullScreenCanvas.this, bitmapAltered);
            Bundle bundle = new Bundle();
            bundle.putString("uriAltered", uriAltered.toString());
            Intent i = new Intent(FullScreenCanvas.this, CropImage.class);
            i.putExtras(bundle);
            startActivity(i);
            finish();
        } else if(v.getTag().equals(TAG_SAVE)){
            Bitmap bitmapFinalImage = saveImage();
            Uri uriFinalImage = getImageUri(FullScreenCanvas.this, bitmapFinalImage);
            Bundle bundle = new Bundle();
            bundle.putString("uriFinalImage", uriFinalImage.toString());
            Intent i = new Intent(FullScreenCanvas.this, Dashboard.class);
            i.putExtras(bundle);
            startActivity(i);
            finish();
        }
    }

    public Bitmap saveImage(){
        return canvasView.getBitmap();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "AlteredImage", null);
        return Uri.parse(path);
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
