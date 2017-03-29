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
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * FXML Controller class
 *
 * @author blj0011
 */
public class LoginScreenController implements Initializable
{
    @FXML TextField tfUsername, tfPassword;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        
    }    
    
    @FXML void handleBTNLogin(ActionEvent event)
    {
        if(tfUsername.getText().length() > 0 && tfPassword.getText().length() > 0)
        {
            
            if(checkUsernameAndPassword(tfUsername.getText(), tfPassword.getText()))
            {                
                try 
                {
                    System.out.println("Login successful!");

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
                    Parent root = (Parent)fxmlLoader.load();
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setTitle("Main Screen");
                    stage.setScene(scene);
                    stage.show();

                    ((Node)(event.getSource())).getScene().getWindow().hide();
                }
                catch (IOException ex) 
                {
                    Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                System.out.println("Password incorrect!");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Sign In Dialog");
                alert.setHeaderText("Login Unsuccessful");
                alert.setContentText("Your username and/or password is incorrect!");
                alert.showAndWait(); 
            }           
        }
    }
    
    private boolean checkUsernameAndPassword(String username, String password){        
        try(Connection conn = DriverManager.getConnection("jdbc:sqlite:usersInfo.db");
            PreparedStatement pstmt = createPreparedStatementReadProperty(conn, username);
            ResultSet rs = pstmt.executeQuery();)
        {
            while(rs.next())
            {
                if(password.equals(rs.getString("password")))
                {
                    return true;
                }
            }            
        } 
        catch (SQLException ex) 
        {            
            System.out.println(ex);
        }
        
        return false;
    }
    
    private PreparedStatement createPreparedStatementReadProperty(Connection con, String username) throws SQLException{
        String sql = "SELECT password FROM Login_Info WHERE username = ?;";     
        PreparedStatement ps = con.prepareStatement(sql);        
        ps.setString(1, username);
        
        return ps;
    }
}
