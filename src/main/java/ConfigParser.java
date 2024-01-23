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
            connections = ((Element) config.getElementsByTagName("connections").item(0)).getElementsByTagName("connection");
            devices = ((Element) config.getElementsByTagName("devices").item(0)).getElementsByTagName("device");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new  RuntimeException(e.getMessage());
        }
    }

    public List<Address> parseConnectionsFrom(String deviceID){
        List<Address> deviceAddressList = new ArrayList<>();
        for (int index = 0; index < connections.getLength(); index++) {
            String content = connections.item(index).getTextContent();
            if (content.contains(deviceID)){
                deviceAddressList.add(parseDeviceAddress(content.replace(deviceID, "").replace(":","")));
            }
        }
        return deviceAddressList;
    }

    public Address parseDeviceAddress(String deviceID){
        Address address = null;
        for (int index = 0; index < devices.getLength(); index++) {
            Element device = ((Element)devices.item(index));
            if (device.getAttribute("id").equals(deviceID)){
                address = new Address(device.getAttribute("ip"), Integer.parseInt(device.getAttribute("port")));
            }
        }
        if (address == null){
            throw new RuntimeException("Address of " + deviceID+ " not found.\n");
        }
        return address;
    }
}
