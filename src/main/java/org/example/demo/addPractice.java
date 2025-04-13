package org.example.demo;
//libraries to import
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class addPractice {
    addPractice(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Practice Question Details");

        // Create the GridPane layout
        GridPane grid = new GridPane();
        grid.setPrefSize(500, 400);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Create labels and text fields
        TextField IdField = new TextField();
        TextField LevelField = new TextField();
        TextField TitleField = new TextField();
        TextArea DescArea = new TextArea();
        TextField AnswerField = new TextField();

        // Add components to the grid
        Label Insert = new Label("Your Question");
        Insert.getStyleClass().add("answerTitle-label");
        grid.add(Insert, 0, 0);

        //attributes-label
        Label idLabel = new Label("ID:");
        idLabel.getStyleClass().add("attributes-label");

        grid.add(idLabel, 0, 1);
        grid.add(IdField, 1, 1);

        Label TitleLabel = new Label("Title:");
        TitleLabel.getStyleClass().add("attributes-label");
        grid.add(TitleLabel, 0, 2);
        grid.add(TitleField, 1, 2);

        Label lvlLabel = new Label("Level (1-5):");
        lvlLabel.getStyleClass().add("attributes-label");

        grid.add(lvlLabel, 0, 3);
        grid.add(LevelField, 1, 3);

        Label descLabel = new Label("Description:");
        descLabel.getStyleClass().add("attributes-label");
        grid.add(descLabel, 0, 5);
        grid.add(DescArea, 1, 5);

        Label ansLabel = new Label("Answer:");
        //ansLabel.getStyleClass().add("attributes-label");

        Label infoLabel = new Label("(if unsolved, leave blank)");

        AnswerField.setPrefWidth(100);
        TitleField.setPrefWidth(100);
        IdField.setPrefWidth(30);

        grid.add(ansLabel, 0, 6);
        grid.add(infoLabel, 1, 7);
        grid.add(AnswerField, 1, 6);

        DescArea.setPrefWidth(300);
        DescArea.setPrefHeight(200);
        DescArea.setWrapText(true);
        // Add the grid to the dialog
        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/AnswerFormStyle.css")).toExternalForm());
        dialog.getDialogPane().setContent(grid);

        // Add OK and Cancel buttons
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        ArrayList<String> input;
        // Show the dialog and process input
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = IdField.getText();
            String title = TitleField.getText();
            String desc = DescArea.getText();
            String ans = AnswerField.getText();
            String lvl = LevelField.getText();
            //checking if everything is valid, you entered everything and its valid
            if(id.isEmpty() || title.isEmpty() || desc.isEmpty() || lvl.isEmpty()) {
                JOptionPane.showMessageDialog(null, "One of the fields is not filled. Try re-entering your question.", "Hold Up! Something's not right...", JOptionPane.INFORMATION_MESSAGE);
            }
            else if(lvl_isnt_Valid(lvl)){
                JOptionPane.showMessageDialog(null, "your level isn't valid. Try re-entering your question.", "Hold Up! Something's not right...", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                //if you didn't mess up inputting, it will now proceed to add that as a question
                System.out.println(id + " " + title + " " + desc + " " + ans + " " + lvl);
                //String id, int level, String title, String desc, String answer
                practicePanels.addQuestionToJson(id, Integer.parseInt(lvl), desc, ans, title);
                ClientApplication.sendQuestion(id, Integer.parseInt(lvl), title, desc, ans);
            }

        } else {
            System.out.println("User canceled input.");
        }
    }
    public static void  enterPracticeQ() {
        addPractice p1 = new addPractice();
    }
    private static boolean lvl_isnt_Valid(String lvl){
        //checking if lvl that was inputted is in range
        try{
            int idInt = Integer.parseInt(lvl);
            if(idInt > 5 || idInt < 1){
                return true;
            }
            return false;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
