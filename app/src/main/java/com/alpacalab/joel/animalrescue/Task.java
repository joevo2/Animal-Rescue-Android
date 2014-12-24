package com.alpacalab.joel.animalrescue;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Joel on 12/24/14.
 */

@ParseClassName("Task")
public class Task extends ParseObject{
    public Task() {

    }

    public boolean isCompleted() {
        return getBoolean("completed");
    }

    public void setCompleted(boolean completed) {
        put("completed",completed);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description",description);
    }
}
