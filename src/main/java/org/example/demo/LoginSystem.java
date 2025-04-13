package org.example.demo;
//libraries to import
import com.google.gson.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.text.html.ImageView;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

import javafx.scene.image.Image;

import static javafx.application.Platform.exit;

public class LoginSystem {
    public class User {
        String username;
        String password;

        User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        String getUsername() {
            return username;
        }
    }

    private Dialog<ButtonType> dialog;
    private GridPane mainGrid;
    private Button existingUser;
    private Button newUser;

    public LoginSystem() {
        dialog = new Dialog<>();
        dialog.setTitle("LockIn Login System");

        // Create main layout
        mainGrid = new GridPane();
        mainGrid.setPrefSize(400, 400);
        mainGrid.setPadding(new Insets(10));
        mainGrid.setHgap(10);
        mainGrid.setVgap(10);
        mainGrid.setAlignment(Pos.CENTER);
        //create labels to help user guide understand what's going on:
        Label welcome = new Label("Welcome to LockIn!");
        welcome.getStyleClass().add("answerTitle-label");


        Label details = new Label("Click what you want to do. To create a new account, click 'Create Account'. If this isn't your first time, click 'Login'");
        details.setWrapText(true);
        details.getStyleClass().add("attributes-label");


        mainGrid.add(welcome, 0, 0);
        mainGrid.add(details, 0, 1);

        // Create buttons
        existingUser = new Button("Login");

        newUser = new Button("Create Account");

        existingUser.setPrefSize(380,50);

        newUser.setPrefSize(380,50);


        // Add buttons to main grid
        mainGrid.add(existingUser, 0, 3);
        mainGrid.add(newUser, 0, 4);

        // Button actions
        existingUser.setOnAction(e -> showLoginForm());
        newUser.setOnAction(e -> showNewAccount());

        // Display the dialog, add style
        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/AnswerFormStyle.css")).toExternalForm());
        dialog.getDialogPane().setContent(mainGrid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            exit();
        }

    }

