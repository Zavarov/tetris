package tetris.tetromino;

import tetris.Field;
import tetris.Game;

import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

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

/**
 * This class implements the basic functions of all the different blocks.
 * @author Zavarov
 */
public abstract class Tetromino {
    /**
     * The color for a single block of the tetromino.
     */
    protected final Color color;
    /**
     * The function for rotating counter clockwise.
     */
    protected final Rotation left;
    /**
     * The function for rotating clockwise.
     */
    protected final Rotation right;
    /**
     * The x coordinate of the center piece.
     */
    protected int xC;
    /**
     * The y coordinate of the center piece.
     */
    protected int yC;
    /**
     * The playing field
     */
    protected final Field field;
    /**
     * The four blocks that define this piece.
     */
    protected final Set<Block> blocks;
    /**
     * @param field the playing field.
     * @param xC the x coordinate of the center piece
     * @param yC the y coordinate of the center piece
     * @param left the function for rotating counter clockwise.
     * @param right the function for rotating clockwise.
     * @param color the color for a single block.
     */
    protected Tetromino(Field field, int xC, int yC, Rotation left, Rotation right, Color color){
        this.field = field;
        this.blocks = new HashSet<>(4);
        this.xC = xC;
        this.yC = yC;
        this.left = left;
        this.right = right;
        this.color = color;
    }
    /**
     * Adds a new block to the piece.
     * @param xOff the x offset from the center piece.
     * @param yOff the y offset from the center piece
     * @return true if the block hasn't already been added.
     */
    public boolean add(int xOff, int yOff){
        return blocks.add(new Block(xOff, yOff));
    }
    /**
     * Removes this tetromino from the field.
     */
    public void remove(){
        blocks.forEach(b -> field.remove(b.getX(), b.getY()));
    }
    /**
     * Puts this tetromino on the field.
     */
    public void put(){
        blocks.forEach(b -> field.put(b.getX(), b.getY(), this));
    }
    /**
     * Pushes the tetromino down until it hits an obstacle.
     * @return true if the block was moved.
     */
    public boolean fallDown(){
        if(!canMoveDown())
            return false;
        
        remove();
        while(canMoveDown())
            yC -= 1;
        put();
        return true;
    }
    /**
     * Rotates the block counter clockwise.
     * @return true if the tetromino was moved as part of this function. 
     */
    public boolean rotateLeft(){
        if(!canRotateLeft())
            return false;
        
        remove();
        blocks.forEach(Block::rotateLeft);
        put();
        return true;
    }
    /**
     * Rotates the block clockwise.
     * @return true if the tetromino was moved as part of this function. 
     */
    public boolean rotateRight(){
        if(!canRotateRight())
            return false;
        
        remove();
        blocks.forEach(Block::rotateRight);
        put();
        return true;
    }
    /**
     * @return true if the tetromino can be rotated counter clockwise. 
     */
    public boolean canRotateLeft(){
        return blocks.stream()
                .allMatch(b -> field.isValid(b.getLeftRotateX(), b.getLeftRotateY()));
    }
    /**
     * @return true if the tetromino can be rotated clockwise. 
     */
    public boolean canRotateRight(){
        return blocks.stream()
                .allMatch(b -> field.isValid(b.getRightRotateX(), b.getRightRotateY()));
    }
    /**
     * Moves the tetromino one step left.
     * @return true if the tetromino was moved as part of this function. 
     */
    public boolean moveLeft(){
        if(!canMoveLeft())
            return false;
        
        remove();
        xC -= 1;
        put();
        return true;
                
    }
    /**
     * Moves the tetromino one step to the right.
     * @return true if the tetromino was moved as part of this function. 
     */
    public boolean moveRight(){
        if(!canMoveRight())
            return false;
        
        remove();
        xC += 1;
        put();
        return true;
    }
    /**
     * Moves the tetromino one step down.
     * @return true if the tetromino was moved as part of this function. 
     */
    public boolean moveDown(){
        if(!canMoveDown())
            return false;
        
        remove();
        yC -= 1;
        put();
        return true;
    }
    /**
     * @return true if the tetromino can be moved to the left. 
     */
    public boolean canMoveLeft(){
        return canMove(x -> x - 1, y -> y);
    }
    /**
     * @return true if the tetromino can be moved to the right. 
     */
    public boolean canMoveRight(){
        return canMove(x -> x + 1, y -> y);
    }
    /**
     * @return true if the tetromino can fall. 
     */
    public boolean canMoveDown(){
        return canMove(x -> x, y -> y - 1);
        
    }
    /**
     * @return true if this tetromino overlaps with another tetromino. 
     */
    public boolean hasCollision(){
        return hasCollision(x -> x, y -> y);
    }
    /**
     * @return true if this tetromino is within the game field.
     */
    public boolean inBounds(){
        return inBounds(x -> x, y -> y);
    }
    /**
     * @param moveX the horizontal change.
     * @param moveY the vertical change.
     * @return true if the moved tetromino is still within the game field.
     */
    private boolean inBounds(Function<Integer,Integer> moveX, Function<Integer,Integer> moveY){
        return blocks.stream()
                .allMatch(b -> field.isValid(moveX.apply(b.getX()), moveY.apply(b.getY())));
    }
    /**
     * @param moveX the horizontal change.
     * @param moveY the vertical change.
     * @return true this tetromino collides with other tetrominos after moving it.
     */
    private boolean hasCollision(Function<Integer,Integer> moveX, Function<Integer,Integer> moveY){
        return blocks.stream()
                .map(b -> field.get(moveX.apply(b.getX()), moveY.apply(b.getY())))
                .anyMatch(t -> t != null && t != this);
    }
    /**
     * @return true if the tetromino was successfully put on the field. 
     */
    public boolean toField(){
        if(!canMove(x -> x, y -> y))
            return false;
        
        blocks.forEach(b -> field.put(b.getX(), b.getY(), this));
        return true;
    }
    /**
     * @param moveX the horizontal change.
     * @param moveY the vertical change.
     * @return true if the tetromino can be moved in the given direction. 
     */
    private boolean canMove(Function<Integer,Integer> moveX, Function<Integer,Integer> moveY){
        return inBounds(moveX, moveY) && !hasCollision(moveX, moveY);
    }
    /**
     * @return the color of the individual blocks. 
     */
    public Color getColor(){
        return color;
    }
    /**
     * The class for a single block in the tetrominoes.
     */
    protected class Block{
        /**
         * The x offset of this block.
         */
        private int xOff;
        /**
         * The y offset of this block.
         */
        private int yOff;
        /**
         * @param xOff the x offset of the block.
         * @param yOff the y offset of the block.
         */
        public Block(int xOff, int yOff){
            this.xOff = xOff;
            this.yOff = yOff;
        }
        /**
         * The coordinate consists of the center piece and the offset.
         * @return the x coordinate of the block. 
         */
        public int getX(){
            return xC + xOff;
        }
        /**
         * The coordinate consists of the center piece and the offset.
         * @return the y coordinate of the block. 
         */
        public int getY(){
            return yC + yOff;
        }
        /**
         * @return the x coordinate after rotating to the left.
         */
        public int getLeftRotateX(){
            return xC + left.rotateX(this);
        }
        /**
         * @return the y coordinate after rotating to the left.
         */
        public int getLeftRotateY(){
            return yC + left.rotateY(this);
        }
        /**
         * @return the x coordinate after rotating to the right.
         */
        public int getRightRotateX(){
            return xC + right.rotateX(this);
        }
        /**
         * @return the y coordinate after rotating to the right.
         */
        public int getRightRotateY(){
            return yC + right.rotateY(this);
        }
        /**
         * Rotate the block by 90° by multiplying with the rotation matrix
         */
        public void rotateLeft(){
            int newX = left.rotateX(this);
            int newY = left.rotateY(this);

            xOff = newX;
            yOff = newY;
        }
        /**
         * Rotate the block by 270° by multiplying with the rotation matrix
         */
        public void rotateRight(){
            int newX = right.rotateX(this);
            int newY = right.rotateY(this);

            xOff = newX;
            yOff = newY;
        }
        /**
         * @param o the object this block is compared with.
         * @return true if the object is a block and the offsets match.
         */
        @Override
        public boolean equals(Object o){
            if(!(o instanceof Block))
                return false;
            Block block = (Block)o;
            return xOff == block.xOff && yOff == block.yOff;
        }
        /**
         * @return a hash code based on the offsets. 
         */
        @Override
        public int hashCode() {
            return Objects.hash(xOff, yOff);
        }
    }
    /**
     * This enum provides the methods for rotating the blocks.
     */
    protected enum Rotation{
        /**
         * Don't rotate
         */
        NONE(b -> b.xOff, b -> b.yOff),
        /**
         * Rotate by 90°
         */
        DEG270(b -> -b.yOff, b -> b.xOff),
        /**
         * Rotate by 270°
         */
        DEG90(b -> b.yOff, b -> -b.xOff),
        /**
         * Swap x and y coordinates.
         */
        SWAP(b -> b.yOff, b -> b.xOff);
        /**
         * @param b the block before the rotation.
         * @return the y coordinate after the rotation.
         */
        public int rotateY(Block b){
            return rotateY.apply(b);
        }
        /**
         * @param b the block before the rotation.
         * @return the x coordinate after the rotation.
         */
        public int rotateX(Block b){
            return rotateX.apply(b);
        }
        /**
         * The function for rotating the x coordinate.
         */
        private final Function<Block,Integer> rotateX;
        /**
         * The function for rotating the y coordinate.
         */
        private final Function<Block,Integer> rotateY;
        /**
         * @param rotateX the function for rotating the x coordinate.
         * @param rotateY the function for rotating the y coordinate.
         */
        Rotation(Function<Block, Integer> rotateX, Function<Block, Integer> rotateY){
            this.rotateX = rotateX;
            this.rotateY = rotateY;
        }
    }
    /**
     * This enum contains all possible type of blocks.
     */
    public enum Type{
        IBLOCK(IBlock::create),
        JBLOCK(JBlock::create),
        LBLOCK(LBlock::create),
        OBLOCK(OBlock::create),
        SBLOCK(SBlock::create),
        TBLOCK(TBlock::create),
        ZBLOCK(ZBlock::create);
        
        /**
         * @param game the game the tetromino is in.
         * @param x the x coordinate of the center.
         * @param y the y coordinate of the center.
         * @return a fresh tetromino. 
         */
        public Tetromino create(Game game, int x, int y){
            return generator.apply(game, x, y);
        }
        
        /**
         * The generator for the games.<br>
         * Takes the x and y coordinate of the center piece and a game and
         * creates a tetromino in the given game.
         */
        private final TriFunction<Game,Integer,Integer,Tetromino> generator;
        /**
         * @param generator the generator for the blocks. 
         */
        Type(TriFunction<Game, Integer, Integer, Tetromino> generator){
            this.generator = generator;
        }
    }
    /**
     * A helper class for creating the different tetrominos.<br>
     * @param <A> the first type.
     * @param <B> the second type.
     * @param <C> the third type.
     * @param <R> the type of the result value.
     */
    @FunctionalInterface
    private interface TriFunction<A,B,C,R>{
        R apply(A a, B b, C c);
    }
}