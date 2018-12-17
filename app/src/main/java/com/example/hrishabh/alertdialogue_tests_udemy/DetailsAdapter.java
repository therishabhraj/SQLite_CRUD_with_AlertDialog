package com.example.hrishabh.alertdialogue_tests_udemy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {

    private List<Details> details;
    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog alert;

    public DetailsAdapter(List<Details> details, Context context) {
        this.details = details;
        this.context = context;
    }
    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerlayout, null);
        DetailsViewHolder holder = new DetailsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder detailsViewHolder, final int i) {
        Details detail = details.get(i);
        detailsViewHolder.Dname.setText(detail.getName());
        detailsViewHolder.Demail.setText(detail.getEmail());

        detailsViewHolder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.info(context, "It's data at Position : " + Integer.toString(i), Toast.LENGTH_SHORT).show();
//                details.remove(i);
//                notifyDataSetChanged();
                deleteDialog(i);
            }
        });

        detailsViewHolder.updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog(i);
            }
        });

    }

    // TODO WOrk on Updation with email validation
    private void updateDialog(final int index) {

        final Details d = details.get(index);

        builder = new AlertDialog.Builder(context);
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup_layout, null);
        TextView heading = view.findViewById(R.id.headline);
        final EditText n = view.findViewById(R.id.inputsF);
        final EditText e = view.findViewById(R.id.inputsS);
        Button s = view.findViewById(R.id.saveBtn);

        heading.setText("Update");
        n.setText(d.getName());
        e.setText(d.getEmail());

        builder.setView(view);
        alert = builder.create();
        alert.show();

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Details d = details.get(index);
                DatabaseHelper myDBhelper = new DatabaseHelper(context);
                if (myDBhelper.isEmailNotExists(e.getText().toString())){


                    int isUpdated = myDBhelper.updateDetailDB(n.getText().toString(), e.getText().toString());
                    if (isUpdated == 1){
                        d.setName(n.getText().toString());
                        d.setEmail(e.getText().toString());
                        Toasty.success(context, "Update Successful", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                        alert.dismiss();
                    }else{
                        Toasty.error(context, "Update Unsuccessful", Toast.LENGTH_SHORT).show();
                    }
//                    Toasty.success(context, "Email not found", Toast.LENGTH_SHORT).show();
                }else {
                    e.setError("Make sure not used before");
                }


            }
        });
    }

    private void deleteDialog(final int index) {
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure want to delete this data ?");

        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
            }
        });
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Details dData = details.get(index);
                Toasty.success(context, dData.getEmail(), Toast.LENGTH_SHORT).show();
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                int isDeleted = dbHelper.deleteDetailDB(dData.getEmail());
                if (isDeleted > 0){
                    details.remove(index);
                    notifyDataSetChanged();
                    Toasty.success(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toasty.error(context, "Can't Delete !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert = builder.create();
        alert.show();
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class DetailsViewHolder extends RecyclerView.ViewHolder{
        TextView Dname;
        TextView Demail;
        RelativeLayout relativeLayout;
        Button updatebtn, deletebtn;
        public DetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            Dname = itemView.findViewById(R.id.editTxtName);
            Demail = itemView.findViewById(R.id.editTxtEmail);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            updatebtn = itemView.findViewById(R.id.btnUpdate);
            deletebtn = itemView.findViewById(R.id.btnDelete);
        }
    }
}
