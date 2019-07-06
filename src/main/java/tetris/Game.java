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
import tetris.tetromino.Tetromino.Type;

import java.util.concurrent.*;
import java.util.function.BiConsumer;

/**
 * This class implements the drawn playing field.
 * @author Zavarov
 */
public class Game extends Field{
    /**
     * The block that is currently selected.
     */
    protected Tetromino currentBlock;
    /**
     * The x coordinate of every new block.
     */
    protected int xStart;
    /**
     * The y coordinate of every new block.
     */
    protected int yStart;
    /**
     * The mutex so that only one thread may modify the game.
     */
    protected Semaphore mutex;
    /**
     * The executor that is responsible for the falling blocks.
     */
    protected ScheduledExecutorService executor;
    /**
     * The time in seconds it takes for a block to fall.
     */
    protected static long PERIOD = 1;
    /**
     * The function that updates the visual interface.
     */
    protected BiConsumer<Integer,Integer> update;
    /**
     * A reference to the currently active session.
     */
    protected ScheduledFuture<?> session;
    /**
     * Initializes an empty field.<br>
     * The width and height will be rounded down to a multiple of the columns and
     * rows respectively.
     * @param columns the number of columns in the field.
     * @param rows the number of rows in the field.
     * @param update the consumer that updates the underlying visual interface.
     */
    public Game(int columns, int rows, BiConsumer<Integer,Integer> update){
        super(columns, rows);
        
        this.xStart = columns / 2;
        this.yStart = rows - 2;
        this.mutex = new Semaphore(1);
        this.update = update;
        executor = Executors.newSingleThreadScheduledExecutor();
    }
    /**
     * @return a random new tetromino.
     */
    private Tetromino newTetromino(){
        Type[] types = Tetromino.Type.values();
        int number = ThreadLocalRandom.current().nextInt(types.length);
        return types[number].create(this, xStart, yStart);
    }
    /**
     * Starts a new game.
     */
    public void start(){
        clear();
        
        currentBlock = newTetromino();
        currentBlock.put();
        session = executor.scheduleAtFixedRate(new GameLogic(), PERIOD, PERIOD, TimeUnit.SECONDS);
    }
    /**
     * Replaces the element at the specified position and paints the new block.
     * @param column the x coordinate.
     * @param row the y coorindate.
     * @param tetromino the new element. 
     * @return the input if the coordinates are inside the field, otherwise null.
     */
    @Override
    public Tetromino put(int column, int row, Tetromino tetromino){
        tetromino = super.put(column, row, tetromino);
        update.accept(column, row);
        return tetromino;
    }
    /**
     * Removes one element from the field and paints an empty block at the given
     * positon.
     * @param column the x coordinate.
     * @param row the y coordinate.
     * @return the element at (x,y) that was removed.
     */
    @Override
    public Tetromino remove(int column, int row){
        Tetromino tetromino = super.remove(column, row);
        update.accept(column, row);
        return tetromino;
    }
    /**
     * Removes the row pushes all rows above it down by one and updates
     * the image.
     * @param row the row.
     * @return true if the field was changed as a result of this function.
     */
    @Override
    public boolean remove(int row){
        if(super.remove(row)){
            //All blocks above this row also need to be repainted
            for(int y = rows - 1 ; y >= row ; --y)
                for(int x = 0 ; x < columns; ++x)
                    update.accept(x, y);
            return true;
        }else{
            return false;
        }
    }
    /**
     * Moves the current tetromino to the left.
     */
    public void moveLeft(){
        mutex.acquireUninterruptibly();
        if(currentBlock != null)
            currentBlock.moveLeft();
        mutex.release();
    }
    /**
     * Moves the current tetromino to the right.
     */
    public void moveRight(){
        mutex.acquireUninterruptibly();
        if(currentBlock != null)
            currentBlock.moveRight();
        mutex.release();
    }
    /**
     * Rotates the current tetromino counter clockwise.
     */
    public void rotateLeft(){
        mutex.acquireUninterruptibly();
        if(currentBlock != null)
            currentBlock.rotateLeft();
        mutex.release();
    }
    /**
     * Rotates the current tetromino clockwise.
     */
    public void rotateRight(){
        mutex.acquireUninterruptibly();
        if(currentBlock != null)
            currentBlock.rotateRight();
        mutex.release();
    }
    /**
     * Moves the current tetromino to the bottom.
     */
    public void fallDown(){
        mutex.acquireUninterruptibly();
        if(currentBlock != null)
            currentBlock.fallDown();
        mutex.release();
    }
    /**
     * Ends the current game.
     */
    private void end(){
        currentBlock.put();
        currentBlock = null;
        if(session != null)
            session.cancel(true);
    }
    /**
     * Removes all rows that have been filled by the most recent block.
     */
    private void deleteFullRows(){
        int y = 0;
        while(y < rows){
            if(this.isFull(y))
                remove(y);
            else
                ++y;
        }
    }
    /**
     * This class deals with the automated movement of the blocks.
     */
    private class GameLogic implements Runnable{
        @Override
        public void run(){
            mutex.acquireUninterruptibly();
            
            if(!currentBlock.moveDown()){
                deleteFullRows();
                currentBlock = newTetromino();
                
                if(currentBlock.hasCollision())
                    end();
                else
                    currentBlock.put();
            }
            
            mutex.release();
        }
    }
    /**
     * Resets the current game.
     */
    @Override
    public void clear(){
        super.clear();
        if(session != null)
            session.cancel(true);
    }
}