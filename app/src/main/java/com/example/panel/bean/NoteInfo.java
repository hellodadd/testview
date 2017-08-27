package com.example.panel.bean;

/**
 * Created by zwb on 2017/8/28.
 */

public class NoteInfo {
    private String note;
    private String note_id;
    private String sketch_id;
    private String start_x;
    private String start_y;

    public String getNote()
    {
        return this.note;
    }

    public String getNoteId()
    {
        return this.note_id;
    }

    public String getSketchId()
    {
        return this.sketch_id;
    }

    public String getStartX()
    {
        return this.start_x;
    }

    public String getStartY()
    {
        return this.start_y;
    }

    public void setNote(String paramString)
    {
        this.note = paramString;
    }

    public void setNoteId(String paramString)
    {
        this.note_id = paramString;
    }

    public void setSketchId(String paramString)
    {
        this.sketch_id = paramString;
    }

    public void setStartX(String paramString)
    {
        this.start_x = paramString;
    }

    public void setStartY(String paramString)
    {
        this.start_y = paramString;
    }
}
