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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tetris.tetromino.OBlock;
import tetris.tetromino.Tetromino;
import tetris.tetromino.ZBlock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 * @author Zavarov
 */
public class GameTest {
    Game game;
    Tetromino tetromino;
    List<Object> list = new ArrayList<>();
    @Before
    public void setUp(){
        game = new Game(4,3,(i,j) -> list.add(i+","+j));
        tetromino = OBlock.create(game, 1, 0);
        tetromino.toField();
        list.clear();
    }
    @After
    public void tearDown(){
        if(game.session != null)
            game.session.cancel(true);
    }
    @Test
    public void putTest(){
        assertEquals(game.put(0,2,tetromino),tetromino);
        assertEquals(list.size(),1);
    }
    @Test
    public void removeTest(){
        assertEquals(game.remove(1,0),tetromino);
        assertEquals(list.size(),1);
    }
    @Test
    public void removeRowTest(){
        assertTrue(game.remove(1));
        assertEquals(list.size(),4*3);
    }
    @Test
    public void removeInvalidRowTest(){
        assertFalse(game.remove(-1));
        assertTrue(list.isEmpty());
    }
    @Test
    public void fallDownTest(){
        game.clear();
        game.currentBlock = OBlock.create(game, 1, 1);
        game.currentBlock.put();
        game.fallDown();
        
        assertEquals(game.get(1, 0), game.currentBlock);
        assertEquals(game.get(1, 1), game.currentBlock);
        assertEquals(game.get(2, 0), game.currentBlock);
        assertEquals(game.get(2, 1), game.currentBlock);
    }
    
    @Test
    public void fallDownNullTest(){
        game.clear();
        game.fallDown();
        
        assertNull(game.get(1, 0));
        assertNull(game.get(0, 1));
        assertNull(game.get(1, 0));
        assertNull(game.get(1, 1));
    }
    @Test
    public void moveRightTest(){
        game.currentBlock = tetromino;
        game.moveRight();
        
        assertNull(game.get(1, 0));
        assertNull(game.get(1, 1));
        assertEquals(game.get(2, 0), tetromino);
        assertEquals(game.get(2, 1), tetromino);
        assertEquals(game.get(3, 0), tetromino);
        assertEquals(game.get(3, 1), tetromino);
    }
    @Test
    public void moveRightNullTest(){
        game.moveRight();
        
        assertEquals(game.get(1, 0), tetromino);
        assertEquals(game.get(1, 1), tetromino);
        assertEquals(game.get(2, 0), tetromino);
        assertEquals(game.get(2, 1), tetromino);
        assertNull(game.get(3, 0));
        assertNull(game.get(3, 1));
    }
    @Test
    public void moveLeftTest(){
        game.currentBlock = tetromino;
        game.moveLeft();
        
        assertEquals(game.get(0, 0), tetromino);
        assertEquals(game.get(0, 1), tetromino);
        assertEquals(game.get(1, 0), tetromino);
        assertEquals(game.get(1, 1), tetromino);
        assertNull(game.get(2, 0));
        assertNull(game.get(2, 1));
    }
    @Test
    public void moveLeftNullTest(){
        game.moveLeft();
        
        assertNull(game.get(0, 0));
        assertNull(game.get(0, 1));
        assertEquals(game.get(1, 0), tetromino);
        assertEquals(game.get(1, 1), tetromino);
        assertEquals(game.get(2, 0), tetromino);
        assertEquals(game.get(2, 1), tetromino);
    }
    @Test
    public void rotateLeftTest(){
        game.clear();
        game.currentBlock = ZBlock.create(game, 2, 1);
        game.currentBlock.put();
        
        game.rotateLeft();
        
        assertEquals(game.get(1, 0), game.currentBlock);
        assertEquals(game.get(1, 1), game.currentBlock);
        assertEquals(game.get(2, 1), game.currentBlock);
        assertEquals(game.get(2, 2), game.currentBlock);
    }
    @Test
    public void rotateLeftNullTest(){
        game.clear();
        
        game.rotateLeft();
        
        assertNull(game.get(1, 0));
        assertNull(game.get(1, 1));
        assertNull(game.get(2, 1));
        assertNull(game.get(2, 2));
    }
    @Test
    public void rotateRightTest(){
        game.clear();
        game.currentBlock = ZBlock.create(game, 2, 1);
        game.currentBlock.put();
        
        game.rotateRight();
        
        assertEquals(game.get(2, 0), game.currentBlock);
        assertEquals(game.get(2, 1), game.currentBlock);
        assertEquals(game.get(3, 1), game.currentBlock);
        assertEquals(game.get(3, 2), game.currentBlock);
    }
    @Test
    public void rotateRightNullTest(){
        game.clear();
        
        game.rotateRight();
        
        assertNull(game.get(2, 0));
        assertNull(game.get(2, 1));
        assertNull(game.get(3, 1));
        assertNull(game.get(3, 2));
    }
    
    /*
    private boolean equals(BufferedImage img1, int x1, int y1, BufferedImage img2, int x2, int y2){
        for(int j = 0 ; j < field.getBlockHeight() ; ++j)
            for(int i = 0 ; i < field.getBlockWidth() ; ++i)
                if(img1.getRGB(x1 + i, y1 + j) != img2.getRGB(x2 + i, y2 + j))
                    return false;
        return true;
    }
    */
}
