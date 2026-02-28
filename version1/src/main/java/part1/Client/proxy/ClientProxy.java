package part1.Client.proxy;


import lombok.AllArgsConstructor;
import part1.Client.IOClient;
import part1.common.Message.RpcRequest;
import part1.common.Message.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/6 16:49
 */
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    // 传入参数service接口的class对象，反射封装成一个request
    private String host;
    private int port;

    // jdk动态代理，每一次代理对象调用方法，都会经过此方法增强（反射获取request对象，socket发送到服务端）
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构建request
        RpcRequest request=RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramsType(method.getParameterTypes()).build();
        // 与服务端进行通信，将请求发送出去，并接受 RpcResponse 响应
        RpcResponse response= IOClient.sendRequest(host,port,request);
        // 获取服务端返回结果，并返回给调用者
        return response.getData();
    }
    // 动态生成一个实现指定接口的代理对象
     public <T>T getProxy(Class<T> clazz){
        // 使用 Proxy.newProxyInstance 方法动态创建一个代理对象，传入类加载器
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)o;
    }
}
