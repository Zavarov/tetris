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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

/**
 *
 * @author Zavarov
 */
public class Panel extends JPanel{
    public static final String MOVE_LEFT = "moveLeft";
    public static final String MOVE_RIGHT = "moveRight";
    public static final String ROTATE_LEFT = "rotateLeft";
    public static final String ROTATE_RIGHT = "rotateRight";
    public static final String FALL_DOWN = "fallDown";
    public static final String START = "start";
    /**
     * The pixel between the inner and outer rectangle in the image.
     */
    private static final int INNER_RECTANGLE_OFFSET = 4;
    /**
     * The visual representation of the field.
     */
    protected BufferedImage image;
    /**
     * The width of the field.
     */
    protected int width;
    /**
     * The height of the field.
     */
    protected int height;
    /**
     * The width of one block in the field.
     */
    protected int blockWidth;
    /**
     * The height of one block in the field.
     */
    protected int blockHeight;
    /**
     * A reference to the game.
     */
    private final Game game;
    /**
     * Initializes the interface for the frame.<br>
     * The width and height of the underlying image will be rounded down to
     * a multiple of the columns and rows respectively.
     * @param columns the number of columns in the game.
     * @param rows the number of rows in the game.
     * @param width the width of the panel.
     * @param height the height of the panel.
     */
    public Panel(int columns, int rows, int width, int height){
        game = new Game(columns, rows, this::repaint);
        
        this.blockWidth = width / columns;
        this.blockHeight = height / rows;
        
        this.width = blockWidth * columns;
        this.height = blockHeight * rows;
        
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT , 0), MOVE_LEFT);
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), MOVE_RIGHT);
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP   , 0), ROTATE_LEFT);
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN , 0), ROTATE_RIGHT);
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), FALL_DOWN);
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S    , 0), START);
        
        getActionMap().put(MOVE_LEFT   , new LambdaAction(e -> game.moveLeft()));
        getActionMap().put(MOVE_RIGHT  , new LambdaAction(e -> game.moveRight()));
        getActionMap().put(ROTATE_LEFT , new LambdaAction(e -> game.rotateLeft()));
        getActionMap().put(ROTATE_RIGHT , new LambdaAction(e -> game.rotateRight()));
        getActionMap().put(FALL_DOWN, new LambdaAction(e -> game.fallDown()));
        getActionMap().put(START, new LambdaAction(e -> game.start()));
        
        game.clear();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
    }
    /**
     * Redraws the block at the given position.
     * @param column the column.
     * @param row the row.
     */
    private void repaint(int column, int row){
        Tetromino tetromino = game.get(column,row);
        if(tetromino == null)
            drawBlank(column, row);
        else
            drawBlock(column, row, tetromino);
        repaint();
    }
    /**
     * Draws the block at the specified position.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param tetromino the block that is drawn.
     */
    private void drawBlock(int x, int y, Tetromino tetromino){
        Graphics g = image.getGraphics();
        g.setColor(tetromino.getColor());
        g.fillRect(getXPosition(x), getYPosition(y), blockWidth, blockHeight);
        
        g.setColor(Color.BLACK);
        //-1 so that we don't have overlaps between the entries
        g.drawRect(getXPosition(x), getYPosition(y), blockWidth-1, blockHeight-1);
        
        //A a inner black rectangle to add a little detail.
        if(width > INNER_RECTANGLE_OFFSET * 2 && height > INNER_RECTANGLE_OFFSET * 2){
            g.setColor(Color.BLACK);
            g.drawRect(getXPosition(x) + INNER_RECTANGLE_OFFSET,
                       getYPosition(y) + INNER_RECTANGLE_OFFSET, 
                       blockWidth  - 2*INNER_RECTANGLE_OFFSET-1, 
                       blockHeight - 2*INNER_RECTANGLE_OFFSET-1);
        }
        g.dispose();
    }
    /**
     * Draws a blank block at the specified position.
     * If both x and y are both either odd or even, the block will be gray,
     * otherwise white.
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    private void drawBlank(int x, int y){
        Graphics g = image.getGraphics();
        Color c = x % 2 == y % 2 ? Color.LIGHT_GRAY : Color.WHITE;
        
        g.setColor(c);
        g.fillRect(x * blockWidth, height - (y+1) * blockHeight, blockWidth, blockHeight);
        
        g.dispose();
    }
    /**
     * @return the width of a single block of the field. 
     */
    public int getBlockWidth(){
        return blockWidth;
    }
    /**
     * @return the height of a single block on the field. 
     */
    public int getBlockHeight(){
        return blockHeight;
    }
    /**
     * @param x the column number
     * @return the coordinate of the column on the image.
     */
    private int getXPosition(int x){
        return x * blockWidth;
    }
    /**
     * @param y the row number.
     * @return the coordinate of the row on the image.
     */
    private int getYPosition(int y){
        return height - (y+1) * blockHeight;
    }
    /**
     * A wrapper class to simulate lambda operations for the Action class.
     */
    private class LambdaAction extends AbstractAction{
        private final Consumer<? super Object> consumer;
        public LambdaAction(Consumer<? super Object> consumer){
            this.consumer = consumer;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            consumer.accept(e);
            repaint();
        }
    }
}
