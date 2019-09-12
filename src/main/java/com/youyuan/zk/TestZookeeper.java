package com.youyuan.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author zhangyu
 * @version 1.0
 * @description 初始化zookeeper 客户端
 * @date 2018/10/19 17:38
 */
public class TestZookeeper {

    //zookeeper服务器地址
    private String connectionString="172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181";
    //超时时间
    private int connectionTime=2000;
    //zookeeper客户端
    private ZooKeeper zkClient=null;

    /**
     * 初始化客户端
     */
    @Test
    @Before
    public void initClient() throws IOException {
        zkClient=new ZooKeeper(connectionString, connectionTime, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println("type:"+watchedEvent.getType()+"path:"+watchedEvent.getPath());
                try {
                    /*List<String> childrens= zkClient.getChildren("/",true);
                    for (String children:childrens){
                        System.out.println("子节点名称:"+children);
                    }*/
                    Stat stat= zkClient.exists("/brokers",true);
                    System.out.println(stat!=null?true:false);
                    System.out.println("===================");
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void createNode() throws KeeperException, InterruptedException {
        String path=zkClient.create("/beimei","有缘在线科技股份有限公司".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("创建节点路径:"+path);
    }

    @Test
    public void getChirens() throws KeeperException, InterruptedException {
        List<String> childrens= zkClient.getChildren("/",true);
        for (String children:childrens){
            System.out.println("子节点名称:"+children);
        }
        System.out.println("===================");
        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void exist() throws KeeperException, InterruptedException {
        Stat stat= zkClient.exists("/brokers",true);
        System.out.println(stat!=null?true:false);
        Thread.sleep(Long.MAX_VALUE);
    }
}
