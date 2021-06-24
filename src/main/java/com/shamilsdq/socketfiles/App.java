package com.shamilsdq.socketfiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.Socket;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class App extends Application 
{
    
    private static Scene scene;
    private static GUIController gui;
    private static NetworkController network;
    
    private static Task listenTask;
    private static Task transferTask;
    
    private static boolean TRANSFER_LOCK;

    
    @Override
    public void start(Stage stage) throws IOException
    {
        TRANSFER_LOCK = false;
        
        initGUI();
        initNetwork();
        
        stage.setScene(scene);
        stage.setTitle("SOCKET-FILES");
        stage.setMinWidth(800.0);
        stage.setMinHeight(560.0);
        stage.show();
    }
    
    
    public static boolean isLocked()
    {
        return TRANSFER_LOCK;
    }
    
    public static void lock()
    {
        // GUI disable
        TRANSFER_LOCK = true;
    }
    
    public static void unlock()
    {
        // enable GUI
        TRANSFER_LOCK = false;
    }
    
    
    private void initGUI() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("GUI.fxml"));
        scene = new Scene((Parent) fxmlLoader.load());
        gui = (GUIController) fxmlLoader.getController();
    }
    
    private void initNetwork()
    {
        try
        {
            network = new NetworkController();
            listenTask = new Task<Void>() {
                @Override
                protected Void call()
                {
                    while (true)
                    {
                        if (isCancelled()) break;
                        try
                        {
                            Socket socket = network.waitIncomingConnection();
                            if (TRANSFER_LOCK)
                            {
                                socket.getOutputStream().write("BUSY".getBytes());
                                socket.close();
                                throw new Exception("Connection request while busy");
                            }
                    
                            socket.getOutputStream().write("FREE".getBytes());
                            incomingConnectionRequest(socket);
                        }
                        catch (Exception ex)
                        {
                            System.out.println(ex);
                        }
                    }
                    return null;
                }
            };
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                listenTask.cancel();
            }));
            new Thread(listenTask).start();
        }
        catch (IOException ex)
        {
            System.out.println(ex);
            Platform.exit();
        }
    }
    
    
    public void incomingConnectionRequest(Socket socket)
    {
        try
        {
            // get transfer data
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String filename = reader.readLine();
            String filesize = reader.readLine();
            
            gui.incomingRequestPopup(socket, filename, filesize);
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }
    
    public static void acceptReceive(Socket peer)
    {
        network.acceptIncomingConnection(peer);
    }
    
    public static void rejectReceive(Socket peer)
    {
        network.rejectIncomingConnection(peer);
    }
    
    
    @Override
    public void stop()
    {
        if (listenTask != null && listenTask.isRunning())
            listenTask.cancel();
        
        network.close();
    }

    
    public static void main(String[] args) 
    {
        launch();
    }

}