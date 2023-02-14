package com.joelallison.screens.userinterface;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.joelallison.generation.Layer;
import com.joelallison.screens.CreationSelectScreen;
import com.joelallison.user.Creation;
import com.joelallison.user.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class CreationSelectInterface extends UserInterface {
    ArrayList<Creation.CreationPreview> userCreations;

    public void genUI(final Stage stage) { //stage is made final here so that it can be accessed within inner classes
        ResultSet getCreationsResults = Database.doSqlQuery(
                "SELECT * FROM creation" +
                "WHERE username = '" + CreationSelectScreen.username + "'" +
                "ORDER BY last_accessed_timestamp DSC"
        );

        if (getCreationsResults != null) {
            try {
                while (getCreationsResults.next()) {
                    String name = getCreationsResults.getString("creation_name");
                    Timestamp dateCreated = getCreationsResults.getTimestamp("created_timestamp");
                    Timestamp lastAccessed = getCreationsResults.getTimestamp("last_accessed_timestamp");
                    //get number of layers which are part of this creation
                    int layerCount = Database.doSqlQuery("SELECT COUNT(*) FROM layer WHERE creation_name = '" + name + "'").getInt(0);

                    userCreations.add(new Creation.CreationPreview(name, dateCreated, lastAccessed, layerCount));

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }
}

