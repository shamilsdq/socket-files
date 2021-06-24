package com.shamilsdq.socketfiles;

import java.net.Socket;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;



public class GUIController 
{
    
    // SEND PANEL
    @FXML private TextField peerAddressInput;
    @FXML private TextField peerFileInput;
    @FXML private Button fileSelectButton;
    @FXML private Button fileSendButton;
    
    // RECEIVE PANEL
    @FXML private Label transferPeer;
    @FXML private Label transferFile;
    @FXML private Label transferStatus;
    
    
    public void initialize()
    {
        System.out.println("initialized GUIController by Shamil");
        this.peerFileInput.setDisable(true);
        this.fileSendButton.setDisable(true);
        
        // TODO: Set handler here itself
        this.fileSelectButton.setOnAction(null);
        this.fileSendButton.setOnAction(null);
    }
    
    
    public void selectedFile(String filename)
    {
        if (filename.isEmpty())
        {
            this.peerFileInput.clear();
            this.fileSendButton.setDisable(true);
        }
        else
        {
            this.peerFileInput.setText(filename);
            this.fileSendButton.setDisable(false);
        }
    }
    
    
    public void incomingRequestPopup(Socket peer, String filename, String filesize)
    {
        System.out.println("incoming request popup");
        ButtonType acceptBtn = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
        ButtonType rejectBtn = new ButtonType("Reject", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(
            Alert.AlertType.INFORMATION,
            "IP: " + peer.getInetAddress().getHostName()
            + "\nFilename: " + filename
            + "\nFilesize: " + filesize,
            acceptBtn, rejectBtn
        );
        alert.setTitle("Incoming request");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.orElse(rejectBtn) == acceptBtn)
        {
            // ACCEPTED
            App.acceptReceive(peer);
        }
        else
        {
            // REJECT
            App.rejectReceive(peer);
        }
    }
    
}
