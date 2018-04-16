package app.and.mobile2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import app.and.mobile2.adapters.RedditListAdapter;
import app.and.mobile2.api.RedditApi;
import app.and.mobile2.database.DBHelper;
import app.and.mobile2.models.ListItemModel;
import app.and.mobile2.models.RedditDataModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedditActivity extends AppCompatActivity {

    private TextView mTitle, mLogin;
    private RecyclerView mRecyclerView;
    private RedditListAdapter mAdapter;
    private String after = "";

    private int UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reddit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeView();
        initializeData();

    }

    private void initializeView(){
        mTitle = findViewById(R.id.reddit_text_title);
        mLogin = findViewById(R.id.reddit_text_login);
        mRecyclerView = findViewById(R.id.reddit_recycler_view);
    }

    private void initializeData(){
        mLogin.setText(getIntent().getStringExtra("username"));
        mTitle.setText("Puppy Lover");
        UserId = getIntent().getIntExtra("user_id",-1);
        loadData();
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction){
                    case ItemTouchHelper.LEFT:
                        Log.d("SWIPE", "Swipe Left!");
                        DBHelper.addToList(getApplicationContext(), UserId, mAdapter.getList().get(viewHolder.getAdapterPosition()).getUrl(),false);
                        mAdapter.getList().remove(viewHolder.getAdapterPosition());
                        mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        break;
                    case ItemTouchHelper.RIGHT:
                        Log.d("SWIPE", "Swipe Right!");
                        DBHelper.addToList(getApplicationContext(), UserId, mAdapter.getList().get(viewHolder.getAdapterPosition()).getUrl(),true);
                        mAdapter.getList().remove(viewHolder.getAdapterPosition());
                        mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        break;
                }
                if(mAdapter.getList().size()<=5) loadData();
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
    }

    private void loadData(){
        try {
            Callback<RedditDataModel> callback = new Callback<RedditDataModel>() {
                @Override
                public void onResponse(Call<RedditDataModel> call, Response<RedditDataModel> response) {
                    ArrayList<RedditDataModel.Data.Children> posts = response.body().getData().getChildren();
                    ArrayList<ListItemModel> data = new ArrayList<>();
                    for(int i=0; i<posts.size(); i++){
                        if(!posts.get(i).getData().isIs_video() && !DBHelper.isInList(getApplicationContext(), UserId, posts.get(i).getData().getThumbnail())){
                            data.add(new ListItemModel(posts.get(i).getData().getThumbnail(),false));
                            Log.d("DATA_LIST", data.get(data.size()-1).getUrl());
                        }
                        after = response.body().getData().getAfter();
                    }
                    LinearLayoutManager llm = new LinearLayoutManager(RedditActivity.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    if(mAdapter!=null && mAdapter.getList()!=null){
                        mAdapter.getList().addAll(data);
                       // mAdapter.notify();
                    } else {
                        mAdapter = new RedditListAdapter(data, getApplicationContext(), UserId);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    if(mAdapter.getList().size()<=5) RedditApi.getInstance().getPuppiesModel(after,25).enqueue(this);
                    //after = mAdapter.getList().get(mAdapter.getList().size()-1)
                    //Toast.makeText(getApplicationContext(), "Data loaded!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<RedditDataModel> call, Throwable t) {
                    Log.e("DATA_LOAD", t.getMessage());
                    //Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
            };
            RedditApi.getInstance().getPuppiesModel(after,25).enqueue(callback);
        } catch (Exception ex) {ex.printStackTrace();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reddit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.reddit_menu_item_1){
            Intent intent = new Intent(RedditActivity.this, ListActivity.class);
            intent.putExtra("user_id",UserId);
            startActivity(intent);
        }
        return true;
    }
}
