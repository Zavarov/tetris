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
import tetris.tetromino.Tetromino.Rotation;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Zavarov
 */
public class RotationTest {
    Game field;
    Tetromino.Block block;
    Tetromino tetromino;
    @Before
    public void setUp(){
        field = new Game(7, 20, (i,j) -> {});
        tetromino = TBlock.create(field, 0, 0);
        block = tetromino.new Block(1, 2);
    }
    @Test
    public void rotate90Test(){
        Rotation rotation = Rotation.DEG90;
        assertEquals(rotation.rotateX(block), 2);
        assertEquals(rotation.rotateY(block),-1);
    }
    @Test
    public void rotate270Test(){
        Rotation rotation = Rotation.DEG270;
        assertEquals(rotation.rotateX(block),-2);
        assertEquals(rotation.rotateY(block), 1);
    }
    @Test
    public void rotateNoneTest(){
        Rotation rotation = Rotation.NONE;
        assertEquals(rotation.rotateX(block), 1);
        assertEquals(rotation.rotateY(block), 2);
    }
    @Test
    public void rotateSwapTest(){
        Rotation rotation = Rotation.SWAP;
        assertEquals(rotation.rotateX(block), 2);
        assertEquals(rotation.rotateY(block), 1);
    }
}
