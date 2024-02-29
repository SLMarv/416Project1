import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigParser{
    private final NodeList connections;
    private final NodeList devices;
    public ConfigParser(String configPath) {
        try {
            File inputFile = new File(configPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            Element config = (Element) doc.getElementsByTagName("config").item(0);
            connections = buildNodeList(config, "connections");
            devices = buildNodeList(config, "devices");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new  RuntimeException(e.getMessage());
        }
    }

    private NodeList buildNodeList(Element config, String category){
        NodeList categoryList = config.getElementsByTagName(category);
        return ((Element) categoryList.item(0)).getElementsByTagName(category.substring(0, category.length()-1));
    }

    public List<Connection> parseVirtualPorts(String deviceID){
        List<Connection> ports = new ArrayList<>();
        for(String id: parseVirtualPortIDs(deviceID)){
            ports.add(parseDeviceAddress(id));
        }
        return ports;
    }

    public List<String> parseVirtualPortIDs(String deviceID){
        List<String> deviceIDList = new ArrayList<>();
        for (int index = 0; index < connections.getLength(); index++) {
            String content = connections.item(index).getTextContent();
            if (content.contains(deviceID)){
                deviceIDList.add(content.replace(deviceID, "").replace(":",""));
            }
        }
        return deviceIDList;
    }

    public Connection parseDeviceAddress(String deviceID){
        if (deviceID.equals(Device.PRINT_CONNECTION.getIP())){
            return Device.PRINT_CONNECTION;
        }
        Connection connection = null;
        for (int index = 0; index < devices.getLength(); index++) {
            Element device = ((Element)devices.item(index));
            if (device.getAttribute("id").equals(deviceID)){
                connection = new Connection(
                        deviceID,
                        device.getAttribute("ip"),
                        Integer.parseInt(device.getAttribute("port")),
                        );
            }
        }
        if (connection == null){
            throw new RuntimeException("Address of " + deviceID+ " not found.\n");
        }
        return connection;
    }
}
