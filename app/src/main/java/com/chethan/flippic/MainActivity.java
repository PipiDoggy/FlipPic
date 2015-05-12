package com.chethan.flippic;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.net.Uri;
import android.opengl.Visibility;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;
import icepick.Icepick;
import icepick.Icicle;
import me.alexrs.prefs.lib.Prefs;
import se.emilsjolander.flipview.FlipView;
import timber.log.Timber;


public class MainActivity extends MyActivity {
    private final String ALL_IMAGES = "ALL IMAGES";
    private final String CAMERA = "Camera";
    private final String WHATSAPP = "WhatsApp Images";
    private final String DOWNLOAD = "Download";
    private final String PINS = "Pins";
    private final String COFFEE = "Coffee";
    private final String SETTINGS = "Settings";

    @InjectView(R.id.foldable_list)
    FlipView  foldableListLayout;

    @InjectView(R.id.action_button)
    FloatingActionsMenu actionMenu;

    @InjectView(R.id.action_delete)
    FloatingActionButton actionDelete;

    @InjectView(R.id.action_share)
    FloatingActionButton actionShare;

    @InjectView(R.id.action_setas)
    FloatingActionButton actionSetAs;

    @Icicle int listPosition;

    ArrayList<String> result = new ArrayList<String>();
    ArrayList<String> allImagesList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        result =allImagesList = getAllImages(getApplicationContext());

