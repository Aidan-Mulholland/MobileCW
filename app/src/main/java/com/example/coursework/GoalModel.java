package com.example.coursework;

public class GoalModel {

    private int id, progress, parent;
    private String name, description, type;

    public GoalModel(int id, String name, String description, int progress, String type, int parent) {
        this.id = id;
        this.name = name;
        this.progress = progress;
        this.description = description;
        this.type = type;
        this.parent = parent;
    }

    public GoalModel(int id, String name, String description, int progress, String type) {
        this.id = id;
        this.name = name;
        this.progress = progress;
        this.description = description;
        this.type = type;
    }

    @Override
    public String toString() {
        return "GoalModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", progress=" + progress +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", parent='" + parent + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParent() { return parent; }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
