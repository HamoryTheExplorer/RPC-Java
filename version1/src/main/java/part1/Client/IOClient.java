package part1.Client;



import part1.common.Message.RpcRequest;
import part1.common.Message.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/4 18:31
 */
public class IOClient {
    //这里负责底层与服务端的通信，发送request，返回response
    public static RpcResponse sendRequest(String host, int port, RpcRequest request){
        try {
            Socket socket=new Socket(host, port); // 通过 socket 与服务端建立 TCP 连接
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream()); // 将对象序列化发送到服务端
            ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());    // 接收并反序列化对象

            oos.writeObject(request); // 将 RpcRequest 对象序列化，并通过输出流发送到服务端
            oos.flush();              // 刷新输出流，确保数据完全发送

            // 从输入流中读取服务端返回的 序列化对象，并反序列化为 RpcResponse
            RpcResponse response=(RpcResponse) ois.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
