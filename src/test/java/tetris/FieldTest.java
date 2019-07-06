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

import org.junit.Before;
import org.junit.Test;
import tetris.tetromino.OBlock;
import tetris.tetromino.Tetromino;

import static org.junit.Assert.*;

/**
 *
 * @author Zavarov
 */
public class FieldTest {
    Game field;
    Tetromino tetromino;
    @Before
    public void setUp(){
        field = new Game(5,7,(i,j) -> {});
        tetromino = OBlock.create(field, 2, 2);
        
        field.field[2][2] = tetromino;
        field.field[4][2] = tetromino;
        
        field.field[3][0] = tetromino;
        field.field[3][1] = tetromino;
        field.field[3][2] = tetromino;
        field.field[3][3] = tetromino;
        field.field[3][4] = tetromino;
    }
    @Test
    public void putTest(){
        assertEquals(field.put(0, 0, tetromino),tetromino);
        
        assertEquals(field.field[0][0], tetromino);
    }
    
    @Test
    public void putInvalidTest(){
        assertNull(field.put(-1, 0, tetromino));
    }
    @Test
    public void removeTest(){
        assertEquals(field.remove(2, 3), tetromino);
        
        assertNull(field.field[3][2]);
    }
    @Test
    public void removeInvalidTest(){
        assertNull(field.remove(-1,3));
    }
    @Test
    public void removeRowTest(){
        assertTrue(field.remove(3));
        
        assertNull(field.field[3][0]);
        assertNull(field.field[3][1]);
        assertEquals(field.field[3][2], tetromino);
        assertNull(field.field[3][3]);
        assertNull(field.field[3][4]);
        
        assertEquals(field.field[2][2], tetromino);
        assertNull(field.field[4][2]);
    }
    @Test
    public void removeInvalidRowTest(){
        assertFalse(field.remove(-1));
    }
    @Test
    public void clearTest(){
        field.clear();
        
        for(int y = 0 ; y < field.field.length ; ++y)
            for(int x = 0 ; x < field.field[y].length ; ++x)
                assertNull(field.field[y][x]);
    }
    @Test
    public void clearRowTest(){
        field.clear(3);
        
        assertNull(field.field[3][0]);
        assertNull(field.field[3][1]);
        assertNull(field.field[3][2]);
        assertNull(field.field[3][3]);
        assertNull(field.field[3][4]);
        
        assertEquals(field.field[2][2], tetromino);
        assertEquals(field.field[4][2], tetromino);
    }
    @Test
    public void clearInvalidRowTest(){
        field.clear(-1);
        
        assertEquals(field.field[3][0], tetromino);
        assertEquals(field.field[3][1], tetromino);
        assertEquals(field.field[3][2], tetromino);
        assertEquals(field.field[3][3], tetromino);
        assertEquals(field.field[3][4], tetromino);
        
        assertEquals(field.field[2][2], tetromino);
        assertEquals(field.field[4][2], tetromino);
    }
    @Test
    public void isFullTest(){
        assertFalse(field.isFull(2));
        assertTrue(field.isFull(3));
        assertFalse(field.isFull(4));
        assertFalse(field.isFull(-1));
    }
    @Test
    public void getTest(){
        assertEquals(field.get(2, 3), tetromino);
        assertNull(field.get(-1, 3));
    }
    @Test
    public void isValidRowTest(){
        assertTrue(field.isValid(2));
        assertFalse(field.isValid(-1));
        assertFalse(field.isValid(7));
    }
    @Test
    public void isValidTest(){
        assertTrue(field.isValid(2,2));
        assertFalse(field.isValid(2,-1));
        assertFalse(field.isValid(2,7));
        assertFalse(field.isValid(7,2));
        assertFalse(field.isValid(-1, 2));
    }
    
    @Test
    public void toStringTest(){
        assertEquals(field.toString(),".....\n.....\n..X..\nXXXXX\n..X..\n.....\n.....\n");
    }
}
