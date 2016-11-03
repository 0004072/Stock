package com.foodcity;

import java.util.*;

/**
 * Created by hsenid on 10/21/16.
 */
public class Table {
    private RowCell[] header;
    private String cellDelimiter, lineDelimiter;
    private LinkedList<RowCell[]> content;
    private StringBuilder tableString;

    public Table(RowCell ... st){
        this.header = st;
        this.cellDelimiter = " | ";
        this.lineDelimiter = "-+-";
        this.content = new LinkedList<>();
        this.content.add(this.header);
        this.addSectionBreak(0);
    }

    public int getNumberOfRows(){
        return this.content.size();
    }

    public String rowToString(int rowIndex){
        StringBuilder rowString = new StringBuilder();
        RowCell[] rowToPrint = this.content.get(rowIndex);
        boolean isLine = false;
        for(int i = 0; i < rowToPrint.length; i++){
            if(!rowToPrint[i].getCellContent().matches("^@null@$")){
                int currentColSpan = rowToPrint[i].getColspan();
                int currentAbsColSpan = rowToPrint[i].getAbsoluteColsSpan();
                if(currentColSpan > 0){
                    int widthToSet = 0;
                    for(int j = i; j <= i + currentColSpan; j++){
                        widthToSet += content.get(0)[j].getCellWidth() + currentAbsColSpan;
                    }
                    String cellValue = rowToPrint[i].getCellContent();
                    String type = rowToPrint[i].getType();
                    if(type.equals("line")) {
                        cellValue = String.format("%1$" + rowToPrint[i].getAlignment() + this.content.get(0)[i].getCellWidth() + "s", "").replace(' ', '-');
                        isLine = true;
                    }
                    rowString.append(String.format("%1$" + rowToPrint[i].getAlignment() + widthToSet + "s", cellValue));
                }
                else if(currentColSpan < 0){
                    int widthToSet = 0;
                    for(int j = i; j >= i + currentColSpan; j--){
                        widthToSet += content.get(0)[j].getCellWidth();
                    }
                    widthToSet += (currentAbsColSpan * 3);
                    String cellValue = rowToPrint[i].getCellContent();
                    String type = rowToPrint[i].getType();
                    if(type.equals("line")) {
                        cellValue = String.format("%1$" + rowToPrint[i].getAlignment() + this.content.get(0)[i].getCellWidth() + "s", "").replace(' ', '-');
                        isLine = true;
                    }
                    rowString.append(String.format("%1$" + rowToPrint[i].getAlignment() + widthToSet + "s", cellValue));
                }
                else {
                    String cellValue = rowToPrint[i].getCellContent();
                    String type = rowToPrint[i].getType();
                    if(type.equals("line")) {
                        cellValue = String.format("%1$" + rowToPrint[i].getAlignment() + this.content.get(0)[i].getCellWidth() + "s", "").replace(' ', '-');
                        isLine = true;
                    }
                    rowString.append(String.format("%1$" + rowToPrint[i].getAlignment() + content.get(0)[i].getCellWidth() + "s", cellValue));
                }
            }
            if(i < rowToPrint.length - 1){
                if(!rowToPrint[i].getCellContent().matches("^@null@$")){
                    if(rowToPrint[i].getType().equals("line")) {
                        if(rowToPrint[i].getCellContent().matches("^@nodelimit@$")){
                            rowString.append("---");
                        }
                        else{
                            rowString.append(this.lineDelimiter);
                        }
                    }
                    else
                        rowString.append(this.cellDelimiter);
                }
            }
        }
        if(isLine) {
            rowString.insert(0, "+-");
            rowString.append("-+");
        }
        else{
            rowString.insert(0, "| ");
            rowString.append(" |");
        }
        return rowString.toString();
    }

    public String tableToString(){
        this.tableString = new StringBuilder();
        for(int i = 0; i < content.size(); i++){
            String row = rowToString(i);
            tableString.append(row).append("\n");
        }
        return tableString.toString();
    }

