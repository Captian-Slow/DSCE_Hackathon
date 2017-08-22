package com.hackathon.dsce.amit.dosomething;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        holder.comments.setText(String.valueOf((task.getComments())));
        holder.upvotes.setText(String.valueOf(task.getUpvotes()));
        holder.workings.setText(String.valueOf(task.getWorkings()));
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
        private TextView upvotes, dwnVotes, workings, comments;
        private ImageButton upvoteBtn, dwnVoteBtn, workingBtn, commentsBtn;

        public ViewHolder(View view){
            super(view);
            upvoteBtn = (ImageButton) view.findViewById(R.id.upvote_count_button);
            dwnVoteBtn = (ImageButton) view.findViewById(R.id.dwmvote_count_button);
            workingBtn = (ImageButton) view.findViewById(R.id.working_count_button);
            commentsBtn = (ImageButton) view.findViewById(R.id.comment_count_button);
            upvotes = (TextView) view.findViewById(R.id.upvote_count);
            workings = (TextView) view.findViewById(R.id.working_count);
            comments = (TextView) view.findViewById(R.id.message_count);
            taskTitleTextView = (TextView) view.findViewById(R.id.taskTitleTextView);
            taskBodyTextView = (TextView) view.findViewById(R.id.taskBodyTextView);
            dateUploadedTextView = (TextView) view.findViewById(R.id.date_uploaded);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.i("ALERT !!", "View " + taskTitleTextView.getText() +" Clicked !!");
                    Intent intent = new Intent(context, TaskInfoActivity.class);
                    intent.putExtra("title", taskTitleTextView.getText());
                    intent.putExtra("body", taskBodyTextView.getText());
                    intent.putExtra("date", dateUploadedTextView.getText());
                    intent.putExtra("upvotes", upvotes.getText());
                    intent.putExtra("dwnVotes", dwnVotes.getText());
                    intent.putExtra("workings", workings.getText());
                    intent.putExtra("comments", comments.getText());
                    context.startActivity(intent);
                }
            });
        }
    }
}
