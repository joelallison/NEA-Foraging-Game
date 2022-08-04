package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {

    public Player(int xPos, int yPos){
        super(xPos, yPos);
    }

    public void handleMovement(){ //was going to make a container class but remembered the libgdx Vector2 class
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            yPos++;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.S)){
            yPos--;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            xPos--;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D)){
            xPos++;
        }
    }

}
