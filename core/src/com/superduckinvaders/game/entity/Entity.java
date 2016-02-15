package com.superduckinvaders.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.assets.TextureSet;

/**
 * Represents an object in the game.
 */
public abstract class Entity {

    /**
     * The round that this Entity is in.
     */
    protected Round parent;

    /**
     * The x and y coordinates of this Entity.
     */
    protected double x, y;

    /**
     * The x and y velocity of this MobileEntity in pixels per second.
     */
    protected double velocityX = 0, velocityY = 0;

    /**
     * Whether or not to remove this Entity on the next frame.
     */
    protected boolean removed = false;

    /**
     * Initialises this Entity with zero initial coordinates.
     *
     * @param parent the round this Entity belongs to
     */
    public Entity(Round parent) {
        this(parent, 0, 0);
    }

    /**
     * Initialises this Entity with the specified initial coordinates.
     *
     * @param parent the round this Entity belongs to
     * @param x      the initial x coordinate
     * @param y      the initial y coordinate
     */
    public Entity(Round parent, double x, double y) {
        this.parent = parent;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate of the entity
     * @return the x coordinate of this Entity
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the entity
     * @return the y coordinate of this Entity
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the x velocity of the entity
     * @return the x velocity of this MobileEntity in pixels per second
     */
    public double getVelocityX() {
        return velocityX;
    }

    /**
     * Returns the y velocity of the entity
     * @return the y coordinate of this MobileEntity in pixels per second
     */
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * Returns true if the specified rectangle intersects this Entity.
     *
     * @param x      the x coordinate of the rectangle's bottom left corner
     * @param y      the y coordinate of the rectangle's bottom left corner
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     * @return whether the specified rectangle intersects this Entity
     */
    public boolean intersects(double x, double y, int width, int height) {
        return this.x < x + width && this.x + getWidth() > x && this.y < y + height && this.y + getHeight() > y;
    }

    /**
     * Returns the distance between this Entity and the specified coordinates.
     *
     * @param x the x coordinate to compare with
     * @param y the y coordinate to compare with
     * @return the distance between this Entity and the coordinates, in pixels
     */
    public double distanceTo(double x, double y) {
        return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
    }

    /**
     * Returns the angle between this Entity and the specified coordinates.
     *
     * @param x the x coordinate to compare with
     * @param y the y coordinate to compare with
     * @return the angle between this Entity and the coordinates, in radians
     */
    public double angleTo(double x, double y) {
        return Math.atan2(y - this.y, x - this.x);
    }

    /**
     * Returns the direction to the specified coordinates from this Entity (one of the FACING_ constants in TexutreSet).
     *
     * @param x the x coordinate to compare with
     * @param y the y coordinate to compare with
     * @return the direction the coordinates are in relative to this Entity
     */
    public int directionTo(double x, double y) {
        double angle = angleTo(x, y);
        
        if (angle < Math.PI * 3 / 4 && angle >= Math.PI / 4) {
            return TextureSet.FACING_BACK;
        } else if (angle < Math.PI / 4 && angle >= -Math.PI / 4) {
            return TextureSet.FACING_RIGHT;
        } else if (angle < -Math.PI / 4 && angle >= -Math.PI * 3 / 4) {
            return TextureSet.FACING_FRONT;
        } else {
            return TextureSet.FACING_LEFT;
        }
    }

    /**
     * Returns the width of the entity
     * @return the width of this Entity
     */
    public abstract int getWidth();

    /**
     * Returns the height of the entity
     * @return the height of this Entity
     */
    public abstract int getHeight();

    /**
     * Returns if this entity should be removed
     * @return whether this Entity has been removed
     */
    public boolean isRemoved() {
        return removed;
    }

    /**
     * Ensures that this MobileEntity stays within the map area.
     */
    protected void checkBounds() {
        if (x < 0) {
            x = 0;
        } else if (x > parent.getMapWidth() - getWidth()) {
            x = parent.getMapWidth() - getWidth();
        }

        if (y < 0) {
            y = 0;
        } else if (y > parent.getMapHeight() - getHeight()) {
            y = parent.getMapHeight() - getHeight();
        }
    }

    

    /**
	 * Gets whether the map tile at the specified coordinates is blocked or not.
	 *
	 * @param x the x coordinate of the map tile
	 * @param y the y coordinate of the map tile
	 * @return whether or not the map tile is blocked
	 */
	public boolean isTileBlocked(int x, int y) {
		int tileX = x / parent.getTileWidth();
		int tileY = y / parent.getTileHeight();

		//allows the player to fly over bushes (and only the player)
		if (this instanceof Player && parent.getPlayer().isFlying()){
			return parent.getCollisionLayer().getCell(tileX, tileY) != null;
		}
		else {
			return parent.getCollisionLayer().getCell(tileX, tileY) != null || (parent.getObstaclesLayer() != null && parent.getObstaclesLayer().getCell(tileX, tileY) != null);
		}
		 
	}
    
    
    /**
     * Gets whether the specified x delta will cause a collision on the left or right.
     *
     * @param deltaX the x delta
     * @return whether a collision would occur on the left or right
     */
    public boolean collidesX(double deltaX) {
        // Check for entity collisions.
        for (Entity entity : parent.getEntities()) {
            if (entity == this || this instanceof Projectile) {
                continue;
            }

            if (entity instanceof Character && entity.intersects(x + deltaX, y, getWidth(), getHeight())) {
                return true;
            }
        }

        // Check for tile collisions.
        return collidesLeft(deltaX) || collidesRight(deltaX);
    }

    /**
     * Gets whether the specified y delta will cause a collision on the bottom or top.
     *
     * @param deltaY the y delta
     * @return whether a collision would occur on the bottom or top
     */
    public boolean collidesY(double deltaY) {
        // Check for entity collisions.
        for (Entity entity : parent.getEntities()) {
            // Don't damage my owner.
            if (entity == this || this instanceof Projectile) {
                continue;
            }

            if (entity instanceof Character && entity.intersects(x, y + deltaY, getWidth(), getHeight())) {
                return true;
            }
        }

        // Check for tile collisions.
        return collidesBottom(deltaY) || collidesTop(deltaY);
    }

    /**
     * Gets whether specified x delta will cause a collision from an arbitrary position
     * Used in AI path detection.
     *
     * @param deltaX the x delta
     * @param fromX  arbitrary x position
     * @param fromY  arbitrary y position
     * @return whether collides
     */
    public boolean collidesXfrom(double deltaX, double fromX, double fromY) {
        double tempX = this.x;
        double tempY = this.y;
        this.x = fromX;
        this.y = fromY;
        boolean result = collidesX(deltaX);
        this.x = tempX;
        this.y = tempY;
        return result;
    }

    /**
     * Gets whether specified y delta will cause a collision from an arbitrary position
     * Used in AI path detection.
     *
     * @param deltaY the y delta
     * @param fromX  arbitrary x position
     * @param fromY  arbitrary y position
     * @return whether collides
     */
    public boolean collidesYfrom(double deltaY, double fromX, double fromY) {
        double tempX = this.x;
        double tempY = this.y;
        this.x = fromX;
        this.y = fromY;
        boolean result = collidesY(deltaY);
        this.x = tempX;
        this.y = tempY;
        return result;
    }

    /**
     * Gets whether the specified x delta will cause a collision on the left.
     *
     * @param deltaX the x delta
     * @return whether a collision would occur on the left
     */
    private boolean collidesLeft(double deltaX) {
        // If entity is smaller than tile we can just check to see if each corner collides instead of all points along the edge.
        if (getHeight() <= parent.getTileHeight()) {
            return isTileBlocked((int) Math.floor(x + deltaX), (int) y) || isTileBlocked((int) Math.floor(x + deltaX), (int) y + getHeight());
        } else {
            for (int i = (int) y; i < y + getHeight(); i++) {
                if (isTileBlocked((int) Math.floor(x + deltaX), i)) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * Gets whether the specified x delta will cause a collision on the right.
     *
     * @param deltaX the x delta
     * @return whether a collision would occur on the right
     */
    private boolean collidesRight(double deltaX) {
        // If entity is smaller than tile we can just check to see if each corner collides instead of all points along the edge.
        if (getHeight() <= parent.getTileHeight()) {
            return isTileBlocked((int) Math.floor(x + getWidth() + deltaX), (int) y) || isTileBlocked((int) Math.floor(x + getWidth() + deltaX), (int) y + getHeight());
        } else {
            for (int i = (int) y; i < y + getHeight(); i++) {
                if (isTileBlocked((int) Math.ceil(x + getWidth() - 1 + deltaX), i)) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * Gets whether the specified y delta will cause a collision on the bottom.
     *
     * @param deltaY the y delta
     * @return whether a collision would occur on the bottom
     */
    private boolean collidesBottom(double deltaY) {
        // If entity is smaller than tile we can just check to see if each corner collides instead of all points along the edge.
        if (getWidth() <= parent.getTileWidth()) {
            return isTileBlocked((int) x, (int) Math.floor(y + deltaY)) || isTileBlocked((int) x + getWidth(), (int) Math.floor(y + deltaY));
        } else {
            for (int i = (int) x; i < x + getWidth(); i++) {
                if (isTileBlocked(i, (int) Math.floor(y + deltaY))) {
                    return true;
                }
            }

            return false;
        }
    }


    /**
     * Gets whether the specified y delta will cause a collision on the top.
     *
     * @param deltaY the y delta
     * @return whether a collision would occur on the top
     */
    private boolean collidesTop(double deltaY) {
        // If entity is smaller than tile we can just check to see if each corner collides instead of all points along the edge.
        if (getWidth() <= parent.getTileWidth()) {
            return isTileBlocked((int) x, (int) Math.floor(y + getHeight() + deltaY)) || isTileBlocked((int) x + getWidth(), (int) Math.floor(y + getHeight() + deltaY));
        } else {
            for (int i = (int) x; i < x + getWidth(); i++) {
                if (isTileBlocked(i, (int) Math.ceil(y + getHeight() - 1 + deltaY))) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * Updates the state of this Entity.
     *
     * @param delta how much time has passed since the last update
     */
    public void update(float delta) {
        double deltaX = velocityX * delta;
        double deltaY = velocityY * delta;

        if (collidesX(deltaX)) {
            deltaX = 0;
        }

        if (collidesY(deltaY)) {
            deltaY = 0;
        }

        x += deltaX;
        y += deltaY;

        checkBounds();
    }

    /**
     * Renders this Entity.
     *
     * @param spriteBatch the sprite batch on which to render
     */
    public abstract void render(SpriteBatch spriteBatch);
}
