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
import tetris.tetromino.Tetromino.Type;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author Zavarov
 */
public class TypeTest {
    Game field;
    @Before
    public void setUp(){
        field = new Game(7, 20, (i,j) -> {});
    }
    @Test
    public void createIBlockTest(){
        assertTrue(Type.IBLOCK.create(field,0,0) instanceof IBlock);
    }
    @Test
    public void createJBlockTest(){
        assertTrue(Type.JBLOCK.create(field,0,0) instanceof JBlock);
    }
    @Test
    public void createLBlockTest(){
        assertTrue(Type.LBLOCK.create(field,0,0) instanceof LBlock);
    }
    @Test
    public void createOBlockTest(){
        assertTrue(Type.OBLOCK.create(field,0,0) instanceof OBlock);
    }
    @Test
    public void createSBlockTest(){
        assertTrue(Type.SBLOCK.create(field,0,0) instanceof SBlock);
    }
    @Test
    public void createTBlockTest(){
        assertTrue(Type.TBLOCK.create(field,0,0) instanceof TBlock);
    }
    @Test
    public void createZBlockTest(){
        assertTrue(Type.ZBLOCK.create(field,0,0) instanceof ZBlock);
    }
}
