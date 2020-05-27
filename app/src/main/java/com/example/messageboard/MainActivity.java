package com.example.messageboard;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";//測試用的LogT;
    EditText EtSystemIn, EtTitleIn;
    Button BtAddArticle;
    MyAdapter Adapter;
    ImageButton ImgBtEdit;


    ArrayList<HashMap<String,String>> List = new ArrayList<>();
    //對應資料庫我們適用ArrayList<HashMap<String,String>>型態
    RecyclerView Rc;
    articleSQL ArticleSQL = new articleSQL(this);

    //使用 ArticleSQL
//    int[] ItemCount=new int[2];???????????????????????????????????????????????????????????????????????????????????
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EtSystemIn = findViewById(R.id.editText);
        EtTitleIn = findViewById(R.id.ETtitle);
        BtAddArticle = findViewById(R.id.button12);
        Rc = findViewById(R.id.rc);
        List = ArticleSQL.getDATA();

        BtAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!EtSystemIn.getText().toString().equals("")&&!EtTitleIn.getText().toString().equals("")){
                    Adapter.addItem(EtTitleIn.getText().toString(), EtSystemIn.getText().toString());
                    //使用addItem這個方法＋並把輸入的值轉成字串帶入
                    ArticleSQL.insertSETDATE(EtTitleIn.getText().toString(), EtSystemIn.getText().toString());
                    //使用ArticleSQL這個class裡面的自訂的insert方法轉成字串給他，使資料庫可以儲存起來
                    EtTitleIn.setText(null);
                    EtSystemIn.setText(null);
                }
                else{
                    Toast toast = Toast.makeText(MainActivity.this,"此處不能為空白",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER|Gravity.TOP,0,0);
                    toast.show();
                }
            }
        });
        Rc.setLayoutManager(new LinearLayoutManager(this));
        Adapter = new MyAdapter(List);
        Rc.setAdapter(Adapter);

    }




    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        public ArrayList<HashMap<String,String>> MapList;
        //這裡的List跟上面的list不一樣這裡是選告一個這個形態的東西

        public MyAdapter(ArrayList<HashMap<String,String>> list) {
            this.MapList=list;
        }//Article裡面的getDate

        public void addItem(String title,String str){

            HashMap<String,String> map = new HashMap<>();
            //在宣告一個map 來put 進去
            map.put("Title",title);//注意 這裡要跟資料庫的insert 名稱一樣
            map.put("Article",str);
            MapList.add(map);//把這邊的map 傳給MapList
            notifyDataSetChanged();
            //需要告訴list你需要更新你的畫面//有點類似重整


        }

        public void removeItem(int position){
            ArticleSQL.DELETEDATE(MapList.get(MapList.size()-position-1).get("Title"));//資料庫刪除
            MapList.remove(MapList.size()-position-1);//移除MapList
            notifyItemRemoved(position);//刪掉拖的畫面更新 有點類似重整
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.article,viewGroup,false));
        }

        @Override
        public void onBindViewHolder( ViewHolder viewHolder, int i) {
            viewHolder.TxItemSetArticle.setText(MapList.get(MapList.size()-i-1).get("Article"));//取KEY值HasMap的用法
            viewHolder.TxItemSetTitle.setText(MapList.get(MapList.size()-i-1).get("Title"));
        }

        @Override
        public int getItemCount() {//recyclerview要顯示幾筆資料

            return  MapList.size();//要告訴總共有幾個長度
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            Button BtItemDelete,BtItemEdit;
            TextView TxItemSetArticle;
            TextView TxItemShowTest;
            TextView TxItemSetTitle;
            TextView EtDialogTitle;

            TextView EtDialogArticle;


            ViewHolder(final View itemView) {
                super(itemView);
                BtItemDelete =itemView.findViewById(R.id.delete);
                BtItemEdit = itemView.findViewById(R.id.itemedit);
                TxItemSetArticle =itemView.findViewById(R.id.articletx);
                TxItemSetTitle = itemView.findViewById(R.id.title1);
                TxItemShowTest = findViewById(R.id.showtx);

                BtItemDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItem(getAdapterPosition());
                        //getAdapterPosition()跟notifyItemRemoved最大的差別就是這個不用重新整理，這個就是簡單刪除item刪除的position
                        Log.d(TAG, "onClick: "+getAdapterPosition());
//                        ItemCount[1]++;?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//                        ArrayList<HashMap<String,String>> SQLlist = List;
////                        if (SQLlist.size()!=0){
////                            ItemTxShowTest.setText(SQLlist.toString()+"\n");
////                        }
                    }
                });


                BtItemEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                     先有資料庫 找到 非0的資料 並要 ？？是寫在這嗎？？  TxDialogTitle.setText(MapList.get(MapList.size()-(ItemtemCount[0]-ICount[1])-1).get("Article"));??????????????????????????????????????????????????????????????????????????
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.editdialog);
                        EtDialogTitle = dialog.findViewById(R.id.EtDialogTitle);
                        EtDialogArticle = dialog.findViewById(R.id.EtDialogArticle);
                        Button BtDialogOK = dialog.findViewById(R.id.BtDialogOK);
                        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                        dialog.getWindow().setAttributes(lp);
                        EtDialogTitle.setText(MapList.get(MapList.size()-getAdapterPosition()-1).get("Title"));//getAdapterPosition()你地幾個item的位置
                        EtDialogArticle.setText(MapList.get(MapList.size()-getAdapterPosition()-1).get("Article"));

                        BtDialogOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                ImgBtEdit = itemView.findViewById(R.id.ImgBtEdit);
                ImgBtEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PopupMenu popup = new PopupMenu(ImgBtEdit.getContext(),v);
                        popup.getMenuInflater().inflate(R.menu.editmenu,popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case R.id.MenuEdit:
                                        Toast.makeText(MainActivity.this,"修改",
                                                Toast.LENGTH_SHORT).show();
                                        break;
                                    case R.id.MenuDelete:
                                        Toast.makeText(MainActivity.this,"刪除",
                                                Toast.LENGTH_SHORT).show();
                                        break;
                                }
                                return true;
                            }

                        });
                        popup.show();
                    }
                });

            }

        }
    }

}


