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
        List<String> addresses = List.of("S2", "A", "B");
        Assertions.assertEquals(addresses, parser.parseVirtualPortIDs("S1"));
    }
}
