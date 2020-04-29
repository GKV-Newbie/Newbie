package com.gkv.newbie.app.home.sections.procedure.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gkv.newbie.R;
import com.gkv.newbie.model.Procedure;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProcedureAdapter extends ArrayAdapter<Procedure> {

    public ProcedureAdapter(@NonNull Context context, int resource, @NonNull List<Procedure> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Procedure procedure = getItem(position);

        LayoutInflater inf = LayoutInflater.from(getContext());
        LinearLayout ll = (LinearLayout) inf.inflate(R.layout.procedure_item, null);

        ((TextView)ll.findViewById(R.id.text)).setText(procedure.getFormattedName());

        ((ImageView)ll.findViewById(R.id.icon)).setImageResource(procedure.getProcedureType().equals("group")?R.drawable.icon_folder :R.drawable.procedure);

        return ll;
    }
}