    public void addRow(String ... input){
        if(input.length != this.header.length){
            System.out.println("Table width and the current row's width differ in length. Check your input!");
            return;
        }

        RowCell[] row = new RowCell[input.length];

        for(int i = 0; i < input.length; i++){
            //Resolving the cell alignment
            String al;
            if(input[i].matches("^@align.*")){
                al = "";
                if(input[i].matches("^@align:left.*"))
                    al = "-";

                input[i] = input[i].replaceAll("^@align:(left|right)@", "");
            }
            else{
                al = "-";
                if(input[i].matches("(-)?[\\d]+(\\.[\\d]+)?")){
                    al = "";
                }
            }
            //End of resolving the alignment

            if(input[i].length() > content.get(0)[i].getCellWidth()){
                content.get(0)[i].setCellWidth(input[i].length());
            }

            RowCell newCell = new RowCell(al, input[i]);
            row[i] = newCell;
        }

        this.content.add(row);
    }

    public void addSectionBreak(int position){
        if(position < 0){
            System.out.println("Invalid position!");
            return;
        }
        RowCell[] lastRow = this.content.get(position);
        RowCell[] hr = new RowCell[lastRow.length];
        int i = 0;
        for(RowCell rc : lastRow){
            String content = "";
            if(rc.getCellContent().equals("@null@"))
                content = "@nodelimit@";
            RowCell cell = new RowCell(rc.getCellWidth(), "", content, "line");
            hr[i] = cell;
            i++;
        }
        if(position > 0)
            position++;
        this.content.add(position, hr);
    }

    public void addSectionBreak(){
        int position = this.content.size() - 1;
        if(position < 0){
            System.out.println("Invalid position!");
            return;
        }
        RowCell[] lastRow = this.content.get(position);
        RowCell[] hr = new RowCell[lastRow.length];
        for(int i = 0; i < lastRow.length; i++){
            RowCell rc = lastRow[i];
            String content = "";
            if(rc.getCellContent().equals("@null@"))
                content = "@nodelimit@";
            RowCell cell = new RowCell(rc.getCellWidth(), "", content, "line");
            hr[i] = cell;
        }
        if(position > 0)
            position++;
        this.content.add(position, hr);
    }

    public void mergeCells(int rowId, int cellId, int span){
        RowCell[] arr = content.get(rowId);
        ArrayList<RowCell> currentRow = new ArrayList<>(Arrays.asList(arr));
        content.remove(rowId);

        if(currentRow == null){
            System.out.println("Row not found!");
            return;
        }

        if((cellId + span) > currentRow.size() || (cellId +span) < 0){
            System.out.println("Unable to merge. Merger span exceeds the table width.");
            return;
        }

        RowCell mergedCell = currentRow.get(cellId);
        mergedCell.setColspan(span);

        if(span >= 0) {
            for (int i = cellId + 1; i <= cellId + span; i++) {
                int newWidth = currentRow.get(i).getCellWidth() + 3;
                mergedCell.setCellWidth(mergedCell.getCellWidth() + newWidth);
                currentRow.remove(i);
                currentRow.add(i, new RowCell(content.get(0)[i].getCellWidth(), "", "@null@"));
            }
        }

        else{
            for (int i = cellId - 1; i >= cellId + span; i--) {
                int newWidth = currentRow.get(i).getCellWidth() + 3;
                mergedCell.setCellWidth(mergedCell.getCellWidth() + newWidth);
                currentRow.remove(i);
                currentRow.add(i, new RowCell(content.get(0)[i].getCellWidth(), "", "@null@"));
            }
        }

        content.add(rowId, currentRow.toArray(new RowCell[currentRow.size()]));
    }

    public void showContent(){
        for(RowCell[] arr : this.content){
            for(RowCell rc : arr){
                System.out.print(rc.getCellContent() + " ");
            }
            System.out.println();
        }
    }
}
