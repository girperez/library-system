/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package custom_components;

import database.Database_programCL;
import database.dataFields.Member_dat;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Gerryl
 */
public class ImageMembers extends ImageView{

    private FileChooser fileChooser;
    private File imagefile;
    private InputStream imageinputStream;
    private Database_programCL program;
    private Member_dat member;
    
    public ImageMembers(Member_dat member, Image image) {
        super(image);
        super.setFitWidth(160);
        super.setFitHeight(192);
        super.setPreserveRatio(true);
        super.setSmooth(true);
        
        this.member = member;
        
        if (image.isError()) {
            try {
                imagefile = new File("src/images/blank_pic.png");
                imageinputStream = new FileInputStream(imagefile);
                byte[] img = new byte[imageinputStream.available()];
                imageinputStream.read(img);
                super.setImage(new Image(new ByteArrayInputStream(img)));
            } catch (Exception e) {
            }
        }
        
        fileChooser = new FileChooser();
        
        super.setOnMouseClicked((event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    try {
                        imagefile = fileChooser.showOpenDialog(new Stage(StageStyle.UTILITY));
                        imageinputStream = new FileInputStream(imagefile);
                        byte[] img = new byte[imageinputStream.available()];
                        imageinputStream.read(img);
                        program = new Database_programCL();
                        this.member.setImageInputStream(new ByteArrayInputStream(img));
                        program.editmemberPic(this.member);
                        setImage(new Image(new ByteArrayInputStream(img)));
                    } catch (Exception e) {
                        System.out.println("No file chosen");
                    }
                }
                
            }
        });
    }
}
