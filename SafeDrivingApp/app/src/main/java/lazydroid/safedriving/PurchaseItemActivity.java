package lazydroid.safedriving;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import java.io.*;

public class PurchaseItemActivity extends AppCompatActivity {


    private static String productURL = "http://35.182.114.230/product/";

    private int image_cost;
    private String image_number;

    private ImageView image;
    private Bitmap image_to_display;

    private String REMEMBER_PURCHASE_INFO = "purchaseInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_item);

        //get image to be purchased
        Intent myIntent = getIntent();
        String image_num = myIntent.getStringExtra("image_num");
        image_number = image_num;
        String cost = myIntent.getStringExtra("cost");
        image_cost = Integer.parseInt(cost);
        Log.d("cost", cost);

        TextView display_cost = (TextView) findViewById(R.id.cost_display);
        display_cost.setText("cost: " + cost);

        //get image
        image = (ImageView)findViewById(R.id.main_image);
        new PurchaseItemNetworkService().execute(Integer.toString(Integer.parseInt(image_num)+1));

        if(UserInfo.getUsername() != null) {
            updateDisplayUserInfo();
        }
    }


    private void updateDisplayUserInfo(){
        TextView username = (TextView) findViewById(R.id.after_login_username);
        username.setText(UserInfo.getUsername());
        TextView safepoint = (TextView) findViewById(R.id.after_login_point);
        safepoint.setText(Integer.toString(UserInfo.getSafepoint()));
    }


    public void saveButtonClicked(View v){
        if(!checkPurchase(image_number)){
            Toast.makeText(this, "please purchase first", Toast.LENGTH_LONG).show();
            return;
        }

        String path = saveImage();

        Toast.makeText(this, "saved to " + path, Toast.LENGTH_LONG).show();
    }


    private String saveImage(){
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), image_to_display,
                "SafeDriving Wall paper" ,"Thank you for using SafeDriving, wish you a safe trip <3");
        return path;
    }


    public void purchaseButtonClicked(View v){
        if(authenticate(image_cost)){
            //check if already purchased
            if(checkPurchase(image_number)){
                Toast.makeText(this, "already purchased", Toast.LENGTH_LONG).show();
                return;
            }

            //now can purchase
            if(image_to_display == null){
                Toast.makeText(this, "wall paper unavailable", Toast.LENGTH_LONG).show();
                return;
            }

            //save to gallary
            String path = saveImage();

            //deduct point
            UserInfo.setSafepoint(UserInfo.getSafepoint() - image_cost);

            //display success message
            Toast.makeText(this,"Purchase success",Toast.LENGTH_LONG).show();
            Toast.makeText(this,"Image saved to " + path, Toast.LENGTH_LONG).show();

            //update GUI
            updateDisplayUserInfo();
            StoreActivity.updateDisplayUserInfo();

            //memorize this purchase
            memorizePurchase(image_number);
        }
    }

    private void memorizePurchase(String purchased){
        SharedPreferences mSharedPreferences = this.getSharedPreferences(REMEMBER_PURCHASE_INFO, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(image_number,"purchased");
        mEditor.apply();
    }

    private boolean checkPurchase(String check_num){
        SharedPreferences mSharedPreferences = this.getSharedPreferences(REMEMBER_PURCHASE_INFO, MODE_PRIVATE);
        String check = mSharedPreferences.getString(check_num, "notpurchased");
        if(check == null || !check.equals("purchased")){
            return false;
        }

        return true;
    }

    private boolean authenticate(int cost){
        if(UserInfo.getUsername() == null || UserInfo.getUsername().isEmpty()){
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
            return false;
        }else if(UserInfo.getSafepoint() < cost){
            Toast.makeText(this, "Sorry, you don't have enough safepoints", Toast.LENGTH_LONG).show();
            return false;
        }

        //Toast.makeText(this,"login success",Toast.LENGTH_LONG).show();
        return true;
    }

    public class PurchaseItemNetworkService extends AsyncTask<String, Boolean, Void> {


        @Override
        protected Void doInBackground(String... strings) {
            String product_num = strings[0];
            URL url = null;
            try {
                Log.d("getting image with", product_num);
                url = new URL(productURL + product_num);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                InputStream input = urlConnection.getInputStream();
                Bitmap bmp = BitmapFactory.decodeStream(input);
                if(bmp != null){
                   image_to_display = bmp;
                   publishProgress();
                }


                //FileInputSream fis = new FileInputStream(file);
                //Bitmap bitmap = BitmapFactory.decodeStream(fis);
                //set image view
                //.setImageBitmap(bitmap);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Boolean... progress){
           image.setImageBitmap(image_to_display);
        }
    }
}
