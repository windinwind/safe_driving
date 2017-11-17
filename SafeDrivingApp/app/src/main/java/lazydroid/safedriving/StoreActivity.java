package lazydroid.safedriving;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends ActionBarActivity {

    private static String shopURL = "http://35.182.114.230/shop";
    private List<ProductInfo> product_list = new ArrayList<ProductInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        new StoreNetworkService().execute("list");

        ListView list = (ListView) findViewById(R.id.product_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                //Toast.makeText(StoreActivity.this, "Clicked" + id, Toast.LENGTH_LONG).show();
                navigateToItem(id);
            }
        });

    }

    protected void navigateToItem(long id){

        Intent myIntent = new Intent(this, PurchaseItemActivity.class);
        myIntent.putExtra("image_num",Long.toString(id));
        myIntent.putExtra("cost", Integer.toString(product_list.get((int)id).getProductCost()));
        //Log.d("compute cost", Integer.toString(product_list.get(1).getProductCost()));
        startActivity(myIntent);
    }


    public void updateProduct(){
        ListView list = (ListView) findViewById(R.id.product_list);

        List<String> product_collection = new ArrayList();
        int index = 0;
        for(ProductInfo this_product : product_list){
            Log.d("processing", this_product.getProductName());
            //reformat for listview
            product_collection.add(this_product.getProductName() + "        " + this_product.getProductCost() + "sp");
        }

        //get adapter for list
        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, product_collection);

        //connect adapter to listview
        list.setAdapter(adapter);

        Log.d("product_list", Integer.toString(product_list.get(0).getProductCost()));
    }



    /*
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, EMPTY ,
                PROJECTION, SELECTION, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    */

    public class StoreNetworkService extends AsyncTask<String, Boolean, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            if(strings[0].equals("list")) {
                getProductList();
            }
            return null;
        }

        private void getProductList(){
            try {
                //open connection with store, get all product
                URL url = new URL(shopURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                product_list = new ArrayList<ProductInfo>();

                BufferedReader response = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));

                String inputLine;

                do {
                    String product_name;
                    int product_cost;
                    //get product name
                    inputLine = response.readLine();
                    if (inputLine == null || inputLine.isEmpty()) {
                        break;
                    }

                    String[] in = inputLine.split(":");
                    if (in[0].contains("product")) {
                        product_name = in[1];

                    } else {
                        Log.d("doesn't match", "break");
                        break;
                    }
                    //get product cost
                    inputLine = response.readLine();
                    in = inputLine.split(":");
                    if (in[0].contains("cost")) {
                        product_cost = Integer.parseInt(in[1]);
                    } else {
                        break;
                    }

                    Log.d("product name", product_name);
                    Log.d("product cost", in[1]);

                    product_list.add(new ProductInfo(product_name, product_cost));

                } while (!inputLine.equals(null) && !inputLine.equals(""));

                response.close();
                urlConnection.disconnect();

                //update product lists
                publishProgress();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Boolean... progress){
            updateProduct();
        }
    }

    }
