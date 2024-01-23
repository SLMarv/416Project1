import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ConfigParserTest {
    private final String testPath = "src\\test\\resources\\testConfig.xml";

    @Test
    public void testParseDeviceAddress(){
        ConfigParser parser = new ConfigParser(testPath);
        Assertions.assertEquals(parser.parseDeviceAddress("A"), new Address("1.12.11.2", 7));
    }

    @Test void testParserParseConnectionsFrom() {
        ConfigParser parser = new ConfigParser(testPath);
        List<Address> addresses = List.of(
                parser.parseDeviceAddress("S2"),
                parser.parseDeviceAddress("A"),
                parser.parseDeviceAddress("B"));
        Assertions.assertEquals(addresses, parser.parseConnectionsFrom("S1"));
    }
}
