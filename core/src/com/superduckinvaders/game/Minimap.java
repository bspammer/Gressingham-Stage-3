package com.superduckinvaders.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.superduckinvaders.game.entity.Player;
import com.superduckinvaders.game.Round;

public class Minimap {
	
	private static final int BORDER_WIDTH = 2;
	
	private Round round;
	private SpriteBatch spriteBatch;
	
	private enum minimapColors {
		GRASS,
		WATER,
		PATH,
		BUSH,
		BUILDING,
		TREE_BOTTOM,
		TREE_TOP,
		OBJECTIVE,
		PLAYER
	}
	
	private static HashMap<minimapColors, Integer> colorDictionary = new HashMap<minimapColors, Integer>();
	static {
		colorDictionary.put(minimapColors.GRASS, 0x7DC847FF);
		colorDictionary.put(minimapColors.WATER, 0x6983E8FF);
		colorDictionary.put(minimapColors.PATH, 0xE8B969FF);
		colorDictionary.put(minimapColors.BUSH, 0x42AA52FF);
		colorDictionary.put(minimapColors.BUILDING, 0xA2693EFF);
		colorDictionary.put(minimapColors.TREE_BOTTOM, 0x4E3A21FF);
		colorDictionary.put(minimapColors.TREE_TOP, 0x108239FF);
		colorDictionary.put(minimapColors.OBJECTIVE, 0xFF0000FF);
		colorDictionary.put(minimapColors.PLAYER, 0xFFFFFFFF);
	}
	
	
	
	public Minimap(Round round,SpriteBatch spriteBatch){
		this.round=round;
		this.spriteBatch=spriteBatch;
	}
	
