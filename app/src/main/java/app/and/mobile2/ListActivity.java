package app.and.mobile2;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import app.and.mobile2.adapters.ListAdapter;
import app.and.mobile2.database.DBHelper;

public class ListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private boolean liked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Инициализация списка и установка адаптера
        mRecyclerView = findViewById(R.id.list_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        loadData();
    }

    private void loadData(){
        // Установка адаптера по курсору из БД, который получает список постов для пользователя по ид
        mRecyclerView.setAdapter(new ListAdapter(this, DBHelper.getListCursor(this,getIntent().getIntExtra("user_id",-1), liked)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.list_menu_item_1){
            Drawable d = item.getIcon();
            if(d instanceof Animatable){
                ((Animatable)d).start();
            }
            liked=!liked;
            loadData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
