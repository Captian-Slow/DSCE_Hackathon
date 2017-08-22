package com.hackathon.dsce.amit.dosomething;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Amit on 10-08-2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{

    private Context context;
    private List<Task> tasks;

    public TaskAdapter(List<Task> tasks, Context context){
        super();
        this.context = context;
        this.tasks = tasks;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Task task = tasks.get(position);
        String notificationMessageHint = task.getBody();
        if (task.getBody().length() > 99){
            notificationMessageHint = task.getBody().substring(0, 100) + "....";
        }

        holder.taskTitleTextView.setText(task.getTitle());
        holder.taskBodyTextView.setText(notificationMessageHint);
        holder.body = task.getBody();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView taskTitleTextView;
        private TextView taskBodyTextView;
        private TextView dateUploadedTextView;
        private String hasAttachment, attachmentName, attachmentType, body;

        public ViewHolder(View view){
            super(view);
            taskTitleTextView = (TextView) view.findViewById(R.id.taskTitleTextView);
            taskBodyTextView = (TextView) view.findViewById(R.id.taskBodyTextView);
            dateUploadedTextView = (TextView) view.findViewById(R.id.date_uploaded);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.i("ALERT !!", "View " + taskTitleTextView.getText() +" Clicked !!");
                    Intent intent = new Intent(context, TaskInfoActivity.class);
                    intent.putExtra("hasAttachment", hasAttachment);
                    intent.putExtra("attachmentName", attachmentName);
                    intent.putExtra("attachmentType", attachmentType);
                    intent.putExtra("dateUploaded", dateUploadedTextView.getText());
                    intent.putExtra("messageTitle", taskTitleTextView.getText());
                    intent.putExtra("message", body);
                    context.startActivity(intent);
                }
            });
        }
    }
}
