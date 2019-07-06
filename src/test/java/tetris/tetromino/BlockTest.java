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
import tetris.tetromino.Tetromino.Block;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 *
 * @author Zavarov
 */
public class BlockTest {
    Game field;
    Block block;
    Tetromino tetromino;
    @Before
    public void setUp(){
        field = new Game(7, 20, (i,j) -> {});
        tetromino = TBlock.create(field, 0, 0);
        block = tetromino.new Block(0, 1);
    }
    @Test
    public void getXTest(){
        assertEquals(block.getX(), 0);
    }
    @Test
    public void getYTest(){
        assertEquals(block.getY(), 1);
    }
    @Test
    public void getLeftRotateXTest(){
        assertEquals(block.getLeftRotateX(),-1);
    }
    @Test
    public void getLeftRotateYTest(){
        assertEquals(block.getLeftRotateY(), 0);
        
    }
    @Test
    public void getRightRotateXTest(){
        assertEquals(block.getRightRotateX(), 1);
    }
    @Test
    public void getRightRotateYTest(){
        assertEquals(block.getRightRotateY(), 0);
    }
    @Test
    public void rotateLeftTest(){
        block.rotateLeft();
        assertEquals(block.getX(),-1);
        assertEquals(block.getY(), 0);
    }
    @Test
    public void rotateRightTest(){
        block.rotateRight();
        assertEquals(block.getX(), 1);
        assertEquals(block.getY(), 0);
    }
    @Test
    public void equalsTest(){
        assertNotEquals(block, tetromino);
        assertNotEquals(block, tetromino.new Block(0, 0));
        assertNotEquals(block, tetromino.new Block(1, 0));
        assertEquals(block, tetromino.new Block(0, 1));
    }
    @Test
    public void hashCodeTest(){
        assertEquals(block.hashCode(), tetromino.new Block(0, 1).hashCode());
    }
}
