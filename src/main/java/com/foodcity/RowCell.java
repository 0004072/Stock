package com.foodcity;

/**
 * Created by hsenid on 10/21/16.
 */
public class RowCell {
    private int cellWidth;
    private String alignment;
    private String cellContent;
    private int colspan;

    public RowCell(int w, String a, String c){
        this.cellWidth = w;
        this.alignment = a;
        this.cellContent = String.format("%1$" + this.alignment + this.cellWidth + "s", c);
        this.colspan = 1;
    }

    public void setCellContent(String text, String al){
        this.cellContent = String.format("%1$" + this.alignment + this.cellWidth + "s", text);
    }

    void setCellWidth(int newWidth){
        this.cellWidth = newWidth;
        this.setCellContent(this.cellContent, this.getAlignment());
    }

    void setCellAlignment(String al){
        if(al.matches("[-]") || (al.length() == 0)) {
            this.alignment = al;
            this.cellContent.trim();
            this.setCellContent(this.cellContent, this.alignment);
        }

        else
            System.out.println("Invalid alignment!");
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

    void setColspan(int c){
        this.colspan = c;
    }

    int getColspan(){
        return this.colspan;
    }
}
