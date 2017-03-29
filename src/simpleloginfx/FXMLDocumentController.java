/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleloginfx;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.*;

/**
 *
 * @author blj0011
 */
public class FXMLDocumentController implements Initializable
{    
    @FXML private TextField tfEnterUserName, tfEnterPassword;
    
    @FXML
    private void handleBTNAddUser(ActionEvent event)
    {
        if(tfEnterUserName.getText().length() > 0 && tfEnterPassword.getText().length() > 0)
        {
            
            addNewUserToDB(tfEnterUserName.getText(), tfEnterPassword.getText());

            tfEnterUserName.setText("");
            tfEnterPassword.setText("");
        }
        else
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Add New User Dialog");
            alert.setHeaderText("Error adding new user");
            alert.setContentText("Make sure you have entered a username and a password in the appropriate textfield.");
            alert.showAndWait();            
        }
    }
    
    @FXML
    private void handleBTNGoToLoginScreen(ActionEvent event)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Login Screen");
            stage.setScene(scene);
            stage.show();            
            
            ((Node)(event.getSource())).getScene().getWindow().hide(); //close current window after opening new Scene           
        }
        catch (IOException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
       
    }  
    
    private void addNewUserToDB(String username, String password){
        String sql = "INSERT INTO Login_Info(username, password) VALUES(?, ?)"; 
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:usersInfo.db");
             PreparedStatement ps = conn.prepareStatement(sql);)
        {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.execute();
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Add New User Alert");
            alert.setContentText("New user added successfully!\nUsername: " + username + "\nPassword: " + password);
            alert.showAndWait();
        } 
        catch (SQLException e) 
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Add New User Alert");
            alert.setContentText("Username may already exist. Try a different username.");
            alert.showAndWait();
        }
    }
}
