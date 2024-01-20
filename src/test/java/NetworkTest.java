import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

class NetworkTest {
    private final String testPath = "src\\test\\resources\\testConfig.xml";

    @Test
    public void testAddress_equals(){
        Network.Address address0 = new Network.Address("S2", 1);
        Network.Address address1 = new Network.Address("S1", 1);
        Network.Address address2 = new Network.Address("S1", 1);
        Assertions.assertEquals(address1, address2);
        Assertions.assertTrue(List.of(address0, address1).contains(address2));
    }

    @Test
    public void testParseDeviceAddress() throws ParserConfigurationException, IOException, SAXException {
        Network.ConfigParser parser = new Network.ConfigParser(testPath);
        Assertions.assertEquals(parser.parseDeviceAddress("A"), new Network.Address("1.12.11.2", 7));
    }

    @Test void testParserParseConnectionsFrom() throws ParserConfigurationException, IOException, SAXException {
        Network.ConfigParser parser = new Network.ConfigParser(testPath);
        List<Network.Address> addresses = List.of(
                parser.parseDeviceAddress("S2"),
                parser.parseDeviceAddress("A"),
                parser.parseDeviceAddress("B"));
        Assertions.assertEquals(addresses, parser.parseConnectionsFrom("S1"));
    }
}
