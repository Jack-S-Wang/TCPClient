package com.dascom.operation.utils.tcp;

import com.dascom.operation.entity.tcp.WifiConfig;

/**
 * 解析数据的类
 * @author Leisenquan
 * @time 2018年9月26日 下午12:56:39
 * @project_name ds-operation
 */
public class AnalyseMessage {
	
	public static WifiConfig getWifiConfig(byte[] message, int index) {

        WifiConfig wifiConfig = new WifiConfig();
//		int index=20+4+8;
        StringBuffer sb = new StringBuffer();
        //获取本机ip
        sb.append((message[index + 3] & 0xff) + ".");
        sb.append((message[index + 2] & 0xff) + ".");
        sb.append((message[index + 1] & 0xff) + ".");
        sb.append((message[index] & 0xff));
        wifiConfig.setLocalIpAddress(sb.toString());
        sb.setLength(0);
        index += 4;


        //获取本机子网掩码
        sb.append((message[index + 3] & 0xff) + ".");
        sb.append((message[index + 2] & 0xff) + ".");
        sb.append((message[index + 1] & 0xff) + ".");
        sb.append((message[index] & 0xff));
        wifiConfig.setLocalSubnetMask(sb.toString());
        sb.setLength(0);
        index += 4;
        //获取本机网关
        sb.append((message[index + 3] & 0xff) + ".");
        sb.append((message[index + 2] & 0xff) + ".");
        sb.append((message[index + 1] & 0xff) + ".");
        sb.append((message[index] & 0xff));
        wifiConfig.setLocalGateway(sb.toString());
        sb.setLength(0);
        index += 4;
        //远程服务器地址
        sb.append((message[index + 3] & 0xff) + ".");
        sb.append((message[index + 2] & 0xff) + ".");
        sb.append((message[index + 1] & 0xff) + ".");
        sb.append((message[index] & 0xff));
        wifiConfig.setRemoteServerAddress(sb.toString());
        sb.setLength(0);
        index += 4;
        //主DNS服务器地址
        sb.append((message[index + 3] & 0xff) + ".");
        sb.append((message[index + 2] & 0xff) + ".");
        sb.append((message[index + 1] & 0xff) + ".");
        sb.append((message[index] & 0xff));
        wifiConfig.setMainDNSServerAddress(sb.toString());
        sb.setLength(0);
        index += 4;
        //备用DNS服务器地址
        sb.append((message[index + 3] & 0xff) + ".");
        sb.append((message[index + 2] & 0xff) + ".");
        sb.append((message[index + 1] & 0xff) + ".");
        sb.append((message[index] & 0xff));
        wifiConfig.setSlaveDNSServerAddress(sb.toString());
        sb.setLength(0);
        index += 4;
        //UDP服务端口号
        int udpPort = (message[index] & 0xff) + ((message[index + 1] & 0xff) << 8);
        wifiConfig.setUdpPort(udpPort);
        index += 2;
        //控制端口号
        int controlPort = (message[index] & 0xff) + ((message[index + 1] & 0xff) << 8);
        wifiConfig.setControlPort(controlPort);
        index += 2;
        //控制端口号
        int dataPort = (message[index] & 0xff) + ((message[index + 1] & 0xff) << 8);
        wifiConfig.setDataPort(dataPort);
        index += 2;
        //远程路由的ssid 即wifi名称
        String remoteRouteSsid = new String(message, index, 33);
        wifiConfig.setRemoteRouteSsid(remoteRouteSsid.trim());
        index += 33;
        //远程路由的password 即wifi密码
        String remoteRoutePassword = new String(message, index, 65);
        wifiConfig.setRemoteRoutePassword(remoteRoutePassword.trim());
        index += 65;
        //使能DHCP功能  0x00:无效 , 0x01:有效
        byte dhcp = message[index];
        wifiConfig.setDhcp(dhcp);
        index += 1;
        //重连服务器的间隔时间
        byte reconnectionInterval = message[index];
        wifiConfig.setReconnectionInterval(reconnectionInterval);
        index += 1;
        //本机AP模式网络ssid 即wifi固件名称
        String apModelSsid = new String(message, index, 22);
        wifiConfig.setApModelSsid(apModelSsid.trim());
        index += 22;
        //本机AP模式网络密码
        String apModelPassword = new String(message, index, 16);
        wifiConfig.setApModelPassword(apModelPassword.trim());
        index += 16;
        //网络工作模式     0:station, 1:AP
        byte networkMode = message[index];
        wifiConfig.setNetworkMode(networkMode);
        index += 1;

        //数据端口数据传输速率    0x02：中速；    0x03：高速；    其他:慢速；
        byte dataTransmissionSpeed = message[index];
        wifiConfig.setDataTransmissionSpeed(dataTransmissionSpeed);
        index += 1;
        //网络通信通道
        byte networkChannel = message[index];
        wifiConfig.setNetworkChannel(networkChannel);
        index += 1;
        //dns功能  0x00:无效,0x01:有效
        byte dns = message[index];
        wifiConfig.setDns(dns);
        index += 1;
        //设备型号
        byte deviceModel = message[index];
        wifiConfig.setDeviceModel(deviceModel);
        index += 1;
        //预留
        index += 8;
        //服务器域名
        String domainName = new String(message, index, 32);
        wifiConfig.setDomainName(domainName.trim());
        index += 32;
        return wifiConfig;
    }

}
