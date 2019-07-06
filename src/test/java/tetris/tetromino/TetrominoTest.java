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

import org.junit.Before;
import org.junit.Test;
import tetris.Game;

import static org.junit.Assert.*;

/**
 *
 * @author Zavarov
 */
public class TetrominoTest {
    Game field;
    Tetromino tetromino;
    @Before
    public void setUp(){
        field = new Game(4,4,(i,j) -> {});
        tetromino = OBlock.create(field, 1, 0);
    }
    @Test
    public void fallDownTest(){
        field.clear();
        tetromino = OBlock.create(field, 1, 2);
        tetromino.toField();
        
        assertTrue(tetromino.fallDown());

        assertNull(field.get(1, 2));
        assertNull(field.get(1, 3));
        assertNull(field.get(2, 2));
        assertNull(field.get(2, 3));
        
        assertEquals(field.get(1, 0), tetromino);
        assertEquals(field.get(1, 1), tetromino);
        assertEquals(field.get(2, 0), tetromino);
        assertEquals(field.get(2, 1), tetromino);
        
        assertFalse(tetromino.fallDown());
    }
    @Test
    public void addTest(){
        assertTrue(tetromino.add(5, 5));
        assertTrue(tetromino.blocks.contains(tetromino.new  Block(5,5)));
        assertFalse(tetromino.add(5, 5));
    }
    @Test
    public void removeTest(){
        field.put(1, 0, tetromino);
        field.put(1, 1, tetromino);
        field.put(2, 0, tetromino);
        field.put(2, 1, tetromino);
        
        tetromino.remove();

        assertNull(field.get(1, 0));
        assertNull(field.get(1, 1));
        assertNull(field.get(2, 0));
        assertNull(field.get(2, 1));
    }
    @Test
    public void putTest(){
        tetromino.put();
        
        assertEquals(field.get(1, 0), tetromino);
        assertEquals(field.get(1, 1), tetromino);
        assertEquals(field.get(2, 0), tetromino);
        assertEquals(field.get(2, 1), tetromino);
    }
    @Test
    public void rotateLeftTest(){
        field.clear();
        tetromino = IBlock.create(field, 0, 2);
        tetromino.put();
        
        assertFalse(tetromino.rotateLeft());
        
        field.clear();
        tetromino = IBlock.create(field, 2, 2);
        tetromino.put();
        
        assertTrue(tetromino.rotateLeft());
        
        assertEquals(field.get(0, 2), tetromino);
        assertEquals(field.get(1, 2), tetromino);
        assertEquals(field.get(2, 2), tetromino);
        assertEquals(field.get(3, 2), tetromino);
    }
    @Test
    public void rotateRightTest(){
        field.clear();
        tetromino = IBlock.create(field, 0, 2);
        tetromino.put();
        
        assertFalse(tetromino.rotateRight());
        
        field.clear();
        tetromino = IBlock.create(field, 2, 2);
        tetromino.put();
        
        assertTrue(tetromino.rotateRight());
        
        assertEquals(field.get(0, 2), tetromino);
        assertEquals(field.get(1, 2), tetromino);
        assertEquals(field.get(2, 2), tetromino);
        assertEquals(field.get(3, 2), tetromino);
    }
    @Test
    public void canRotateLeftTest(){
        assertTrue(tetromino.canRotateLeft());
        
        field.clear();
        tetromino = IBlock.create(field, 0, 2);
        tetromino.put();
        
        assertFalse(tetromino.canRotateLeft());
    }
    @Test
    public void canRotateRightTest(){
        assertTrue(tetromino.canRotateRight());
        
        field.clear();
        tetromino = IBlock.create(field, 0, 2);
        tetromino.put();
        
        assertFalse(tetromino.canRotateRight());
    }
    @Test
    public void moveCollisionTest(){
        field.put(0, 0, OBlock.create(field, 0, 0));
        assertFalse(tetromino.moveLeft());
    }
    @Test
    public void moveRightTest(){
        assertTrue(tetromino.moveRight());
        
        assertNull(field.get(1, 0));
        assertNull(field.get(1, 1));
        assertEquals(field.get(2, 0), tetromino);
        assertEquals(field.get(2, 1), tetromino);
        assertEquals(field.get(3, 0), tetromino);
        assertEquals(field.get(3, 1), tetromino);
        
        assertFalse(tetromino.moveRight());
    }
    @Test
    public void moveLeftTest(){
        assertTrue(tetromino.moveLeft());
        
        assertEquals(field.get(0, 0), tetromino);
        assertEquals(field.get(0, 1), tetromino);
        assertEquals(field.get(1, 0), tetromino);
        assertEquals(field.get(1, 1), tetromino);
        assertNull(field.get(2, 0));
        assertNull(field.get(2, 1));
        
        assertFalse(tetromino.moveLeft());
    }
    @Test
    public void canMoveRightTest(){
        assertTrue(tetromino.canMoveRight());
        
        field.clear();
        tetromino = OBlock.create(field, 2, 0);
        tetromino.toField();
        
        assertFalse(tetromino.canMoveRight());
    }
    @Test
    public void canMoveLeftTest(){
        assertTrue(tetromino.canMoveLeft());
        
        field.clear();
        tetromino = OBlock.create(field, 0, 0);
        tetromino.toField();
        
        assertFalse(tetromino.canMoveLeft());
    }
    @Test
    public void moveDownTest(){
        assertFalse(tetromino.moveDown());
        
        field.clear();
        tetromino = OBlock.create(field, 0, 1);
        tetromino.toField();
        
        assertTrue(tetromino.moveDown());
    }
    @Test
    public void canMoveDownTest(){
        assertFalse(tetromino.canMoveDown());
        
        field.clear();
        tetromino = OBlock.create(field, 0, 1);
        tetromino.toField();
        
        assertTrue(tetromino.canMoveDown());
    }
    @Test
    public void hasCollisionTest(){
        assertFalse(tetromino.hasCollision());
        
        field.put(1, 1, OBlock.create(field, 1, 1));
        
        assertTrue(tetromino.hasCollision());
    }
    @Test
    public void inBoundsTest(){
        assertTrue(tetromino.inBounds());
        
        tetromino = OBlock.create(field, 3, 3);
        assertFalse(tetromino.inBounds());
    }
    @Test
    public void toFieldOutOfBoundsTest(){
        tetromino = OBlock.create(field, 3, 3);
        assertFalse(tetromino.toField());
    }
    @Test
    public void toFieldTest(){
        assertTrue(tetromino.toField());
        
        assertEquals(field.get(1, 0),tetromino);
        assertEquals(field.get(1, 1),tetromino);
        assertEquals(field.get(2, 0),tetromino);
        assertEquals(field.get(2, 1),tetromino);
    }
    @Test
    public void getColorTest(){
        assertEquals(tetromino.color, tetromino.getColor());
    }
}
