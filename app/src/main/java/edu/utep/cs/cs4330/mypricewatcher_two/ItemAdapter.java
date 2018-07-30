package edu.utep.cs.cs4330.mypricewatcher_two;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item>{
    private List<Item> itemsList = new ArrayList<Item>();
    private Context iContext;

    public ItemAdapter(Context context,ArrayList<Item> list) {
        super(context, 0 , list);
        iContext = context;
        itemsList = list;
    }
    ItemAdapter(Context context, Item[] items){
        super(context, R.layout.custom_row, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(iContext).inflate(R.layout.custom_row,parent,false);
        }
        Item currentItem = itemsList.get(position);
        TextView itemText = listItem.findViewById(R.id.nameText);
        if(!(currentItem == null)){
            itemText.setText("Item: " + currentItem.getName() + "  Price: " + String.format("%.02f",currentItem.getCurrPrice())
            + "  Price Change: " + String.format("%.0f%%",currentItem.percentageChange()));
        }
        return listItem;
    }

}
