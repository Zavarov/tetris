/*
 * Copyright (C) 2019 Zavarov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tetris;

import tetris.tetromino.Tetromino;

import java.util.Arrays;
import java.util.Objects;

/**
 * This class implements the playing field of the tetris game.
 * @author Zavarov
 */
public class Field{
    /**
     * The underlying field where the first dimension is the number of rows,
     * the other one is the number of columns.
     */
    protected final Tetromino[][] field;
    /**
     * The number of columns.
     */
    protected final int columns;
    /**
     * The number of rows.
     */
    protected final int rows;
    /**
     * Creates a new field of the given size.
     * @param columns the number of columns.
     * @param rows the number of rows.
     */
    public Field(int columns, int rows){
        this.field = new Tetromino[rows][columns];
        this.columns = columns;
        this.rows = rows;
    }
    /**
     * Replaces the element at the specified position.
     * @param column the x coordinate.
     * @param row the y coorindate.
     * @param tetromino the new element. 
     * @return the input if the coordinates are inside the field, otherwise null.
     */
    public Tetromino put(int column, int row, Tetromino tetromino){
        if(isValid(column,row))
            return field[row][column] = tetromino;
        return null;
    }
    /**
     * Removes one element from the field.
     * @param column the x coordinate.
     * @param row the y coordinate.
     * @return the element at (x,y) that was removed.
     */
    public Tetromino remove(int column, int row){
        if(!isValid(column,row))
            return null;
        
        Tetromino removed = field[row][column];
        field[row][column] = null;
        return removed;
    }
    /**
     * Removes the row pushes all rows above it down by one.
     * @param row the row.
     * @return true if the field was changed as a result of this function.
     */
    public boolean remove(int row){
        if(!isValid(row))
            return false;
        
        clear(row);
        Tetromino[] removed = field[row];

        if (rows - 1 - row >= 0)
            System.arraycopy(field, row + 1, field, row, rows - 1 - row);
        field[rows - 1] = removed;
        return true;
    }
    /**
     * Removes all elements in the row.
     * @param row the row.
     */
    public void clear(int row){
        if(isValid(row))
            for(int x = 0; x < columns ; ++x)
                remove(x, row);
    }
    /**
     * Removes all elements on the field.
     */
    public void clear(){
        for(int y = 0 ; y < rows ; ++y)
            clear(y);
    }
    /**
     * @param row the row.
     * @return true if all entries in this row are filled.
     */
    public boolean isFull(int row){
        if(isValid(row))
            return Arrays.stream(field[row]).allMatch(Objects::nonNull);
        return false;
    }
    /**
     * @param column the x coordinate.
     * @param row the y coordinate.
     * @return the element at position (x,y) or null, if it is invalid.
     */
    public Tetromino get(int column, int row){
        if(isValid(column,row))
            return field[row][column];
        return null;
    }
    /**
     * @param row the row.
     * @return true if the row exists.
     */
    public boolean isValid(int row){
        return row >= 0 && row < rows;
    }
    /**
     * @param column the x coordinate.
     * @param row the y coordinate.
     * @return true if the (x,y) coordinate is inside the playing field.
     */
    public boolean isValid(int column, int row){
        return isValid(row) && column >= 0 && column < columns;
    }
    /**
     * Computes a simplied version of the current state of the field.<br>
     * Occupied spaces will be indicated by an X, while empty spaces are
     * represented by a dot.
     * @return a representation of the underlying playing field.
     */
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for(int y = rows - 1 ; y >= 0 ; --y){
            for(int x = 0 ; x < columns ; ++x)
                builder.append(get(x,y) == null ? "." : "X");
            builder.append("\n");
        }
        return builder.toString();
    }
}