     final Drawer.Result drawer = new Drawer()
                .withActivity(this)
                .withActionBarDrawerToggle(true)
                .withCloseOnClick(true)
                .withTranslucentStatusBar(true)
                .withHeader(R.layout.folder_drawer_header_layout)
                .withDrawerItems(getDrawerItems(getFolderList(allImagesList)))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l,final IDrawerItem iDrawerItem) {

                        if (((PrimaryDrawerItem) iDrawerItem).getName().equalsIgnoreCase(COFFEE)) {
                            Intent coffeeIntent = new Intent(getApplicationContext(), CoffeeActivity.class);
                            startActivity(coffeeIntent);
                            
                        } else if (((PrimaryDrawerItem) iDrawerItem).getName().equalsIgnoreCase(SETTINGS)) {
                            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(settingsIntent);
                        } else {
                            actionMenu.collapse();
                            foldableListLayout.smoothFlipTo(0);
                            if (foldableListLayout.getCurrentPage() == 0) {
                                if (((PrimaryDrawerItem) iDrawerItem).getName().equalsIgnoreCase(ALL_IMAGES)) {
                                    result = getAllImages(getApplicationContext());
                                } else {
                                    result = getImagesFromAFolder(allImagesList, ((PrimaryDrawerItem) iDrawerItem).getName());
                                }
                                ((BaseAdapter) foldableListLayout.getAdapter()).notifyDataSetChanged();
                            }
                            foldableListLayout.setOnFlipListener(new FlipView.OnFlipListener() {
                                @Override
                                public void onFlippedToPage(FlipView flipView, int i, long l) {
                                    if (i == 0) {
    //                                    Toast.makeText(MainActivity.this, "clicked " + ((PrimaryDrawerItem) iDrawerItem).getName(), Toast.LENGTH_SHORT).show();
                                        if (((PrimaryDrawerItem) iDrawerItem).getName().equalsIgnoreCase(ALL_IMAGES)) {
                                            result = getAllImages(getApplicationContext());
                                        } else if (((PrimaryDrawerItem) iDrawerItem).getName().equalsIgnoreCase(COFFEE)) {
                                            Intent coffeeIntent = new Intent(getApplicationContext(), CoffeeActivity.class);
                                            startActivity(coffeeIntent);
                                        } else if (((PrimaryDrawerItem) iDrawerItem).getName().equalsIgnoreCase(SETTINGS)) {
                                            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                                            startActivity(settingsIntent);
                                        } else {
                                            result = getImagesFromAFolder(allImagesList, ((PrimaryDrawerItem) iDrawerItem).getName());
                                        }
                                        ((BaseAdapter) foldableListLayout.getAdapter()).notifyDataSetChanged();
                                        foldableListLayout.peakNext(true);
                                    }
                                }
                            });
                         }
                    }
                })
             .withTranslucentStatusBar(true)
             .build();

             if(Prefs.with(getApplicationContext()).getBoolean(SettingsActivity.DRAWER_OPEN,true)){
                 drawer.openDrawer();
             }

            if (Prefs.with(getApplicationContext()).getBoolean(SettingsActivity.BRIGHTNESS,false)){
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.screenBrightness = 1.0f;

                getWindow().setAttributes(lp);
            }

        foldableListLayout.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return result.size();
            }

            @Override
            public Object getItem(int position) {
                return result.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                listPosition = position;
                Timber.d("List position in list view " + listPosition);
                final ViewHolder viewHolder;

                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = getLayoutInflater().inflate(R.layout.item, parent, false);
                    viewHolder.image_container = (HorizontalScrollView) convertView.findViewById(R.id.list_item_imagecontainer);
                    viewHolder.name = (TextView) convertView.findViewById(R.id.list_item_title);
                    viewHolder.image = (ScaleImageView) convertView.findViewById(R.id.list_item_image);
                    viewHolder.name.setTypeface(Utils.getRegularTypeface(getApplicationContext()));
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                ImageLoader.getInstance().displayImage("file://" + result.get(position), viewHolder.image);

                viewHolder.name.setText(getFolderName(result.get(position)));
                viewHolder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(actionMenu.getVisibility() == View.GONE){
                            actionMenu.setVisibility(View.VISIBLE);
                        }else{
                            actionMenu.setVisibility(View.GONE);
                            actionMenu.collapse();
                        }
                    }
                });
                viewHolder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawer.openDrawer();
                    }
                });
                return convertView;
            }
        });

        foldableListLayout.peakNext(true);

        foldableListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actionMenu.getVisibility() == View.GONE){
                    actionMenu.setVisibility(View.VISIBLE);
                }else{
                    actionMenu.setVisibility(View.GONE);
                }
            }
        });

        actionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionMenu.collapse();

                MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                        .title("Flip Pic")
                        .content("Do you really want to delete this image")
                        .positiveText("Yes")
                        .negativeText("No")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                int deletePosition = foldableListLayout.getCurrentPage();
                                getContentResolver().delete(getContentUri(getApplicationContext(), result.get(deletePosition)), null, null);
                                result.remove(deletePosition);
                                ((BaseAdapter)foldableListLayout.getAdapter()).notifyDataSetChanged();
                                //if needed move to next valid position
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        })
                        .show();

            }
        });

        actionSetAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionMenu.collapse();
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.setDataAndType(getContentUri(getApplicationContext(), result.get(foldableListLayout.getCurrentPage())), "image/*");
                intent.putExtra("mimeType", "image/*");
                startActivity(Intent.createChooser(intent, "set as"));

            }
        });

        actionShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionMenu.collapse();
               Intent mShareIntent = new Intent();
                mShareIntent.setAction(Intent.ACTION_SEND);
                mShareIntent.setType("image/*");
                Timber.d("file share name"+result.get(foldableListLayout.getCurrentPage()));
                Uri uri = Uri.fromFile(new File(result.get(foldableListLayout.getCurrentPage())));
                mShareIntent.putExtra(Intent.EXTRA_STREAM, getContentUri(getApplicationContext(),result.get(foldableListLayout.getCurrentPage())));
                startActivity(Intent.createChooser(mShareIntent, "Image"));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @DebugLog
    private String getFolderName(String fileName){
        return fileName.substring(fileName.substring(0, fileName.lastIndexOf("/")).lastIndexOf("/") + 1, fileName.lastIndexOf("/"));
    }

    public static Uri getContentUri(Context context, String imageFilePath) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { imageFilePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        }
        return null;
    }

    @DebugLog
    public ArrayList<String> getAllImages(Context context) {
        ArrayList<String> images = new ArrayList<String>();
        ContentResolver cr = context.getContentResolver();

        String[] columns = new String[] {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.TITLE,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.MIME_TYPE,
                MediaStore.Images.ImageColumns.SIZE };
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns, null, null, MediaStore.Images.ImageColumns.DATE_MODIFIED+" desc");
            if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {
                final String data = cursor.getString(dataColumn);
                images.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();

        Timber.d("Image list count "+images.size());
        Timber.d("Images list \n"+images.toString());
        return images;
    }

    @DebugLog
    private ArrayList<String> getFolderList(ArrayList<String> imageList){
        ArrayList<String> folderList = new ArrayList<String>();
        for(String image:imageList){
            if (!folderList.contains(getFolderName(image))){
                folderList.add(getFolderName(image));
            }
        }
        return folderList;
    }

    @DebugLog
    private ArrayList<IDrawerItem> getDrawerItems(ArrayList<String> folderList){
        ArrayList<IDrawerItem> drawerList = new ArrayList<IDrawerItem>();
        drawerList.add(new PrimaryDrawerItem().withName(ALL_IMAGES).withIcon(R.drawable.flippic_logo3));
        drawerList.add(new DividerDrawerItem());
        for(String image:folderList){
            if (image.equalsIgnoreCase(CAMERA)){
                drawerList.add(new PrimaryDrawerItem().withName(image).withIcon(R.drawable.camera));
            }else if(image.equalsIgnoreCase(DOWNLOAD)){
                drawerList.add(new PrimaryDrawerItem().withName(image).withIcon(R.drawable.download));
            }else if(image.equalsIgnoreCase(WHATSAPP)){
                drawerList.add(new PrimaryDrawerItem().withName(image).withIcon(R.drawable.whatsapp));
            }else if(image.equalsIgnoreCase(PINS)){
                drawerList.add(new PrimaryDrawerItem().withName(image).withIcon(R.drawable.pinterest));
            }
            else {
                drawerList.add(new PrimaryDrawerItem().withName(image).withIcon(R.drawable.picture));
            }
        }
        drawerList.add(new DividerDrawerItem());
        drawerList.add(new PrimaryDrawerItem().withName(COFFEE).withIcon(R.drawable.coffee));
        drawerList.add(new DividerDrawerItem());
        drawerList.add(new PrimaryDrawerItem().withName(SETTINGS).withIcon(R.drawable.settings));


        return drawerList;
    }

    @DebugLog
    private ArrayList<String> getImagesFromAFolder(ArrayList<String>allImages,String folderName){
        ArrayList<String> imagesList = new ArrayList<String>();
        for(String image:allImages){
            if(folderName.equalsIgnoreCase(getFolderName(image))){
                imagesList.add(image);
            }
        }
        return imagesList;
    }

    class ViewHolder{
        HorizontalScrollView image_container;
        ScaleImageView image;
        TextView name;
    }
}
