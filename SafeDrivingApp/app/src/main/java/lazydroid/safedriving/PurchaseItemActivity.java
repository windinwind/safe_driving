package lazydroid.safedriving;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_item);

        //get image to be purchased
        Intent myIntent = getIntent();
        String image_num = myIntent.getStringExtra("image_num");
        String cost = myIntent.getStringExtra("cost");
        image_cost = Integer.parseInt(cost);
        Log.d("cost", cost);

        //get image
        image = (ImageView)findViewById(R.id.main_image);
        new PurchaseItemNetworkService().execute(Integer.toString(Integer.parseInt(image_num)+1));

    }

    public void purchaseButtonClicked(View v){
        if(authenticate(image_cost)){
            //now can purchase
        }
    }

    private boolean authenticate(int cost){
        if(UserInfo.getUsername() == null || UserInfo.getUsername().isEmpty()){
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
            return false;
        }else if(UserInfo.getSafepoint() < cost){
            Toast.makeText(this, "Sorry, you don't have enough safepoints", Toast.LENGTH_LONG).show();
            return false;
        }

        Toast.makeText(this,"login success",Toast.LENGTH_LONG).show();
        return true;
    }

    public class PurchaseItemNetworkService extends AsyncTask<String, Boolean, Void> {

        private Bitmap image_to_display;
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