    private void showLoginForm() {
        GridPane loginGrid = new GridPane();
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.setPrefSize(400,400);
        loginGrid.setPadding(new Insets(10));
        loginGrid.setHgap(10);
        loginGrid.setVgap(10);
        //interface changes to a different one, this is to login

        Label insertUser = new Label("Welcome Back!");
        insertUser.setAlignment(Pos.CENTER);
        insertUser.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));


        Label details = new Label("Enter your details:");
        details.getStyleClass().add("answerTitle-label");
        loginGrid.add(details, 0, 1);

        Label usernameLabel = new Label("Username:");
        usernameLabel.getStyleClass().add("attributes-label");
        TextField usernameField = new TextField();
        loginGrid.add(usernameLabel, 0, 2);
        loginGrid.add(usernameField, 0, 3);

        Label passwordLabel = new Label("Password:");
        passwordLabel.getStyleClass().add("attributes-label");
        PasswordField passwordField = new PasswordField();
        loginGrid.add(passwordLabel, 0, 4);
        loginGrid.add(passwordField, 0, 5);

        // Back button to return to main screen
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> dialog.getDialogPane().setContent(mainGrid));

        // Submit button, checks if username is valid
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            if (usernameField.getText().isEmpty()) {
                System.out.println("Username cannot be empty");
            } else {
                User u1 = searchUser(usernameField.getText(), passwordField.getText());
                if (u1.getUsername().isEmpty()) { //if username and password arent the same/cant be found, alert message
                    JOptionPane.showMessageDialog(null, "Invalid username or password", "Attention!", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Invalid username or password");
                } else {
                    ClientApplication.setUsername(usernameField.getText());
                    dialog.setResult(ButtonType.OK);  // Close dialog on successful input
                }
            }
        });

        loginGrid.add(backButton, 0, 6);
        loginGrid.add(submitButton, 1, 5);

        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/AnswerFormStyle.css")).toExternalForm());
        // Update the dialog content dynamically
        dialog.getDialogPane().setContent(loginGrid);
    }

    private void showNewAccount() {
        GridPane registerGrid = new GridPane();
        registerGrid.setAlignment(Pos.CENTER);
        registerGrid.setPrefSize(400,400);
        registerGrid.setPadding(new Insets(10));
        registerGrid.setHgap(10);
        registerGrid.setVgap(10);

        //labels and UI Initalized

        Label insertUser = new Label("Create a new Account:");
        insertUser.setAlignment(Pos.CENTER);
        insertUser.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        registerGrid.add(insertUser, 0, 0);

        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Tahoma", 15));
        TextField usernameField = new TextField();
        registerGrid.add(usernameLabel, 0, 1);
        registerGrid.add(usernameField, 0, 2);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Tahoma", 15));
        PasswordField passwordField = new PasswordField();
        registerGrid.add(passwordLabel, 0, 3);
        registerGrid.add(passwordField, 0, 4);

        insertUser.getStyleClass().add("answerTitle-label");
        usernameLabel.getStyleClass().add("attributes-label");
        passwordLabel.getStyleClass().add("attributes-label");
        // Back button to return to main screen
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> dialog.getDialogPane().setContent(mainGrid));

        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                System.out.println("Username and Password cannot be empty"); //checks if null case
            } else if(IsUsernameInUse(usernameField.getText())){ //checks if the username is already taken
                JOptionPane.showMessageDialog(null, "Username is already in use", "Attention!", JOptionPane.INFORMATION_MESSAGE);
                usernameField.setText(""); //resets the text
            }
            else {
                //else -> will go through and make a new account with username and password
                addAccount(usernameField.getText(), passwordField.getText());
                ClientApplication.setUsername(usernameField.getText());
                dialog.setResult(ButtonType.OK);  // Close dialog on successful input
            }
        });

        //add buttons
        registerGrid.add(backButton, 0, 7);
        registerGrid.add(submitButton, 1, 7);

        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/AnswerFormStyle.css")).toExternalForm());
        // Update the dialog content dynamically
        dialog.getDialogPane().setContent(registerGrid);
    }

    private static void addAccount(String username, String password) {
        String filePath = "src/main/java/users.json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonData;
        //parses JSon data
        try {
            String json = Files.exists(Paths.get(filePath)) ? Files.readString(Paths.get(filePath)) : "{}";
            jsonData = JsonParser.parseString(json).getAsJsonObject();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            return;
        }

        //creates a new user as a sort of "base" so its info can be added to the json file, because the json file acts like a group of objects
        JsonArray users = jsonData.has("users") ? jsonData.getAsJsonArray("users") : new JsonArray();
        JsonObject newUser = new JsonObject();
        newUser.addProperty("username", username);
        newUser.addProperty("password", password);

        //adds the new user to the group of users
        users.add(newUser);
        jsonData.add("users", users);  //uses users
        try {
            Files.write(Paths.get(filePath), gson.toJson(jsonData).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User searchUser(String username, String password) {
        String filePath = "src/main/java/users.json";
        Gson gson = new Gson();

        try {
            FileReader reader = new FileReader(filePath);
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            reader.close();

            JsonArray usersArray = jsonObject.getAsJsonArray("users");  // Fixed key from "questions" to "users"
            //goes through the whole group of users and checks if the name and password match
            for (JsonElement userElement : usersArray) {
                JsonObject user = userElement.getAsJsonObject();
                if (user.get("username").getAsString().equals(username) &&
                        user.get("password").getAsString().equals(password)) {
                    return new User(username, password);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //otherwise they do not match, the user returns both null attributes, which is also invalid, so the system will ask you again to input proper username and password
        return new User("", "");
    }

    private boolean IsUsernameInUse(String username) {
        String filePath = "src/main/java/users.json";
        Gson gson = new Gson();
        //similar to searchUser, this will go through all of the usernames. if it's in use, it returns true, and makes you try again

        try {
            FileReader reader = new FileReader(filePath);
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            reader.close();

            JsonArray usersArray = jsonObject.getAsJsonArray("users");

            //goes through each user; sees if they have the same name
            for (JsonElement userElement : usersArray) {
                JsonObject user = userElement.getAsJsonObject();
                if (user.get("username").getAsString().equals(username)) {
                    return true;
                }
            }
            //otherwise return false, because the username wasn't found
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
