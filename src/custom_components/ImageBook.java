/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package custom_components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Gerryl
 */
public class ImageBook extends ImageView{

    public ImageBook(Image image) {
        super(image);
        super.setFitWidth(160);
        super.setFitHeight(192);
        super.setPreserveRatio(true);
        super.setSmooth(true);
    }

   
}
