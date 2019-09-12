package com.youyuan.zk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author zhangyu
 * @version 1.0
 * @description  模拟服务器上下线客户端代码
 * @date 2018/10/21 19:52
 */
public class DistributeClient {
    private static String connectString = "172.18.32.21:12181,172.18.32.25:12181,172.18.32.16:12181";
    private static int sessionTimeout = 2000;
    private ZooKeeper zk = null;
    private String parentNode = "/servers";

    // 创建到zk的客户端连接
    public void getConnect() throws IOException {
        zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {

            @Override
            public void process(WatchedEvent event) {

                // 再次启动监听
                try {

                    getServerList();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //
    public void getServerList() throws Exception {

        // 获取服务器子节点信息，并且对父节点进行监听
        List<String> children = zk.getChildren(parentNode, true);
        ArrayList<String> servers = new ArrayList<String>();

        for (String child : children) {
            byte[] data = zk.getData(parentNode + "/" + child, false, null);

            servers.add(new String(data));
        }

        // 把servers赋值给成员serverList，已提供给各业务线程使用
        //serversList = servers;

        System.out.println(servers);
    }

    // 业务功能
    public void business() throws Exception {
        System.out.println("client is working ...");
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void main(String[] args) throws Exception {

        // 获取zk连接
        DistributeClient client = new DistributeClient();
        client.getConnect();

        // 获取servers的子节点信息，从中获取服务器信息列表
        client.getServerList();

        // 业务进程启动
        client.business();
    }
}