	/**
	 * draws a minimap in the top right using coloured cells.
	 */
	public void drawMinimap(int prevWindowWidth,int prevWindowHeight) {
		Player player = round.getPlayer();
		MapLayers layers = round.getMap().getLayers();

		int tileWidth = round.getTileWidth();
		int tileHeight = round.getTileHeight();
		int mapWidth = round.getMapWidth();
		int mapHeight = round.getMapHeight();
		int playerX = (int) player.getX()/tileWidth;
		int playerY = (int) player.getY()/tileHeight;

		// Odd numbers so player is centred
		int minimapWidth = 51;
		int minimapHeight = 51;
		int minimapScale = 4;
		
		//calculate offset for positioning of stamina bar
		int resizeOffsetX = (Gdx.graphics.getWidth() - prevWindowWidth ) == 0 ? minimapWidth*minimapScale : (minimapWidth*minimapScale + (Gdx.graphics.getWidth() - prevWindowWidth));
		int resizeOffsetY = (Gdx.graphics.getHeight() - prevWindowHeight ) == 0 ? minimapHeight*minimapScale : (minimapHeight*minimapScale + (Gdx.graphics.getHeight() - prevWindowHeight));
		
		int minimapX = Gdx.graphics.getWidth() - resizeOffsetX - 88;
		int minimapY = Gdx.graphics.getHeight() - resizeOffsetY - 8;
		int minimapXOffset = playerX - minimapWidth/2;
		int minimapYOffset = playerY - minimapHeight/2;
		// +2 pixels for the border
		Pixmap minimapData = new Pixmap(minimapWidth*minimapScale+2*BORDER_WIDTH, minimapHeight*minimapScale+2*BORDER_WIDTH, Pixmap.Format.RGBA8888);
		TiledMapTileLayer waterLayer = (TiledMapTileLayer) layers.get("Water");
		TiledMapTileLayer baseLayer = (TiledMapTileLayer) layers.get("Base");
		TiledMapTileLayer collisionLayer = (TiledMapTileLayer) layers.get("Collision");
		TiledMapTileLayer obstaclesLayer = round.getObstaclesLayer();
		TiledMapTileLayer overhangLayer = (TiledMapTileLayer) layers.get("Overhang");
		
		
		

		if (playerX < minimapWidth/2) {
			minimapXOffset = 0;
		}
		if (playerY < minimapHeight/2) {
			minimapYOffset = 0;
		}
		if (playerX >= mapWidth/tileWidth - minimapWidth/2) {
			minimapXOffset = mapWidth/tileWidth - minimapWidth;
		}
		if (playerY >= mapHeight/tileHeight - minimapHeight/2) {
			minimapYOffset = mapHeight/tileHeight - minimapHeight;
		}
		
		// Defines the colour for each cell
		for (int i=0; i<minimapWidth; i++) {
			for (int j=0; j<minimapHeight; j++) {
				// Default green grass colour
				int cellColor = colorDictionary.get(minimapColors.GRASS);
				int absoluteX = i + minimapXOffset;
				int absoluteY = j + minimapYOffset;

				// Cell is on the base layer
				if (baseLayer.getCell(absoluteX, absoluteY) != null) {
					int cellID = baseLayer.getCell(absoluteX, absoluteY).getTile().getId();
					//if the cell is not one of the grass cells then it must be a path cell
					if (cellID!=1 && cellID!=2 && cellID!=11 && cellID!=12 && cellID!=13 && cellID!=21 && cellID!=22 && cellID!=26 && cellID!=31 && cellID!=33 && cellID!=51 && cellID!=52 && cellID!=53 && cellID!=228){
						cellColor = colorDictionary.get(minimapColors.PATH);
					}
				}
				// Cell is on the water layer
				if (waterLayer.getCell(absoluteX, absoluteY) != null) {
					cellColor = colorDictionary.get(minimapColors.WATER);
				}
				// Cell is in the collision layer 
				if (collisionLayer.getCell(absoluteX, absoluteY) != null) {
					int cellID = collisionLayer.getCell(absoluteX, absoluteY).getTile().getId();
					if (cellID == 8) {
						cellColor = colorDictionary.get(minimapColors.BUSH);
					} else {
						cellColor = 0xA2693EFF;
					}
				}
				// Cell is on the obstacles layer (bushes)
				if (obstaclesLayer.getCell(absoluteX, absoluteY) != null) {
					int cellID = obstaclesLayer.getCell(absoluteX, absoluteY).getTile().getId();
					if (cellID == 8) {
						cellColor = colorDictionary.get(minimapColors.BUSH);
					}
					else if (cellID ==49 || cellID ==50) {
						cellColor = colorDictionary.get(minimapColors.TREE_BOTTOM);
					}
					else {
						cellColor = colorDictionary.get(minimapColors.BUILDING);
					}
				}
				// Cell contains objective
				if (Integer.parseInt(round.getMap().getProperties().get("ObjectiveX").toString())==i+minimapXOffset+1
						&& Integer.parseInt(round.getMap().getProperties().get("ObjectiveY").toString())==j+minimapYOffset) {
					cellColor = colorDictionary.get(minimapColors.OBJECTIVE);
				}
				
				// Cell contains an overhang
				if (overhangLayer.getCell(absoluteX, absoluteY) != null){
					int cellID = overhangLayer.getCell(absoluteX, absoluteY).getTile().getId();
					if (cellID == 9 || cellID == 10 || cellID == 29 || cellID == 30){
						cellColor = colorDictionary.get(minimapColors.TREE_TOP);
					}
				}
				
				// Cell contains player
				if (playerX - minimapXOffset == i && playerY - minimapYOffset == j) {
					cellColor = colorDictionary.get(minimapColors.PLAYER);
				}
				
				for (int k=0; k<minimapScale; k++) {
					for (int l=0; l<minimapScale; l++) {
						minimapData.drawPixel(i*minimapScale+k+BORDER_WIDTH, -(j*minimapScale+l)+minimapHeight*minimapScale+BORDER_WIDTH-1, cellColor);
					}
				}
			}
		}

		// Draw minimap border
		minimapData.setColor(Color.BLACK);
		minimapData.fillRectangle(0, 0, minimapData.getWidth(), BORDER_WIDTH);;
		minimapData.fillRectangle(0, 0, BORDER_WIDTH, minimapData.getHeight());
		minimapData.fillRectangle(0, minimapData.getHeight()-BORDER_WIDTH, minimapData.getWidth(), BORDER_WIDTH);
		minimapData.fillRectangle(minimapData.getWidth()-BORDER_WIDTH, 0, BORDER_WIDTH, minimapData.getHeight());
		
		Texture minimapTexture = new Texture(minimapData);
		spriteBatch.draw(minimapTexture, minimapX, minimapY);

		// Need to flush because we're about to dispose the texture
		spriteBatch.flush();
		minimapData.dispose();
		minimapTexture.dispose();
	}
}
