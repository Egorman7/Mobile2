package app.and.mobile2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
        // Загрузка данных
        loadData();
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Запись в БД поста и удаление из общего списка
                boolean liked = direction == ItemTouchHelper.RIGHT;
                DBHelper.addToList(getApplicationContext(), UserId, mAdapter.getList().get(viewHolder.getAdapterPosition()).getUrl(),liked);
                mAdapter.getList().remove(viewHolder.getAdapterPosition());
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Toast.makeText(getApplicationContext(), liked ? "Понравилось" : "Не понравилось", Toast.LENGTH_SHORT).show();
                // Если осталось мало постов, то подгрузка данных
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
                    if(response.body()==null){
                        Toast.makeText(getApplicationContext(), "Ошибка загрузки данных", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ArrayList<RedditDataModel.Data.Children> posts = response.body().getData().getChildren();
                    ArrayList<ListItemModel> data = new ArrayList<>();
                    for(int i=0; i<posts.size(); i++){
                        if(!posts.get(i).getData().isIs_video() && !DBHelper.isInList(getApplicationContext(), UserId, posts.get(i).getData().getThumbnail())){
                            data.add(new ListItemModel(posts.get(i).getData().getThumbnail(),false));
                        }
                        after = response.body().getData().getAfter();
                    }
                    LinearLayoutManager llm = new LinearLayoutManager(RedditActivity.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    // Проверка для подгрузки данных в список
                    if(mAdapter!=null && mAdapter.getList()!=null){
                        mAdapter.getList().addAll(data);
                    } else {
                        mAdapter = new RedditListAdapter(data, getApplicationContext(), UserId);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    // если загрузило мало, то снова подгружаем данные
                    if(mAdapter.getList().size()<=5) RedditApi.getInstance().getPuppiesModel(after,25).enqueue(this);
                    else {Toast.makeText(getApplicationContext(), "Данные загружены!", Toast.LENGTH_SHORT).show();}
                }

                @Override
                public void onFailure(Call<RedditDataModel> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Ошибка загрузки данных!", Toast.LENGTH_LONG).show();
                }
            };
            Toast.makeText(getApplicationContext(), "Загрузка данных...", Toast.LENGTH_LONG).show();
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
