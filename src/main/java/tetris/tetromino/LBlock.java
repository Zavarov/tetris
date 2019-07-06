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
package tetris.tetromino;

import tetris.Field;

import java.awt.*;

import static tetris.tetromino.Tetromino.Rotation.DEG270;
import static tetris.tetromino.Tetromino.Rotation.DEG90;

/**
 *
 * @author Zavarov
 */
public class LBlock extends Tetromino{
    /**
     * @param field the playing field.
     * @param xC the x coordinate of the center piece
     * @param yC the y coordinate of the center piece
     * @param color the color for a single block.
     */
    private LBlock(Field field, int xC, int yC, Color color){
        super(field, xC, yC, DEG270, DEG90, color);
    }
    
    public static LBlock create(Field field, int x, int y){
        LBlock tetromino = new LBlock(field, x, y, Color.MAGENTA);
        tetromino.add( 0, 0);
        tetromino.add( 0, 1);
        tetromino.add( 0,-1);
        tetromino.add( 1,-1);
        return tetromino;
    }
}