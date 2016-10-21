package com.foodcity;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by hsenid on 10/21/16.
 */
public class Table {
    private RowCell[] header;
    private String cellDelimiter;
    private LinkedList<RowCell[]> content;
    private StringBuilder tableString;

    public Table(RowCell ... st){
        this.header = st;
        this.cellDelimiter = " | ";
        this.content = new LinkedList<>();
        this.content.add(this.header);
    }

    public int getNumberOfRows(){
        return this.content.size();
    }

    public String rowToString(int rowIndex){
        StringBuilder rowString = new StringBuilder();
        RowCell[] rowToPrint = this.content.get(rowIndex);
        for(int i = 0; i < rowToPrint.length; i++) {
            rowString.append(rowToPrint[i].getCellContent());
            if (i < (rowToPrint.length - 1)) {
                if(rowToPrint[i].getColspan() == -1){
                    for(int j = 0; j < this.cellDelimiter.length(); j++)
                        rowString.append(" ");
                }
                else{
                    rowString.append(this.cellDelimiter);
                }
            }
        }
        return rowString.toString();
    }

    public String tableToString(){
        this.tableString = new StringBuilder();
        StringBuilder hr = outerHr();
        StringBuilder hrInner = innerHr();
        String header = rowToString(0);

        for(int i = 1; i < header.length() - 1; i++){
            if((header.charAt(i - 1) == ' ') && (header.charAt(i) == '|') && (header.charAt(i + 1) == ' ')) {
                hrInner.deleteCharAt(i + 2);
                hrInner.insert(i + 2, '|');
            }
        }

        for(int i = 0; i < content.size(); i++){
            String row = rowToString(i);
            if(i == 0)
                tableString.append(hr).append("| ").append(row).append(" |\n").append(hrInner);

            else
                tableString.append("| ").append(row).append(" |\n");
        }
        tableString.append(hrInner);
        return tableString.toString();
    }

    public void addRow(String ... input){
        if(input.length != this.header.length){
            System.out.println("Table width and the current row's width differ in length. Check your input!");
            return;
        }

        RowCell[] row = new RowCell[input.length];
        int i = 0;
        for(RowCell rc : header){
            if(rc.getCellWidth() < input[i].length()) {
                for(RowCell[] rows : content){
                    rows[i].setCellWidth(input[i].length());
                }
            }

            String al = "-";
            if(input[i].matches("(-)?[\\d]+(\\.[\\d]+)?")){
                al = "";
            }

            RowCell obj = new RowCell(rc.getCellWidth(), al, input[i]);
            row[i] = obj;
            i++;
        }

        this.content.add(row);
    }

    private StringBuilder outerHr(){
        StringBuilder hr = new StringBuilder(" ");
        String header = rowToString(0);
        for(int j = 0; j < header.length() + 2; j++) {
            hr.append("_");
        }

        hr.append(" \n");
        return hr;
    }

    private StringBuilder innerHr(){
        StringBuilder hrInner = new StringBuilder("|");
        String header = rowToString(0);
        for(int j = 0; j < header.length() + 2; j++) {
            hrInner.append("_");
        }

        hrInner.append("|\n");

        return hrInner;
    }

    public void addInnerHr(){
        this.tableString.append(this.innerHr());
    }

    public void addOuterHr(){
        this.tableString.append(this.outerHr());
    }

    public void mergeCells(int rowId, int cellId, int span){
        RowCell[] currentRow = content.get(rowId);
        if(currentRow == null){
            System.out.println("Row not found!");
            return;
        }

        if((cellId + span) > currentRow.length || (cellId +span) < 0){
            System.out.println("Unable to merge. Merger span exceeds the table width.");
            return;
        }

        if(span >= 0) {
            for (int i = cellId - 1; i <= cellId + span; i++) {
                RowCell blank = new RowCell(currentRow[i].getCellWidth(), "", "");
                blank.setColspan(-1);
                currentRow[i] = blank;
            }
        }

        else{
            for (int i = cellId - 1; i >= cellId + span; i--) {
                RowCell blank = new RowCell(currentRow[i].getCellWidth(), "", "");
                blank.setColspan(-1);
                currentRow[i] = blank;
            }
        }
    }
}
