package com.example.demo5;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;
import java.sql.*;

import java.io.IOException;

import static jdk.internal.org.jline.utils.Colors.s;

public class HelloApplication extends Application {

    TextField a;
    TextField b;
    TextField c;
    String a1,fg,c1;
    public void start(Stage stage) throws IOException{
        stage.setTitle("STUDENT DETAIL FORM");
        GridPane s=new GridPane();
        s.setHgap(10);
        s.setVgap(5);
        s.setAlignment(Pos.CENTER);
        Label l1=new Label("ENTER YOUR NAME:");
        a=new TextField();
        l1.setStyle("-fx-font-weight: bold");
        s.addRow(1,l1,a);
        Label l2=new Label("ENTER YOUR DEPARTMENT:");
        b=new TextField();
        l2.setStyle("-fx-font-weight: bold");
        s.addRow(2,l2,b);
        Label l3=new Label("ENTER YOUR ROLLNO:");
        c=new TextField();
        l3.setStyle("-fx-font-weight: bold");
        s.addRow(3,l3,c);
        Button b1=new Button("Submit");
        Button b2=new Button("Reset");
        s.addRow(5,b1,b2);
        b1.setOnMouseClicked(event ->{
            a1=a.getText();
            fg=b.getText();
            c1=c.getText();
            try {
                Platform.exit();
                connection();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Scene sc=new Scene(s,350,250);
        s.setStyle("-fx-background-color:skyblue");
        stage.setScene(sc);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    public void connection()
    {
        String sql;
        Connection connection = null;
        try {
            // below two lines are used for connectivity.
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/books",
                    "root", "123456");
            Statement statement;
            statement = connection.createStatement();
            int resultSet;
            sql="insert into student(SNAME,DEPARTMENT,ROLLNO)values("+"'" + a1 + "'"+","+"'"+fg+"'"+","+"'"+c1+"'"+");";
            System.out.println(sql);
            resultSet=statement.executeUpdate(sql);
            if(resultSet>0){
                System.out.println("INSERTED SUCESSFULLY");
            }
            else{
                System.out.println("NOT INSERTED SUCESSFULLY");
            }
            statement.close();
            connection.close();
        }
        catch (Exception exception) {
            System.out.println(exception);
        }

    }

}