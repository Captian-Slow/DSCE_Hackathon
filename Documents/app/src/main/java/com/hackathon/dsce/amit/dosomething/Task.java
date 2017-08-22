package com.hackathon.dsce.amit.dosomething;

/**
 * Created by Amit on 22-08-2017.
 */

public class Task {

    private String title;
    private String body;
    private int upvotes;
    private int comments;

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getWorkings() {
        return workings;
    }

    public void setWorkings(int workings) {
        this.workings = workings;
    }

    private int workings;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }
}
