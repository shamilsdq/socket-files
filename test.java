import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

class test
{
    public static void main(String[] args)
    {
        try
        {
            Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 5023);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            System.out.println(reader.readLine());

            writer.write("godzilla\n\n");
            writer.write("200MB\n\n");

            System.out.println(reader.readLine());

            reader.close();
            writer.close();
            socket.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }
}