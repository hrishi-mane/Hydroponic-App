package com.example.hydrophoicapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "firebase_data")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private  int id;

    private String mode;

    private int button_state;

    public Note(String mode, int button_state) {
        this.mode = mode;
        this.button_state = button_state;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getMode() {
        return mode;
    }

    public int getButton_state() {
        return button_state;
    }
}
