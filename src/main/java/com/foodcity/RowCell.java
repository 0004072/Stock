package com.foodcity;

/**
 * Created by hsenid on 10/21/16.
 */
public class RowCell {
    private int cellWidth;
    private String alignment;
    private String cellContent;
    private int colspan;
    private String type;

    public RowCell(int w, String a, String c){
        this.cellWidth = w;
        this.alignment = a;
        this.cellContent = c;
        this.colspan = 0;
        this.type = "text";
    }

    public RowCell(String a, String c){
        this.cellWidth = c.length();
        this.alignment = a;
        this.cellContent = c;
        this.colspan = 0;
        this.type = "text";
    }

    public RowCell(int w, String a, String c, String t){
        this.cellWidth = w;
        this.alignment = a;
        this.cellContent = c;
        this.colspan = 0;
        this.type = t;
    }

    public void setCellContent(String text){
        this.cellContent = text;
    }

    void setCellWidth(int newWidth){
        this.cellWidth = newWidth;
    }

    int getCellWidth(){
        return this.cellWidth;
    }

    String getAlignment(){
        return this.alignment;
    }

    String getCellContent(){
        return this.cellContent;
    }

    int getColspan(){
        return this.colspan;
    }

    int getAbsoluteColsSpan(){
        if(colspan < 0)
            return this.colspan * -1;

        return this.colspan;
    }

    String getType(){
        return this.type;
    }

    void setColspan(int c){
        this.colspan = c;
    }
}
